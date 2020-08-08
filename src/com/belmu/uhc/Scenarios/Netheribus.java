package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Main;
import com.belmu.uhc.Utils.CountdownWithInt;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Netheribus implements Listener {

    private static String prefix = "§7[§4Netheribus§7] ";
    private static int amplifier = 1;

    private static List<String> netheribus = new ArrayList<>();

    public static void startNetheribus() {
        if(Main.scenarios.contains("netheribus")) {
            CountdownWithInt nether = new CountdownWithInt(Main.getInstance(),
                    10,
                    () -> {
                    },
                    () -> {
                        Bukkit.broadcastMessage(Main.prefix + prefix + "§cThe§b Netheribus§c started! Being in the Overworld will now give you damage.");
                        netheribus.add("on");

                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if(all.getWorld().equals(Bukkit.getWorld("world"))) {
                                all.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, Netheribus.amplifier));
                            }
                            all.playSound(all.getLocation(), Sound.LEVEL_UP, 1, Integer.MAX_VALUE);
                        }
                    },
                    (t) -> {
                        if (t.getSecondsLeft() == 5) {
                            Bukkit.broadcastMessage(Main.prefix + prefix + "§cThe §bNetheribus§c is starting in §b5§c seconds!");
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);
                            }
                        } else if (t.getSecondsLeft() == 4) {
                            Bukkit.broadcastMessage(Main.prefix + prefix + "§cThe §bNetheribus§c is starting in §b4§c seconds!");
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);
                            }
                        } else if (t.getSecondsLeft() == 3) {
                            Bukkit.broadcastMessage(Main.prefix + prefix + "§cThe §bNetheribus§c is starting in §b3§c seconds!");
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);
                            }
                        } else if (t.getSecondsLeft() == 2) {
                            Bukkit.broadcastMessage(Main.prefix + prefix + "§cThe §bNetheribus§c is starting in §b2§c seconds!");
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);
                            }
                        } else if (t.getSecondsLeft() == 1) {
                            Bukkit.broadcastMessage(Main.prefix + prefix + "§cThe §bNetheribus§c is starting in §b1§c second!");
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);
                            }
                        }
                    }
            );
            nether.scheduleTimer();
        }
    }

    @EventHandler
    public void onChange(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();

        if(Main.scenarios.contains("netheribus")) {
            if (netheribus.contains("on")) {
                if (player.getWorld().equals(Bukkit.getWorld("world_nether"))) {
                    if (player.hasPotionEffect(PotionEffectType.WITHER)) {
                        player.removePotionEffect(PotionEffectType.WITHER);
                    }
                }

                if (player.getWorld().equals(Bukkit.getWorld("world")) || player.getWorld().equals(Bukkit.getWorld("world_the_end"))) {
                    if (!player.hasPotionEffect(PotionEffectType.WITHER)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, amplifier));
                    }
                }
            }
        }
    }
}
