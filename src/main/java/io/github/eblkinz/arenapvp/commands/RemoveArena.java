package io.github.eblkinz.arenapvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.eblkinz.arenapvp.ArenaBuilder;
import io.github.eblkinz.arenapvp.CommandInfo;
import io.github.eblkinz.arenapvp.GameCommand;
import io.github.eblkinz.arenapvp.SettingsManager;

@CommandInfo(description = "Removes an arena.", usage = "<id>", aliases = {"removearena", "ra"}, requiresPerm = true)
public class RemoveArena extends GameCommand
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
		
		// If the there's no arena with that ID
		if (ArenaBuilder.getArena(id) == null)
		{
			// Display a warning message
			p.sendMessage(ChatColor.RED + "An arena with that id doesn't exist.");
		}
		else
		{
			// Remove the specified arena and display a confirmation message
			SettingsManager.getArenas().set(id, null);
			p.sendMessage(ChatColor.GREEN + "Removed arena " + ChatColor.GOLD + id + ChatColor.GREEN + ".");
		}
	}
}