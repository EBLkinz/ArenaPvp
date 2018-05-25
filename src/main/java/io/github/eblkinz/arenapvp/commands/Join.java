package io.github.eblkinz.arenapvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.eblkinz.arenapvp.Arena;
import io.github.eblkinz.arenapvp.ArenaBuilder;
import io.github.eblkinz.arenapvp.CommandInfo;
import io.github.eblkinz.arenapvp.GameCommand;

@CommandInfo(description = "Joins an arena.", usage = "<id>", aliases = {"join", "j"}, requiresPerm = false)
public class Join extends GameCommand
{
	@Override
	public void onCommand(Player p, String[] args)
	{
		// If no ID was given
		if (args.length == 0)
		{
			// Display a warning message and return
			p.sendMessage(ChatColor.RED + "You must specify an arena.");
			return;
		}
		
		// Get the user entered ID
		String id = args[0];
		
		// Attempt to get the arena the player is trying to join
		Arena next = ArenaBuilder.getArena(id);
		
		// If the arena the user is trying to join doesn't exist
		if (next == null)
		{
			// Display a warning message and return
			p.sendMessage(ChatColor.RED + "An arena with that id doesn't exist.");
			return;
		}
		
		// Join the arena
		next.addPlayer(p);
	}
}
