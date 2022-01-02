package com.belmu.uhc.Scenarios;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Utility.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class FinalHeal implements Listener {

    public final UHC plugin;
    public FinalHeal(UHC plugin) {
        this.plugin = plugin;
    }

    public void execute(int time) {
        if(plugin.scenarios.contains("finalheal")) {

            Countdown heal = new Countdown(plugin,
                    time,
                    () -> {},
                    () -> {
                        for(Player all : Bukkit.getOnlinePlayers()) {

                            if(plugin.players.contains(all.getUniqueId())) {
                                all.setHealth(20);
                                all.setFoodLevel(20);
                                all.playSound(all.getLocation(), Sound.CAT_MEOW, 1, Integer.MAX_VALUE);
                            }
                        }
                        Bukkit.broadcastMessage(plugin.prefix + "§aFinal heal proceeded!");
                    },
                    (t) -> {}
            );
            heal.scheduleTimer();
        }
    }
}
