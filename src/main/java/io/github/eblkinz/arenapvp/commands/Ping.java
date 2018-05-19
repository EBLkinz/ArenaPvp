package io.github.eblkinz.arenapvp.commands;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.eblkinz.arenapvp.CommandInfo;
import io.github.eblkinz.arenapvp.GameCommand;

@CommandInfo(description = "Ping the server.", usage = "", aliases = {"ping"})
public class Ping extends GameCommand
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
			// Print a message using a random color and style
			p.sendMessage(colors[random.nextInt(colors.length)] + "" + styles[random.nextInt(styles.length)] + "PONG!");
		}
		else
		{
			// Print a message using a random color
			p.sendMessage(colors[random.nextInt(colors.length)] + "PONG!");
		}
	}
}
