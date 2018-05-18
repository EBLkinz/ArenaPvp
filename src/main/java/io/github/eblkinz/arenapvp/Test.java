package io.github.eblkinz.arenapvp;

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
		Player p = e.getPlayer();
		if (p.getFoodLevel() < 20 &&
			e.getAction() == Action.RIGHT_CLICK_BLOCK &&
			e.getClickedBlock().getType() == Material.CAKE_BLOCK)
		{
			p.sendMessage(SettingsManager.getTest().get("data").toString());
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
							  200, 1, false, false), true);
		}
	}
}