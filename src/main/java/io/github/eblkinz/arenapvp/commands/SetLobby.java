package io.github.eblkinz.arenapvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.github.eblkinz.arenapvp.Arena;
import io.github.eblkinz.arenapvp.ArenaBuilder;
import io.github.eblkinz.arenapvp.CommandInfo;
import io.github.eblkinz.arenapvp.GameCommand;

@CommandInfo(description = "Sets the location of an arena's lobby.", usage = "<id>",
			 aliases = {"setlobby", "sl"}, requiresPerm = true)
public class SetLobby extends GameCommand
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
		
		// Attempt to get the arena the player is trying to set the lobby of
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
			
			// Set the lobby location of the specified arena and display a confirmation message
			arena.setLobby(loc);
			p.sendMessage(ChatColor.GREEN + "The lobby for arena " + ChatColor.GOLD + args[0] +
						  ChatColor.GREEN + " has been set to " + ChatColor.GOLD + loc.getBlockX() +
						  " " + loc.getBlockY() + " " + loc.getBlockZ() + ChatColor.GREEN + ".");
		}
	}
}
