package io.github.eblkinz.arenapvp;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Test
{
	@EventHandler
	public void onEatCake(PlayerInteractEvent e)
	{
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CAKE)
		{
			
		}
	}
}
