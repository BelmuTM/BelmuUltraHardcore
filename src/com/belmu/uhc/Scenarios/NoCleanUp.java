package com.belmu.uhc.Scenarios;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Core.Options;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class NoCleanUp implements Listener {

    public final UHC plugin;
    public NoCleanUp(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        Player target = e.getEntity();
        Player killer = target.getKiller();

        if(killer != null) {
            int hearts = Options.noCleanUpHearts * 2;
            if (plugin.scenarios.contains("nocleanup")) {

                if(killer.getHealth() < 20 && killer.getHealth() > 0) {

                    killer.setHealth(killer.getHealth() + hearts);
                    killer.sendMessage(plugin.prefix + "§a+§c " + (hearts / 2) + "§4❤");
                }
            }
        }
    }

}
