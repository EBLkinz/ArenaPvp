package io.github.eblkinz.arenapvp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Test implements Listener
{
	@EventHandler
	public void onEatCake(PlayerInteractEvent e)
	{
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK &&
			e.getClickedBlock().getType() == Material.CAKE_BLOCK)
		{
			Player p = e.getPlayer();
			
			p.sendMessage(ChatColor.AQUA + "You feel a rush of energy!");
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
							  10, 1, false, false), true);
		}
	}
}
