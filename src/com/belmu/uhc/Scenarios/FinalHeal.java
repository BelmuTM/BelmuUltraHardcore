package com.belmu.uhc.Scenarios;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Utils.Countdown;
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

    public void execute(int seconds) {
        if(plugin.scenarios.contains("finalheal")) {

            Countdown heal = new Countdown(plugin,
                    seconds,
                    () -> {},
                    () -> {
                        for(Player all : Bukkit.getOnlinePlayers()) {

                            if(plugin.players.contains(all.getUniqueId())) {
                                all.setHealth(20);
                                all.setFoodLevel(20);
                                all.playSound(all.getLocation(), Sound.CAT_MEOW, 1, Integer.MAX_VALUE);
                            }
                        }
                        Bukkit.broadcastMessage(plugin.prefix + "§aFinal Heal §eproceeded!");
                    },
                    (t) -> {}
            );
            heal.scheduleTimer();
        }
    }

}
