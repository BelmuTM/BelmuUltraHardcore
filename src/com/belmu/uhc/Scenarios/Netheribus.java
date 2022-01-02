package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Core.Options;
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

    private static final String prefix = "§7[§dNetheribus§7] ";

    public void execute() {

        if(plugin.scenarios.contains("netheribus")) {

            Countdown nether = new Countdown(plugin,
                    Options.netheribusMinutes,
                    () -> {
                    },
                    () -> {
                        Bukkit.broadcastMessage(plugin.prefix + prefix + "§6§lNetheribus §r§eis now active! §cStaying in the Overworld will deal you damage");

                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                if(!plugin.scenarios.contains("netheribus"))
                                    this.cancel();

                                for(Player all : Bukkit.getOnlinePlayers()) {

                                    if(all.getWorld() != plugin.nether) {

                                        all.sendMessage(plugin.prefix + prefix + "§eHurry up!§7 You to go in the Nether");
                                        all.damage(7.5);
                                        all.getWorld().playEffect(all.getLocation(), Effect.SMOKE, 1, 2);
                                    }
                                }
                            }
                        }.runTaskTimer(plugin, 0, (long) Options.netheribusDmgTime * 20);

                    },
                    (t) -> {
                        if(t.getSecondsLeft() <= 5) {

                            Bukkit.broadcastMessage(plugin.prefix + "§dNetheribus§7 starts in §b" + t.getSecondsLeft() + "§7 seconds!");

                            for(Player all : Bukkit.getOnlinePlayers())
                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);
                        }
                    }
            );
            nether.scheduleTimer();
        }
    }

}
