package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Main;
import com.belmu.uhc.Utils.CountdownWithInt;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class Netheribus implements Listener {

    private static String prefix = "§7[§4Netheribus§7] ";
    private static int seconds = 30;

    public static void startNetheribus() {

        if(Main.scenarios.contains("netheribus")) {

            CountdownWithInt nether = new CountdownWithInt(Main.getInstance(),
                    10,
                    () -> {
                    },
                    () -> {
                        Bukkit.broadcastMessage(Main.prefix + prefix + "§cThe§b Netheribus§c started! Staying in the Overworld will now deal you damage.");

                        new BukkitRunnable() {

                            @Override
                            public void run() {

                                if(!Main.scenarios.contains("netheribus"))
                                    this.cancel();

                                for(Player all : Bukkit.getOnlinePlayers()) {

                                    if(all.getWorld() != Bukkit.getWorld("world_nether")) {

                                        all.sendMessage(Main.prefix + prefix + "§bHurry up!§f You §dneed§f to go to the Nether.");
                                        all.damage(7.5);
                                        all.getWorld().playEffect(all.getLocation(), Effect.SMOKE, 1, 2);
                                    }
                                }
                            }
                        }.runTaskTimer(Main.getInstance(), 0, seconds * 20);

                    },
                    (t) -> {
                        if(t.getSecondsLeft() <= 5) {

                            Bukkit.broadcastMessage(Main.prefix + prefix + "§cThe §bNetheribus§c is starting in §b" + t.getSecondsLeft() + "§c seconds!");

                            for(Player all : Bukkit.getOnlinePlayers())
                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);
                        }
                    }
            );
            nether.scheduleTimer();
        }
    }

}
