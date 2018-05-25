package io.github.eblkinz.arenapvp;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Arena
{	
	private String id;					// Holds the arena's ID
	
	private ArenaState state;			// Holds the arena's state
	
	private Location lobbySpawn;		// Holds the location of the lobby
	
	private ArrayList<Location> spawns;	// Holds a list of arena spawns
	private ArrayList<Chest> chests;	// Holds a list of arena chests
	private ArrayList<UUID> players;	// Holds a list of player's UUIDs
	
	/*
	 * Defines the various states an arena can be in.
	 */
	
	public enum ArenaState
	{
		WAITING, COUNTDOWN, STARTED
	}
	
	/**
	 * Creates a new arena with the specified ID, lobby, spawns and chests.
	 * @param arenaID The ID of the arena.
	 * @param lobby The location of the lobby.
	 * @param arenaSpawns A list of arena spawns.
	 * @param arenaChests A list of arena chests.
	 */
	
	protected Arena(String arenaID, Location lobby, ArrayList<Location> arenaSpawns, ArrayList<Chest> arenaChests)
	{
		// Set the ID of the arena to the specified arena ID
		id = arenaID;
		
		// Set the state of the arena to WAITING
		setState(ArenaState.WAITING);
		
		// Set the location of the arena lobby spawn to the specified location
		lobbySpawn = lobby;
		
		// Set the location(s) of the arena spawn(s) to the specified location ArrayList
		spawns = arenaSpawns;
		
		// Set the chest(s) of the arena to the specified chest ArrayList
		chests = arenaChests;
		
		// Create a new empty UUID ArrayList to hold player's UUIDs
		players = new ArrayList<UUID>();
	}
	
	/**
	 * Gets the arena's ID.
	 * @return The arena's ID.
	 */
	
	public String getID()
	{
		return id;
	}
	
	/**
	 * Gets the arena's state.
	 * @return The arena's state.
	 */
	
	public ArenaState getState()
	{
		return state;
	}
	
	/**
	 * Sets the arena's state.
	 * @param state The new arena state.
	 */
	
	protected void setState(ArenaState arenaState) // TODO Potentially make this private
	{
		state = arenaState;
		//SignManager.getInstance().updateSigns(this);
	}
	
	/**
	 * Sets the lobby spawn that the players return to after the game.
	 * @param loc The location of the new lobby spawn.
	 */
	
	public void setLobby(Location loc)
	{
		// Set the lobby location of the arena
		SettingsManager.getArenas().set(id + ".lobby", loc);
		lobbySpawn = loc;
	}
	
	/**
	 * Gets an array of the arena's spawn locations.
	 * @return The arena's spawn locations.
	 */
	
	public Location[] getSpawns()
	{		
		return spawns.toArray(new Location[spawns.size()]);
	}
	
	/**
	 * Adds a spawn location to the list of spawns.
	 * @param loc The location to add.
	 */
	
	public void addSpawn(Location loc)
	{
		// Get the number of spawns created for this arena so far
		int spawnID = SettingsManager.getArenas().<ConfigurationSection>get(id + ".spawns").getKeys(false).size();
		
		// Add the spawn to the arenas configuration file and the spawns ArrayList
		SettingsManager.getArenas().set(id + ".spawns." + spawnID, loc);
		spawns.add(loc);
		
		//SignManager.getInstance().updateSigns(this);
	}
	
	/**
	 * Adds a new chest to the arena.
	 * @param chest The chest to add.
	 */
	
	public void addChest(Chest chest)
	{
		// Get the number of chests created for this arena so far
		int chestID = SettingsManager.getArenas().<ConfigurationSection>get(id + ".chests").getKeys(false).size();
		
		// Add the chest to the arenas configuration file and the chests ArrayList
		SettingsManager.getArenas().set(id + ".chests." + chestID, chest.getLocation());
		chests.add(chest);
	}
	
	/**
	 * Gets an array of every player in the arena.
	 * @return Every player in the arena.
	 */
	
	public Player[] getPlayers()
	{
		// Create an empty player list
		ArrayList<Player> playerList = new ArrayList<Player>();
		
		// For each UUID in the players ArrayList
		for (UUID u : players)
		{
			// Get player from UUID and add it to the player list
			Player p = Bukkit.getPlayer(u);
			playerList.add(p);
		}
		
		// Return an array of every player in the arena
		return playerList.toArray(new Player[playerList.size()]);
	}
	
	/**
	 * Returns whether a given player is in the arena.
	 * @param p The player to test.
	 * @return Whether the player was found.
	 */
	
	public boolean hasPlayer(Player p)
	{
		return players.contains(p.getUniqueId());
	}
	
	/**
	 * Adds a player to the arena.
	 * @param p The player to add.
	 */
	
	public void addPlayer(Player p)
	{
		// If the arena is full
		if (!isFull())
		{
			// Add the player's UUID to the players ArrayList and teleport them to the arena
			players.add(p.getUniqueId());
			p.teleport(spawns.get(players.size() - 1));
			
			// If the arena is full and waiting to begin a game
			if (isFull() && state == ArenaState.WAITING)
			{
				// Set the arena's state to countdown
				setState(ArenaState.COUNTDOWN);
				
				// Create a new countdown
				Countdown c = new Countdown(30, this, 30, 20, 15, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
				
				// Run the countdown
				c.runTaskTimer(Main.getPlugin(), 0, 20L);
			}
		}
	}
	
	/**
	 * Removes a player from the arena.
	 * @param p The player to remove.
	 */
	
	public void removePlayer(Player p)
	{
		// Remove the player's UUID from the players ArrayList and teleport them to the lobby spawn
		players.remove(p.getUniqueId());
		p.teleport(lobbySpawn);
		
		// If the arena is currently active
		if (players.size() <= 1 && state == ArenaState.STARTED)
		{
			// If there is only one player left
			if (players.size() == 1)
			{
				// Get the winner
				Player winner = getPlayers()[0];
				
				// Teleport the winner to spawn and broadcast a victory message
				winner.teleport(lobbySpawn);
				winner.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + winner.getName() +
													ChatColor.GOLD + " has won arena " + id + "!");
				
				// Remove the player's UUID from the players ArrayList
				players.remove(0);
			}
			else
			{
				// Broadcast that the arena has ended
				Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Arena " + id + " has ended.");
			}
			
			// Set the arena's state to waiting
			setState(ArenaState.WAITING);
		}
	}
	
	/**
	 * Returns whether the arena is full.
	 * @return Whether the arena is full.
	 */
	
	public boolean isFull()
	{
		if (players.size() < spawns.size())
		{
			return false;
		}
		
		return true;
	}
}
