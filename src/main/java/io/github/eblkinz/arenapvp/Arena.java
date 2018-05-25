package io.github.eblkinz.arenapvp;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Arena
{	
	private String id;					// Holds the arena's ID
	
	private ArenaState state;			// Holds the arena's state
	
	private Location lobbySpawn;		// Holds the location of the lobby
	
	private ArrayList<UUID> players;	// Holds a list of player's UUIDs
	private ArrayList<Sign> signs;		// Holds a list of arena signs
	private ArrayList<Location> spawns;	// Holds a list of arena spawns
	
	
	/*
	 * Defines the various states an arena can be in.
	 */
	
	public enum ArenaState
	{
		WAITING, COUNTDOWN, STARTED
	}
	
	/**
	 * Creates a new arena with the specified ID, lobby, spawns and signs.
	 * @param arenaID The ID of the arena.
	 * @param lobby The location of the lobby.
	 * @param arenaSigns A list of arena signs.
	 * @param arenaSpawns A list of arena spawns.
	 */
	
	protected Arena(String arenaID, Location lobby, ArrayList<Sign> arenaSigns, ArrayList<Location> arenaSpawns)
	{
		// Set the ID of the arena to the specified arena ID
		id = arenaID;
		
		// Set the location of the arena lobby spawn to the specified location
		lobbySpawn = lobby;
		
		// Set the sign(s) of the arena to the specified sign ArrayList
		signs = arenaSigns;
		
		// Set the location(s) of the arena spawn(s) to the specified location ArrayList
		spawns = arenaSpawns;
		
		// Create a new empty UUID ArrayList to hold player's UUIDs
		players = new ArrayList<>();
		
		// Set the state of the arena to WAITING
		setState(ArenaState.WAITING);
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
	
	protected void setState(ArenaState arenaState)
	{
		state = arenaState;
		updateSigns();
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
	 * Gets an array of every player in the arena.
	 * @return Every player in the arena.
	 */
	
	public Player[] getPlayers()
	{
		// Create an empty player list
		ArrayList<Player> playerList = new ArrayList<>();
		
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
		if (isFull())
		{
			// Display a warning and return
			p.sendMessage(ChatColor.RED + "This arena is full.");
			return;
		}
		else
		{
			// Attempt to get the arena the player is currently in
			Arena current = ArenaBuilder.getArena(p);
			
			// If the player is in an arena
			if (current != null)
			{
				// If the player's current arena is the same as the one they are trying to join
				if (current.getID().equals(id))
				{
					// Display a warning message and return
					p.sendMessage(ChatColor.RED + "You are already in this arena.");
					return;
				}
				else
				{
					// Remove the player from their current arena and display a notification
					current.removePlayer(p);
					p.sendMessage(ChatColor.GREEN + "Leaving arena " + ChatColor.GOLD +
								  current.getID() + ChatColor.GREEN + ".");
				}
			}
			
			// Display a confirmation message
			p.sendMessage(ChatColor.GREEN + "Joining arena " + ChatColor.GOLD + id + ChatColor.GREEN + ".");
			
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
			else
			{
				// Update the arena's signs
				updateSigns();
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
		else
		{
			// Update the arena's signs
			updateSigns();
		}
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
		
		updateSigns();
	}
	
	public boolean isLobbySign(Sign sign)
	{
		if (signs.contains(sign))
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Adds a new sign to the arena.
	 * @param sign The sign to add.
	 */
	
	public void addSign(Sign sign)
	{
		// Get the location of the sign
		Location loc = sign.getLocation();
		
		// For each lobby sign in the signs ArrayList
		for (Sign lobbySign : signs)
		{
			// If the lobby's location is the same as the current signs location
			if (lobbySign.getLocation().equals(loc))
			{
				// The sign already exists
				return;
			}
		}
		
		// Get the number of signs created for this arena so far
		int signID = SettingsManager.getArenas().<ConfigurationSection>get(id + ".signs").getKeys(false).size();
		
		// Add the sign to the arenas configuration file and the sings ArrayList
		SettingsManager.getArenas().set(id + ".signs." + signID, sign.getLocation());
		signs.add(sign);
		
		// Update the arena's signs
		updateSigns();
	}
	
	/**
	 * Updates every lobby sign for this arena.
	 */
	
	public void updateSigns()
	{
		// Get the number of players and spawns in the arena
		int numOfPlayers = players.size(),
			numOfSpawns = spawns.size();
		
		// Get the arena's current state as a string
		String arenaState = state.toString();
		
		// For each lobby sign in the signs ArrayList
		for (Sign sign : signs)
		{
			// Set the first line of the sign to "[Arena Pvp]"
			sign.setLine(0, ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Arena Pvp" + ChatColor.DARK_GREEN + "]");
			
			// Set the second line of the sign to the arena's ID
			sign.setLine(1, ChatColor.DARK_GREEN + id);
			
			// Set the third line of the sign to the arena's current state
			if (state == ArenaState.WAITING)
			{
				sign.setLine(2, ChatColor.GREEN + arenaState);
			}
			else if (state == ArenaState.COUNTDOWN)
			{
				sign.setLine(2, ChatColor.YELLOW + arenaState);
			}
			else if (state == ArenaState.STARTED)
			{
				sign.setLine(2, ChatColor.RED + arenaState);
			}
			
			// If the number of spawns isn't 0
			if (numOfSpawns != 0)
			{
				// Set the fourth line of the sign to the number of players over the number of spawns
				if ((numOfPlayers / numOfSpawns) < 0.25)
				{
					sign.setLine(3, ChatColor.GREEN + "" + numOfPlayers + "/" + numOfSpawns);
				}
				else if ((numOfPlayers / numOfSpawns) < 0.5)
				{
					sign.setLine(3, ChatColor.YELLOW + "" + numOfPlayers + "/" + numOfSpawns);
				}
				else if ((numOfPlayers / numOfSpawns) < 0.75)
				{
					sign.setLine(3, ChatColor.GOLD + "" + numOfPlayers + "/" + numOfSpawns);
				}
				else if ((numOfPlayers / numOfSpawns) == 1)
				{
					sign.setLine(3, ChatColor.RED + "" + numOfPlayers + "/" + numOfSpawns);
				}
			}
			else
			{
				// Set the fourth line of the sign to the number of layers over the number of spawns
				sign.setLine(3, ChatColor.DARK_RED + "" + numOfPlayers + "/" + numOfSpawns);
			}
			
			// Update the sign
			sign.update();
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
