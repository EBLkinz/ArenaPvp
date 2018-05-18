package io.github.eblkinz.arenapvp;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		getLogger().info("Enabled");
	}
	
	@Override
	public void onDisable()
	{
		getLogger().info("Disabled");
	}
}
