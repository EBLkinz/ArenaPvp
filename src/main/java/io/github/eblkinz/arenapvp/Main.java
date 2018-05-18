package io.github.eblkinz.arenapvp;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	/**
	 * Runs when the server starts.
	 */
	
	@Override
	public void onEnable()
	{
		// Register server events
		registerEvents(this, new Test());
		
		// Log test keys
		//getLogger().info(SettingsManager.getTest().getKeys().toString());
	}
	
	/**
	 * Runs when the server stops.
	 */
	
	@Override
	public void onDisable()
	{
		// Log test keys
		//getLogger().info(SettingsManager.getTest().getKeys().toString());
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
			Bukkit.getServer().getPluginManager().registerEvents(listener,
																 plugin);
		}
	}
}
