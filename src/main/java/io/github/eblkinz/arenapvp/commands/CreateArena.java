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
		if (args.length == 0)
		{
			p.sendMessage(ChatColor.RED + "You must specify a name for the arena.");
			return;
		}
		
		String id = args[0];
		
		if (ArenaBuilder.getArena(id) != null)
		{
			p.sendMessage(ChatColor.RED + "An arena with that name already exists.");
			return;
		}
		
		ArenaBuilder.addArena(id);
		p.sendMessage(ChatColor.GREEN + "Created arena " + ChatColor.GOLD + id + ChatColor.GREEN + ".");
	}
}
