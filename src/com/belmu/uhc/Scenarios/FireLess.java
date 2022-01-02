package com.belmu.uhc.Scenarios;

import com.belmu.uhc.UHC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class FireLess implements Listener {

    public final UHC plugin;
    public FireLess(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity player = e.getEntity();

        if(player instanceof Player) {
            if (plugin.scenarios.contains("fireless") && !e.isCancelled()) {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)) {
                    e.setCancelled(true);
                    player.setFireTicks(0);
                }
            }
        }
    }
}
