package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Main;
import com.belmu.uhc.Utils.CountdownWithDouble;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class FinalHeal implements Listener {

    public static void finalHeal(int seconds) {

        if(Main.scenarios.contains("finalheal")) {

            CountdownWithDouble heal = new CountdownWithDouble(Main.getInstance(),
                    seconds,
                    () -> {
                    },
                    () -> {

                        for(Player all : Bukkit.getOnlinePlayers()) {

                            if(Main.players.contains(all.getName())) {

                                all.setHealth(20);
                                all.setFoodLevel(20);
                                all.playSound(all.getLocation(), Sound.VILLAGER_YES, 1, Integer.MAX_VALUE);
                            }
                        }

                        Bukkit.broadcastMessage(Main.prefix + "§aFinal Heal proceeded!");
                    },
                    (t) -> {

                    }
            );
            heal.scheduleTimer();
        }
    }

}
