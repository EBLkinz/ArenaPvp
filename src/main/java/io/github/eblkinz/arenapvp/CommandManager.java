package io.github.eblkinz.arenapvp;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.eblkinz.arenapvp.commands.Ping;

public class CommandManager implements CommandExecutor
{
	private ArrayList<GameCommand> cmds;	// List of all game commands
	
	/*
	 * Adds all game commands to the arraylist.
	 */
	
	protected CommandManager()
	{
		cmds = new ArrayList<>();
		
		cmds.add(new Ping());
	}
	
	/*
	 * Called whenever a command specified in plugin.yml is run.
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		// If the sender is not a player
		if (!(sender instanceof Player))
		{
			// Print a warning and return
			sender.sendMessage(ChatColor.RED + "This command can only be run by players!");
			return true;
		}
		
		// Cast the command sender to player
		Player p = (Player) sender;
		
		// If the command entered is '/arenapvp'
		if (cmd.getName().equalsIgnoreCase("arenapvp"))
		{
			// If there are no arguments included
			if (args.length == 0)
			{
				// For each game command in the cmds list
				for (GameCommand gcmd : cmds)
				{
					// Get the game command's info
					CommandInfo info = gcmd.getClass().getAnnotation(CommandInfo.class);
					
					// Create a string with every alias of the command separated by a colon
					String aliases = String.join(":", info.aliases());
					
					// Send the player a message detailing the game command's usage and return
					p.sendMessage(ChatColor.GOLD + "/" + cmd.getName() + " " + aliases + " " +
								  info.usage() + "- " + ChatColor.WHITE + info.description());
				}
				
				return true;
			}
			
			// Initialize a new game command
			GameCommand wanted = null;
			
			// For each game command in the cmds list
			for (GameCommand gcmd : cmds)
			{
				// Get the game command's info
				CommandInfo info = gcmd.getClass().getAnnotation(CommandInfo.class);
				
				// For each alias in the info's list of aliases
				for (String alias : info.aliases())
				{
					// If the alias equals the command's argument
					if (alias.equalsIgnoreCase(args[0]))
					{
						// Store the game command in wanted and break
						wanted = gcmd;
						break;
					}
				}
			}
			
			// If no command matched the argument entered
			if (wanted == null) {
				// Print a warning and return
				p.sendMessage(ChatColor.RED + "Could not find command. Use '/arenapvp' for help.");
				return true;
			}
			
			// Copy the remaining arguments to args
			args = Arrays.copyOfRange(args, 1, args.length);
			
			// Call the game command stored in wanted. Use the player and remaining arguments.
			wanted.onCommand(p, args);
		}
		
		return true;
	}
}
