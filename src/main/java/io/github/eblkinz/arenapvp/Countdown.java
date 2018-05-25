package io.github.eblkinz.arenapvp;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.eblkinz.arenapvp.Arena.ArenaState;

public class Countdown extends BukkitRunnable
{

	private int i;								// Holds the current count
	
	private Arena counting;						// Holds the arena counting down
	
	private ArrayList<Integer> countingNums;	// Holds a list of integers that trigger notifications
	
	/**
	 * Creates a new countdown with the specified start, arena, and trigger numbers.
	 * @param start The number to countdown from.
	 * @param arena The arena counting down.
	 * @param cNums The trigger numbers.
	 */
	
	public Countdown(int start, Arena arena, int... cNums)
	{
		// Set the arena counting down
		counting = arena;
		
		// Set the starting number
		i = start;
		
		// Create a new empty ArrayList
		countingNums = new ArrayList<>();
		
		// For each number in cNums
		for (int c : cNums)
		{
			// Add the number to the counting numbers ArrayList
			countingNums.add(c);
		}
	}
	
	/*
	 * Runs when the countdown runnable is scheduled.
	 */
	
	@Override
	public void run()
	{
		// If the arena has fewer than two players
		if (counting.getPlayers().length < 2)
		{
			// Set the arena to waiting for players and cancel the countdown
			counting.setState(ArenaState.WAITING);
			cancel();
			return;
		}
		else if (i == 0) // Else if the countdown reaches 0
		{
			// For each player in the arena
			for (Player p : counting.getPlayers())
			{
				// Display a chat message and a title letting them know the game has begun
				p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "The game has begun!");
				p.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "GET READY!", ChatColor.GREEN +
							"" + ChatColor.BOLD + "The game has begun!", 10, 30, 10);
				
				// Play a sound at the player's location
				p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 10, 1);
			}
			
			// Start the arena and cancel the countdown
			counting.setState(ArenaState.STARTED);
			cancel();
			return;
		}
		else if (countingNums.contains(i)) // Else if the count is on one of the display numbers
		{
			// For each player in the arena
			for (Player p : counting.getPlayers()) 
			{
				// Display a chat message
				p.sendMessage(ChatColor.GREEN + "The game will begin in " +
							  ChatColor.GOLD + i + ChatColor.GREEN + " seconds!");
				
				// If the count is less than or equal to 10
				if (i <= 10) 
				{
					// Display a title
					p.sendTitle(ChatColor.GREEN + "The game will", ChatColor.GREEN + "begin in " +
								ChatColor.GOLD + i + ChatColor.GREEN + " seconds!", 0, 20, 0);
					
					// Play a sound at the player's location
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 2f - ((i - 1) / 9.0f));
				}
			}
		}
		
		// Subtract 1 from the count
		i--;
	}
}

