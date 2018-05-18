package io.github.eblkinz.arenapvp;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		//registerEvents(this, new );
	}
	
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
	 * Registers event listeners.
	 * @param plugin The plugin to register the events to.
	 * @param listeners The listeners to register.
	 */
	
	public static void registerEvents(Plugin plugin, Listener... listeners)
	{
		//For each listener register events
		for (Listener listener : listeners)
		{
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}
}
