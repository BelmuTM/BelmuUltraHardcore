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
public class NoFall implements Listener {

    public final UHC plugin;
    public NoFall(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity player = e.getEntity();

        if(player instanceof Player) {
            if (plugin.scenarios.contains("nofall")) {
                if (!e.isCancelled()) {

                    if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) e.setCancelled(true);
                }
            }
        }
    }

}
