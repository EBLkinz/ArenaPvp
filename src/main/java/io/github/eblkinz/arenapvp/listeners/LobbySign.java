package io.github.eblkinz.arenapvp.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import io.github.eblkinz.arenapvp.Arena;
import io.github.eblkinz.arenapvp.ArenaBuilder;

public class LobbySign implements Listener {
	
	@EventHandler
	public void onSignChange(SignChangeEvent e)
	{
		// If the first line of the sign is "<ap>" and the player has permission to create lobby signs
		if (e.getLine(0).equalsIgnoreCase("<ap>") && e.getPlayer().hasPermission("lordofthearena")) 
		{
			// Attempt to get the arena listed on the second line of the sign
			Arena arena = ArenaBuilder.getArena(ChatColor.stripColor(e.getLine(1)));
			
			// If the arena exists
			if (arena != null)
			{
				// Set the first line of the sign to "[Arena Pvp]" and the second line to the arena ID
				e.setLine(0, ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Arena Pvp" + ChatColor.DARK_GREEN + "]");
				e.setLine(1, ChatColor.GREEN + arena.getID());
				
				// Add the sign to the arena's list of lobby signs
				arena.addSign((Sign) e.getBlock().getState());
			}	
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		// If the player right clicked a sign
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK &&
			(e.getClickedBlock().getType() == Material.SIGN ||
			 e.getClickedBlock().getType() == Material.SIGN_POST ||
			 e.getClickedBlock().getType() == Material.WALL_SIGN))
		{
			// Get the sign the player clicked and the arena that sign is linked to
			Sign sign = (Sign) e.getClickedBlock().getState();
			Arena arena = ArenaBuilder.getArena(ChatColor.stripColor(sign.getLine(1)));
			
			// If the arena exists and the sign is a lobby sign of that arena
			if (arena != null && arena.isLobbySign(sign))
			{
				// Add the player to the arena
				arena.addPlayer(e.getPlayer());
			}
		}
	}
}
