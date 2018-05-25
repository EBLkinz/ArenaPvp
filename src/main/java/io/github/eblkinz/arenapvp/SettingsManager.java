package io.github.eblkinz.arenapvp;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class SettingsManager
{
	private final static SettingsManager
		arenas = new SettingsManager("arenas");	// Holds information about arenas
	
	/**
	 * Gets the arenas config file.
	 * @return the arenas config file.
	 */
	
	public static SettingsManager getArenas()
	{
		return arenas;
	}
	
	private File file;							// A blank file
	private FileConfiguration config;			// A blank configuration file
	private Plugin plugin = Main.getPlugin();	// Holds the plugin
	
	/*
	 * Creates a new configuration file with a specified filename.
	 */
	
	private SettingsManager(String fileName)
	{
		// If the plugin doesn't have a data folder
		if (!plugin.getDataFolder().exists())
		{
			// Log the lack of a data folder
			plugin.getLogger().info("Couldn't find data folder. Creating a new data folder.");
			
			// Make a new data folder
			plugin.getDataFolder().mkdirs();
		}
		
		// Create a new file with the specified file name
		file =  new File(plugin.getDataFolder(), fileName + ".yml");
		
		// If the file doesn't exist
		if (!file.exists())
		{
			// Log the lack of a file
			plugin.getLogger().info("Couldn't find " + fileName + ".yml. Creating a new config file.");
			
			// Try to create a new file
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			// Create a new Reader object to hold the default configuration
			Reader defaultConfig = null;
			
			// Try to fetch the default configuration
			try
			{
				defaultConfig = new InputStreamReader(plugin.getResource(fileName + ".yml"), "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			
			// If a default was loaded
			if (defaultConfig != null)
			{
				// Log the configuration changes
				plugin.getLogger().info("Loading default file " + fileName + ".yml.");
				
				// Load the default configuration into config
				config = YamlConfiguration.loadConfiguration(defaultConfig);
				
				// Save the configuration and return
				save();
				return;
			}
		}
		
		// Load the file configuration into config
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	/**
	 * Gets a set of keys as a String.
	 * @return A set of keys.
	 */
	
	public Set<String> getKeys()
	{
		return config.getKeys(false);
	}
	
	/**
	 * Gets the value(s) stored at a specified string path.
	 * @param path The path to get from.
	 * @return The value(s) stored.
	 */
	
	@SuppressWarnings("unchecked")
	public <T> T get(String path)
	{
		return (T) config.get(path);
	}
	
	/**
	 * Sets the value of a particular path.
	 * @param path The path to set at.
	 * @param value The value to set.
	 */
	
	public void set(String path, Object value)
	{
		config.set(path, value);
		save();
	}
	
	/*
	 * Attempts to save the configuration file.
	 */
	
	private void save()
	{
		try
		{
			config.save(file);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns whether a string path exists.
	 * @param path The path to check.
	 * @return Whether the path exists.
	 */
	
	public boolean contains(String path)
	{
		return config.contains(path);
	}
	
	/**
	 * Creates a new section in the config file.
	 * @param path The path to create.
	 * @return The section.
	 */
	
	public ConfigurationSection createSection(String path)
	{
		ConfigurationSection section = config.createSection(path);
		save();
		return section;
	}
}
