package io.github.eblkinz.arenapvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.github.eblkinz.arenapvp.Arena;
import io.github.eblkinz.arenapvp.ArenaBuilder;
import io.github.eblkinz.arenapvp.CommandInfo;
import io.github.eblkinz.arenapvp.GameCommand;

@CommandInfo(description = "Adds a spawn to an arena.", usage = "<id>",
			 aliases = {"addspawn", "as"}, requiresPerm = true)
public class AddSpawn extends GameCommand
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
		
		// Attempt to get the arena the player is adding a spawn to
		Arena arena = ArenaBuilder.getArena(args[0]);
		
		// If the there's no arena with that ID
		if (arena == null)
		{
			// Display a warning message and return
			p.sendMessage(ChatColor.RED + "An arena with that id doesn't exist.");
		}
		else
		{
			// Get the players current location
			Location loc = p.getLocation();
			
			// Add a spawn to the specified arena and display a confirmation message
			arena.addSpawn(loc);
			p.sendMessage(ChatColor.GREEN + "A spawn has been added to arena " + ChatColor.GOLD +
						  args[0] + ChatColor.GREEN + " at " + ChatColor.GOLD + loc.getBlockX() +
						  " " + loc.getBlockY() + " " + loc.getBlockZ() + ChatColor.GREEN + ".");
		}
	}
}
