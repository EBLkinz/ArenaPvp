package io.github.eblkinz.arenapvp.commands;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.eblkinz.arenapvp.CommandInfo;
import io.github.eblkinz.arenapvp.GameCommand;

@CommandInfo(description = "Pings the entire server.", usage = "",
			 aliases = {"adminping", "aping"}, requiresPerm = true)
public class AdminPing extends GameCommand
{
	@Override
	public void onCommand(Player p, String[] args)
	{
		// Create arrays to hold the various ChatColor adaptations
		ChatColor[] colors = {ChatColor.DARK_GREEN, ChatColor.GREEN, ChatColor.AQUA, ChatColor.GOLD, ChatColor.BLUE};
		ChatColor[] styles = {ChatColor.BOLD, ChatColor.ITALIC, ChatColor.MAGIC};
		
		// Create a new instance of the Random object
		Random random = new Random();
		
		// One in five chance
		if (random.nextInt(5) == 0)
		{
			// Broadcast a message using a random color and style
			p.getServer().broadcastMessage(colors[random.nextInt(colors.length)] + "" +
										   styles[random.nextInt(styles.length)] + "PONG!");
		}
		else
		{
			// Broadcast a message using a random color
			p.getServer().broadcastMessage(colors[random.nextInt(colors.length)] + "PONG!");
		}
	}
}
