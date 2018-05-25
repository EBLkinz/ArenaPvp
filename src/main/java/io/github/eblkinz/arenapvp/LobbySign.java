package io.github.eblkinz.arenapvp;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import io.github.eblkinz.arenapvp.Arena.ArenaState;

public class LobbySign {

	private Sign sign;
	private Arena arena;
	
	public LobbySign(Sign s, Arena a) {
		sign = s;
		
		arena = a;
		
		update();
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public Location getLocation() {
		return sign.getLocation();
	}
	
	public Sign getSign() {
		return sign;
	}
	
	public void update() {
		sign.setLine(0, ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Survival Games" + ChatColor.DARK_GREEN + "]");
		sign.setLine(1, ChatColor.DARK_GREEN + arena.getID());
		if (arena.getState() == ArenaState.WAITING) {
			sign.setLine(2, ChatColor.GREEN + arena.getState().toString());
		}
		else if (arena.getState() == ArenaState.COUNTDOWN) {
			sign.setLine(2, ChatColor.YELLOW + arena.getState().toString());
		}
		else if (arena.getState() == ArenaState.STARTED) {
			sign.setLine(2, ChatColor.RED + arena.getState().toString());
		}
		
		if ((arena.getPlayers().length / arena.getSpawns().length) < 0.25) {
			sign.setLine(3, ChatColor.GREEN + "" + arena.getPlayers().length + ChatColor.DARK_GREEN + "/" + ChatColor.GREEN + arena.getSpawns().length);
		}
		else if ((arena.getPlayers().length / arena.getSpawns().length) < 0.5) {
			sign.setLine(3, ChatColor.YELLOW + "" + arena.getPlayers().length + ChatColor.GOLD + "/" + ChatColor.YELLOW + arena.getSpawns().length);
		}
		else if ((arena.getPlayers().length / arena.getSpawns().length) < 0.75) {
			sign.setLine(3, ChatColor.GOLD + "" + arena.getPlayers().length + ChatColor.RED + "/" + ChatColor.GOLD + arena.getSpawns().length);
		}
		else if ((arena.getPlayers().length / arena.getSpawns().length) == 1) {
			sign.setLine(3, ChatColor.RED + "" + arena.getPlayers().length + ChatColor.DARK_RED + "/" + ChatColor.RED + arena.getSpawns().length);
		}
		sign.update();
	}	
}
