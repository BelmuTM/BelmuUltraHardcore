package com.belmu.uhc.Scenarios;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Utility.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class Netheribus implements Listener {

    public final UHC plugin;
    public Netheribus(UHC plugin) {
        this.plugin = plugin;
    }

    private static final String prefix = "§7[§4Netheribus§7] ";
    private static final int seconds = 30;

    public void execute() {

        if(plugin.scenarios.contains("netheribus")) {

            Countdown nether = new Countdown(plugin,
                    10,
                    () -> {
                    },
                    () -> {
                        Bukkit.broadcastMessage(plugin.prefix + prefix + "§cThe§b Netheribus§c started! Staying in the Overworld will now deal you damage.");

                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                if(!plugin.scenarios.contains("netheribus"))
                                    this.cancel();

                                for(Player all : Bukkit.getOnlinePlayers()) {

                                    if(all.getWorld() != plugin.nether) {

                                        all.sendMessage(plugin.prefix + prefix + "§bHurry up!§f You §dneed§f to stay in the Nether.");
                                        all.damage(7.5);
                                        all.getWorld().playEffect(all.getLocation(), Effect.SMOKE, 1, 2);
                                    }
                                }
                            }
                        }.runTaskTimer(plugin, 0, (long) seconds * 20);

                    },
                    (t) -> {
                        if(t.getSecondsLeft() <= 5) {

                            Bukkit.broadcastMessage(plugin.prefix + prefix + "§cThe §bNetheribus§c is starting in §b" + t.getSecondsLeft() + "§c seconds!");

                            for(Player all : Bukkit.getOnlinePlayers())
                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);
                        }
                    }
            );
            nether.scheduleTimer();
        }
    }

}
