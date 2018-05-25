package io.github.eblkinz.arenapvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.eblkinz.arenapvp.ArenaBuilder;
import io.github.eblkinz.arenapvp.CommandInfo;
import io.github.eblkinz.arenapvp.GameCommand;

@CommandInfo(description = "Creates a new arena.", usage = "<id>", aliases = {"createarena", "ca"}, requiresPerm = true)
public class CreateArena extends GameCommand
{
	@Override
	public void onCommand(Player p, String[] args)
	{
		// If no ID was given
		if (args.length == 0)
		{
			// Display a warning message and return
			p.sendMessage(ChatColor.RED + "You must specify an id for the arena.");
			return;
		}
		
		// Get the user entered ID
		String id = args[0];
		
		// If the there's already an arena with that ID
		if (ArenaBuilder.getArena(id) != null)
		{
			// Display a warning message
			p.sendMessage(ChatColor.RED + "An arena with that id already exists.");
		}
		else
		{
			// Create a new arena with the specified ID and display a confirmation message
			ArenaBuilder.addArena(id);
			p.sendMessage(ChatColor.GREEN + "Created arena " + ChatColor.GOLD + id + ChatColor.GREEN + ".");
		}
	}
}
