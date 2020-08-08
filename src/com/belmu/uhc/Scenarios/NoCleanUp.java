package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Main;
import com.belmu.uhc.Utils.Options;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class NoCleanUp implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        Player target = e.getEntity();
        Player killer = target.getKiller();

        if(killer != null) {

            int hearts = Options.noCleanUpHearts * 2;

            if (Main.scenarios.contains("nocleanup")) {

                if(killer.getHealth() < 20 && killer.getHealth() > 0) {

                    killer.setHealth(killer.getHealth() + hearts);
                    killer.sendMessage(Main.prefix + "§a+§c " + (hearts / 2) + "§4❤");

                }

            }

        }

    }

}
