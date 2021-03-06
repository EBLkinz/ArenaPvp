package io.github.eblkinz.arenapvp;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.eblkinz.arenapvp.listeners.LobbySign;

public class Main extends JavaPlugin
{
	/**
	 * Runs when the server starts.
	 */
	
	@Override
	public void onEnable()
	{
		// Register server events
		registerEvents(this, new LobbySign());
		
		// Set command executor and tab completer
		PluginCommand pc = getCommand("arenapvp");
		pc.setExecutor(new CommandManager());
		pc.setTabCompleter(new CommandManager());
		
		// Run the ArenaBuilder setup
		ArenaBuilder.setup();
	}
	
	/**
	 * Runs when the server stops.
	 */
	
	@Override
	public void onDisable()
	{
		
	}
	
	/**
	 * Gets the ArenaPvp plugin.
	 * @return The ArenaPvp plugin.
	 */
	
	public static Plugin getPlugin()
	{
		return Bukkit.getServer().getPluginManager().getPlugin("ArenaPvp");
	}
	
	/**
	 * Registers custom event listeners.
	 * @param plugin The plugin to register the events to.
	 * @param listeners The listeners to register.
	 */
	
	public static void registerEvents(Plugin plugin, Listener... listeners)
	{
		//For each listener register events
		for (Listener listener : listeners)
		{
			//Register the event
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}
}
