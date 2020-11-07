package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoFall implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity player = e.getEntity();

        if(player instanceof Player) {

            if (Main.scenarios.contains("nofall")) {

                if (!e.isCancelled()) {

                    if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL))
                        e.setCancelled(true);
                }
            }
        }
    }

}
