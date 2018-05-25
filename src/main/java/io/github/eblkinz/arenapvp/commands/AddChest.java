package io.github.eblkinz.arenapvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import io.github.eblkinz.arenapvp.Arena;
import io.github.eblkinz.arenapvp.ArenaBuilder;
import io.github.eblkinz.arenapvp.CommandInfo;
import io.github.eblkinz.arenapvp.GameCommand;

@CommandInfo(description = "Adds a chest to an arena.", usage = "<id>",
			 aliases = {"addchest", "ac"}, requiresPerm = true)
public class AddChest extends GameCommand
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
		
		// Attempt to get the arena the player is adding a chest to
		Arena arena = ArenaBuilder.getArena(args[0]);
		
		// If the there's no arena with that ID
		if (arena == null)
		{
			// Display a warning message and return
			p.sendMessage(ChatColor.RED + "An arena with that id doesn't exist.");
			return;
		}
		
		// Get the block the player is currently looking at
		Block target = p.getTargetBlock(null, 10);
		
		// If the block is air
		if (target == null)
		{
			// Display a warning message and return
			p.sendMessage(ChatColor.RED + "You need to be looking at a block.");
			return;
		}
		
		// If the block is not a chest
		if (!(target instanceof Chest))
		{
			// Display a warning message
			p.sendMessage(ChatColor.RED + "You need to be looking at a chest.");
		}
		else
		{
			// Get the chest at the target location
			Chest chest = (Chest) target.getState();
			
			// Get the chest's location
			Location loc = chest.getLocation();
			
			// Add a chest to the specified arena and display a confirmation message
			arena.addChest(chest);
			p.sendMessage(ChatColor.GREEN + "A chest has been added to arena " + ChatColor.GOLD +
						  args[0] + ChatColor.GREEN + " at " + ChatColor.GOLD + loc.getBlockX() +
						  " " + loc.getBlockY() + " " + loc.getBlockZ() + ChatColor.GREEN + ".");
		}
	}
}
