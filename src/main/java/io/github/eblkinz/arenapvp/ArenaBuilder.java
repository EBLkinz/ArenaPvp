package io.github.eblkinz.arenapvp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public final class ArenaBuilder
{
	// Create an ArrayList to hold arenas
	private static ArrayList<Arena> arenas = new ArrayList<>();
	
	/*
	 * The ArenaBuilder class is a static utility class and should not be instantiated.
	 */
	
	private ArenaBuilder()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Loads arenas from the arenas configuration file. Needs to be run in onEnable()!
	 */
	
	public static void setup()
	{
		// Clear the list of arenas
		arenas.clear();
		
		// For each arena ID in the arenas configuration file
		for (String arenaID : SettingsManager.getArenas().getKeys())
		{
			// Build the arena
			addArena(arenaID);
		}
	}
	
	/**
	 * Gets an arena given the arena's ID.
	 * @param id The ID of the arena.
	 * @return The arena with the specified ID or null if no arena was found.
	 */
	
	public static Arena getArena(String id)
	{
		// For each arena in the arenas ArrayList
		for (Arena arena : arenas)
		{
			// If the arena has the correct ID
			if (arena.getID().equals(id))
			{
				// Return the arena
				return arena;
			}
		}
		
		// No arena was found
		return null;
	}
	
	/**
	 * Gets an arena with the given player inside.
	 * @param p The player to search for.
	 * @return The arena the player is in or null if no arena was found.
	 */
	
	public static Arena getArena(Player p)
	{
		// For each arena in the arenas ArrayList
		for (Arena arena : arenas)
		{
			// If the player is in the arena
			if (arena.hasPlayer(p))
			{
				// Return the arena
				return arena;
			}
		}
		
		// No arena was found
		return null;
	}
	
	/**
	 * Creates a new arena with a given ID and adds it to a list. If the arena already exists, reload the arena.
	 * @param id The ID to create the arena with.
	 */
	
	public static void addArena(String id)
	{		
		Location lobbySpawn;							// Holds the location of the arena's lobby
		ArrayList<Location> spawns = new ArrayList<>();	// Holds the arena's spawn locations
		ArrayList<Sign> signs = new ArrayList<>();		// Holds the arena's signs
		
		// Attempt to get an arena with the same ID
		Arena a = getArena(id);
		
		// If an arena with the same ID already exists
		if (a != null)
		{
			// Remove it
			arenas.remove(a);
		}
		
		// If the arena's lobby settings already exist
		if (SettingsManager.getArenas().contains(id + ".lobby"))
		{
			// Load the lobby settings
			lobbySpawn = SettingsManager.getArenas().<Location>get(id + ".lobby");
		}
		else
		{
			// Create the lobby section in the arenas configuration file
			SettingsManager.getArenas().createSection(id + ".lobby");
			
			// Save the location of the world spawn for this server
			Location loc = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
			
			// Set the lobby spawn to the world spawn
			SettingsManager.getArenas().set(id + ".lobby", loc);
			lobbySpawn = loc;
		}
		
		// If the arena's spawn settings already exist
		if (SettingsManager.getArenas().contains(id + ".spawns"))
		{
			// For each spawn ID in the arenas configuration file
			for (String spawnID : SettingsManager.getArenas().<ConfigurationSection>get(id + ".spawns").getKeys(false))
			{
				// Load the spawn settings
				spawns.add(SettingsManager.getArenas().<Location>get(id + ".spawns." + spawnID));
			}
		}
		else
		{
			// Create the spawn section in the arenas configuration file
			SettingsManager.getArenas().createSection(id + ".spawns");
		}
		
		// If the arena's sign settings already exist
		if (SettingsManager.getArenas().contains(id + ".signs"))
		{			
			// For each sign ID in the arenas configuration file
			for (String signID : SettingsManager.getArenas().<ConfigurationSection>get(id + ".signs").getKeys(false))
			{
				// Save the location stored at the sign ID
				Location loc = SettingsManager.getArenas().<Location>get(id + ".signs." + signID);
				
				// If the block at the specified location is a sign
				if (loc.getBlock().getState() instanceof Sign)
				{
					// Get the sign located at that block
					Sign sign = (Sign) loc.getBlock().getState();
					
					// If the sign corresponds to the correct arena
					if (ChatColor.stripColor(sign.getLine(1)).equals(id))
					{
						// Load the sign settings
						signs.add(sign);
					}
					else 
					{
						// Remove the sign from the arenas configuration file
						SettingsManager.getArenas().set(id + ".signs." + signID, null);
					}
				}
				else
				{
					// Remove the sign from the arenas configuration file
					SettingsManager.getArenas().set(id + ".signs." + signID, null);
				}
			}
		}
		else
		{
			// Create the sign section in the arenas configuration file
			SettingsManager.getArenas().createSection(id + ".signs");
		}
		
		// Create a new Arena object and add it to the arenas ArrayList
		arenas.add(new Arena(id, lobbySpawn, signs, spawns));
	}
}
