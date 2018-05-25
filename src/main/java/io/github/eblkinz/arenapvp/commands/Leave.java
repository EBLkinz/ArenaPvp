package io.github.eblkinz.arenapvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.eblkinz.arenapvp.Arena;
import io.github.eblkinz.arenapvp.ArenaBuilder;
import io.github.eblkinz.arenapvp.CommandInfo;
import io.github.eblkinz.arenapvp.GameCommand;

@CommandInfo(description = "Leaves an arena.", usage = "", aliases = {"leave", "l"}, requiresPerm = false)
public class Leave extends GameCommand
{
	@Override
	public void onCommand(Player p, String[] args)
	{
		// Attempt to get the arena the player is currently in
		Arena arena = ArenaBuilder.getArena(p);
		
		// If the player is in an arena
		if (arena == null)
		{
			// Display a warning message
			p.sendMessage(ChatColor.RED + "You aren't in an arena.");	
		}
		else
		{
			// Remove the player from their current arena and display a confirmation message
			arena.removePlayer(p);
			p.sendMessage(ChatColor.GREEN + "Leaving arena " + ChatColor.GOLD + arena.getID() + ChatColor.GREEN + ".");
		}
	}
}
