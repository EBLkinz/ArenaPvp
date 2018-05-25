package io.github.eblkinz.arenapvp;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

public class SignBuilder {
	
	// Create an ArrayList to hold signs
	private static ArrayList<LobbySign> signs = new ArrayList<LobbySign>();

	/*
	 * The SignBuilder class is a static utility class and should not be instantiated.
	 */
	
	private SignBuilder()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Loads signs from the signs configuration file. Needs to be run in onEnable()!
	 */
	
	public static void setup()
	{
		// Clear the list of signs
		signs.clear();
		
		// For each arena ID in the signs configuration file
		for (String arenaID : SettingsManager.getSigns().getKeys())
		{
			// Save the number of signs for this arena ID and the arena with that ID
			int numOfSigns = SettingsManager.getSigns().<ConfigurationSection>get(arenaID).getKeys(false).size() - 1;
			Arena arena = ArenaBuilder.getArena(arenaID);
			
			Main.getPlugin().getLogger().info(arenaID + " has " + numOfSigns + " signs."); // TODO REMOVE THIS!
			
			// For each sign for this arena
			for (int i = 0; i < numOfSigns; i++)
			{
				// Build the sign
				//addSign(SettingsManager.getSigns().<Location>get(arenaID + "." + i), arena);
			}
		}
	}
	
	/**
	 * Gets an array of signs for a given arena.
	 * @param a The arena of the signs.
	 * @return An array of signs with the specified arena.
	 */
	
	public static LobbySign[] getSigns(Arena arena)
	{
		// Create a new ArrayList of every sign for the arena
		ArrayList<LobbySign> arenaSigns = new ArrayList<LobbySign>();
		
		// For each sign in the signs ArrayList
		for (LobbySign sign : signs)
		{
			// If the sign has the correct arena
			if (sign.getArena().getID() == arena.getID())
			{
				// Add the sign to the arena signs ArrayList
				arenaSigns.add(sign);
			}
		}
		
		// Return an array of every sign for the specified arena
		return arenaSigns.toArray(new LobbySign[arenaSigns.size()]);
	}
	
	/**
	 * Gets a sign given the signs block location.
	 * @param block The block that contains the sign.
	 * @return The sign with the specified block or null if no sign was found.
	 */
	
	public static LobbySign getSign(Block block)
	{
		// If the block is a sign
		if (block.getState() instanceof Sign)
		{
			// Get the sign from the block
			Sign targetSign = (Sign) block.getState();
			
			// For each sign in the signs ArrayList
			for (LobbySign lobbySign : signs)
			{
				// If the lobby sign is equal to the target sign
				if (lobbySign.getSign().equals(targetSign)) 
				{
					// Return the lobby sign
					return lobbySign;
				}
			}
		}
		
		// No sign was found
		return null;
	}
	
	public static void addSign(Sign sign, Arena arena)
	{
		String arenaID = arena.getID();
		
		if (!SettingsManager.getSigns().contains(arenaID))
		{
			SettingsManager.getSigns().createSection(arenaID);
		}
		else
		{
			int numOfSigns = SettingsManager.getSigns().<ConfigurationSection>get(arenaID).getKeys(false).size() - 1;
			
			// For each sign for this arena
			for (int i = 0; i < numOfSigns; i++)
			{
				
			}
		}
		
		signs.add(new LobbySign(sign, arena));
	}
	
	public static void updateSigns(Arena arena)
	{
		for (LobbySign sign : signs)
		{
			if (sign.getArena().getID() == arena.getID())
			{
				sign.update();
			}
		}
	}
		
}