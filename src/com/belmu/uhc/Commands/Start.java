package com.belmu.uhc.Commands;

import com.belmu.uhc.Main;
import com.belmu.uhc.Scenarios.DiamondLimit;
import com.belmu.uhc.Scenarios.FinalHeal;
import com.belmu.uhc.Scenarios.Netheribus;
import com.belmu.uhc.Scoreboard.GameScoreboard;
import com.belmu.uhc.Teams.Teams;
import com.belmu.uhc.Utils.*;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class Start implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("start")) {

                if (player.isOp()) {

                    if (args.length == 0) {
                        FileConfiguration cfg = Main.getInstance().getConfig();

                        if(cfg.get("Host") != null) {

                            if(cfg.get("Host").equals(player.getName())) {

                                if (Main.partie.contains("lancée")) {

                                    player.sendMessage(Main.prefix + "§cGame has already started.");

                                } else if (Main.début.contains("willlancée")) {

                                    player.sendMessage(Main.prefix + "§cGame has already started.");

                                } else {

                                    World world = Bukkit.getWorld("world");
                                    World nether = Bukkit.getWorld("world_nether");

                                    if(!Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                                        new BukkitRunnable() {

                                            @Override
                                            public void run() {

                                                if (Main.joueurs.size() == 1 && Main.online.size() > 1) {

                                                    this.cancel();

                                                    CountdownWithDouble gtimer = new CountdownWithDouble(Main.getInstance(),
                                                            0.25,
                                                            () -> {
                                                            },
                                                            () -> {

                                                                int kills = cfg.getInt("Players" + "." + Main.joueurs.get(0) + "." + "kills");
                                                                String bar_1 = "§8§m                                                       \n";
                                                                String bar_2 = "\n§8§m                                                       ";

                                                                Bukkit.broadcastMessage(bar_1 + "§c§l                UHC Solo" + "\n" + "§7            Thanks for playing!" + "\n " + "\n " + "          §cWinner :§7 " + Main.joueurs.get(0) + " §b§o(" + kills + " kills)" + bar_2);

                                                                for (Player all : Bukkit.getOnlinePlayers()) {

                                                                    all.playSound(all.getLocation(), Sound.ENDERDRAGON_GROWL, 1, Integer.MAX_VALUE);

                                                                }

                                                                CountdownWithDouble kickall = new CountdownWithDouble(Main.getInstance(),
                                                                        60,
                                                                        () -> Bukkit.broadcastMessage(Main.prefix + "§cKicking all the players in§b 1 minute§c!"),

                                                                        () -> {

                                                                            for (Player online : Bukkit.getOnlinePlayers()) {

                                                                                UsefulMethods.sendToServer(Main.getInstance(), online, "Lobby");

                                                                            }

                                                                            EasyCountdown shutdown = new EasyCountdown(Main.getInstance(),
                                                                                    2.30D,
                                                                                    () -> {
                                                                                        Bukkit.unloadWorld("world", false);
                                                                                        Bukkit.unloadWorld("world_nether", false);

                                                                                        try {

                                                                                            FileUtils.deleteDirectory(new File(world.getWorldFolder().getPath()));
                                                                                            FileUtils.deleteDirectory(new File(nether.getWorldFolder().getPath()));

                                                                                        } catch (IOException exc) {

                                                                                            exc.printStackTrace();

                                                                                        }

                                                                                        Bukkit.getServer().shutdown();
                                                                                    }
                                                                            );
                                                                            shutdown.scheduleTimer();

                                                                        },
                                                                        (t) -> {
                                                                        }
                                                                );
                                                                kickall.scheduleTimer();

                                                            },
                                                            (t) -> {
                                                            }
                                                    );
                                                    gtimer.scheduleTimer();
                                                }

                                            }
                                        }.runTaskTimer(Main.getInstance(), 0, 100);

                                    } else if(Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                                        new BukkitRunnable() {

                                            @Override
                                            public void run() {

                                                if (Teams.inGameTeams.size() == 1 && Main.online.size() > 1) {

                                                    this.cancel();

                                                    CountdownWithDouble gtimer = new CountdownWithDouble(Main.getInstance(),
                                                            0.25,
                                                            () -> {
                                                            },
                                                            () -> {

                                                                String bar_1 = "§8§m                                                       \n";
                                                                String bar_2 = "\n§8§m                                                       ";



                                                                Bukkit.broadcastMessage(bar_1 + "§c§l               UHC Teams" + "\n" + "§7            Thanks for playing!" + "\n " + "\n " + "          §cWinner : " + Teams.inGameTeams.get(0).getPrefix() + Teams.inGameTeams.get(0).getDisplayName() + "§7 Team" + bar_2);

                                                                for (Player all : Bukkit.getOnlinePlayers()) {

                                                                    all.playSound(all.getLocation(), Sound.ENDERDRAGON_GROWL, 1, Integer.MAX_VALUE);

                                                                }

                                                                CountdownWithDouble kickall = new CountdownWithDouble(Main.getInstance(),
                                                                        60,
                                                                        () -> Bukkit.broadcastMessage(Main.prefix + "§cKicking all the players in§b 1 minute§c!"),

                                                                        () -> {

                                                                            for (Player online : Bukkit.getOnlinePlayers()) {

                                                                                UsefulMethods.sendToServer(Main.getInstance(), online, "Lobby");

                                                                            }

                                                                            EasyCountdown shutdown = new EasyCountdown(Main.getInstance(),
                                                                                    2.30D,
                                                                                    () -> {
                                                                                        Bukkit.unloadWorld("world", false);
                                                                                        Bukkit.unloadWorld("world_nether", false);

                                                                                        try {

                                                                                            FileUtils.deleteDirectory(new File(world.getWorldFolder().getPath()));
                                                                                            FileUtils.deleteDirectory(new File(nether.getWorldFolder().getPath()));

                                                                                        } catch (IOException exc) {

                                                                                            exc.printStackTrace();

                                                                                        }

                                                                                        Bukkit.getServer().shutdown();
                                                                                    }
                                                                            );
                                                                            shutdown.scheduleTimer();

                                                                        },
                                                                        (t) -> {
                                                                        }
                                                                );
                                                                kickall.scheduleTimer();

                                                            },
                                                            (t) -> {
                                                            }
                                                    );
                                                    gtimer.scheduleTimer();
                                                }

                                            }
                                        }.runTaskTimer(Main.getInstance(), 0, 100);

                                    }

                                    Main.début.add("willlancée");

                                    double preparationTime = Bukkit.getOnlinePlayers().size() * 1.3D;

                                    CountdownWithDouble untilStart = new CountdownWithDouble(Main.getInstance(),
                                            Options.untilGameStarts,

                                            () -> {

                                                Bukkit.broadcastMessage(Main.prefix + "§7Teleporting players in§c " + (Options.untilGameStarts / 60) + "§7 minutes!");

                                                if(Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                                                    if (Main.getInstance().getConfig().get("UHC" + "." + "TeamPicking").equals("Normal")) {

                                                        ItemStack ch = new ItemStack(Material.MELON, 1);
                                                        ItemMeta chM = ch.getItemMeta();

                                                        chM.setDisplayName("§fChoose Team§7 (Right Click)");
                                                        ch.setItemMeta(chM);

                                                        for (Player online : Bukkit.getOnlinePlayers()) {

                                                            Teams.playersToSpread.add(online.getUniqueId());
                                                            online.getInventory().setItem(0, ch);

                                                        }

                                                        Main.startTeams.add(true);

                                                    }

                                                }

                                                for (Player online : Bukkit.getOnlinePlayers()) {

                                                    online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);

                                                }

                                            },

                                            () -> {

                                                CountdownWithDouble prep = new CountdownWithDouble(Main.getInstance(),

                                                        preparationTime,

                                                        () -> {

                                                            for(Player all : Bukkit.getOnlinePlayers()) {

                                                                UsefulMethods.sendPacket(all, "§a§oGenerating chunks... §7§o(Estimated time: " + preparationTime + "s)");

                                                            }

                                                            if(Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                                                                if (Main.getInstance().getConfig().get("UHC" + "." + "TeamPicking").equals("Random")) {

                                                                    for (Player online : Bukkit.getOnlinePlayers()) {

                                                                        Teams.addPlayersToTeams(online);

                                                                    }

                                                                } else {

                                                                    Main.startTeams.clear();
                                                                    Main.startTeams.add(false);

                                                                    for (int i = 0; i < Teams.playersToSpread.size(); i++) {

                                                                        Player toSpread = Bukkit.getPlayer(Teams.playersToSpread.get(i));

                                                                        Teams.addPlayersToTeams(toSpread);
                                                                    }

                                                                }

                                                            }

                                                            Main.partie.add("lancée");
                                                            Main.début.clear();
                                                            Main.preparation.add(true);

                                                            EasyCountdown oof = new EasyCountdown(Main.getInstance(),
                                                                    1.5D,

                                                                    UsefulMethods::prepareTp

                                                            );
                                                            oof.scheduleTimer();

                                                            for (Player all : Bukkit.getOnlinePlayers()) {

                                                                all.playSound(all.getLocation(), Sound.CHICKEN_EGG_POP, 1, Integer.MAX_VALUE);

                                                            }

                                                        },

                                                        () -> {
                                                            Main.preparation.remove(true);
                                                            Main.preparation.add(false);

                                                            CountdownWithInt gtimer = new CountdownWithInt(Main.getInstance(),
                                                                    Main.timer,

                                                                    () -> {

                                                                        Location loc3 = new Location(world, Main.platformSize, Main.height + 1, -Main.platformSize);
                                                                        Location loc4 = new Location(world, -Main.platformSize, Main.height + 1, Main.platformSize);

                                                                        UsefulMethods.setBlocksRegion(loc3, loc4, Material.AIR);

                                                                        Location loc1 = new Location(world, Main.platformSize, Main.height, -Main.platformSize);
                                                                        Location loc2 = new Location(world, -Main.platformSize, Main.height, Main.platformSize);

                                                                        UsefulMethods.setBlocksRegion(loc1, loc2, Material.AIR);


                                                                        UsefulMethods.tp();
                                                                        UsefulMethods.start();

                                                                    },


                                                                    () -> {
                                                                        for (Player all : Bukkit.getOnlinePlayers()) {

                                                                            Main.joueurs.add(all.getName());
                                                                            all.getInventory().clear();
                                                                            all.setGameMode(GameMode.SURVIVAL);

                                                                        }

                                                                        world.setTime(0);
                                                                        world.getWorldBorder().setSize(Options.borderScale);
                                                                        world.getWorldBorder().setCenter(0, 0);
                                                                        world.getWorldBorder().setWarningDistance(25);

                                                                        for (Player all : Bukkit.getOnlinePlayers()) {

                                                                            all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);
                                                                            all.sendMessage(Main.prefix + "§aGood luck!");

                                                                            GameScoreboard.createGameScoreboard(all);

                                                                            new BukkitRunnable() {

                                                                                @Override
                                                                                public void run() {

                                                                                    try {

                                                                                        GameScoreboard.updateGameScoreboard(all);

                                                                                    } catch (NullPointerException exc) {

                                                                                        System.out.println("Having troubles with Scoreboard...");

                                                                                    }

                                                                                }
                                                                            }.runTaskTimer(Main.getInstance(), 0, 5);

                                                                            DiamondLimit.dLimit.put(all.getUniqueId(), 0);

                                                                            GameScoreboard.initializeHealth(all);

                                                                            all.setHealth(all.getHealth());

                                                                        }

                                                                        Netheribus.startNetheribus();
                                                                        FinalHeal.finalHeal(Options.finalHealSeconds);

                                                                        GameScoreboard.startScoreboardTimer();

                                                                        if(Main.scenarios.contains("paranoia")) {

                                                                            Bukkit.broadcastMessage(Main.prefix + "§2Tip :§a Use /h to hide the§c Paranoïa§a alerts.");

                                                                        }

                                                                        if(Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                                                                            Bukkit.broadcastMessage(Main.prefix + "§2Tip :§a Type ! before your message to talk in the global chat.");

                                                                        }

                                                                        CountdownWithInt monsters = new CountdownWithInt(Main.getInstance(),
                                                                                Options.monstersSpawn,
                                                                                () -> Bukkit.broadcastMessage(Main.prefix + "§cMonsters§f will spawn in§7 " + (Options.monstersSpawn / 60) + " minutes§c."),

                                                                                () -> {

                                                                                    world.setMonsterSpawnLimit(70);
                                                                                    Bukkit.broadcastMessage(Main.prefix + "§cMonsters§f will now §7spawn§f.");

                                                                                },
                                                                                (t) -> {

                                                                                }
                                                                        );
                                                                        monsters.scheduleTimer();

                                                                        CountdownWithInt border = new CountdownWithInt(Main.getInstance(),
                                                                                Options.borderSeconds,
                                                                                () -> {
                                                                                },
                                                                                () -> {

                                                                                    world.getWorldBorder().setSize(250, Options.borderShrinkingSeconds);
                                                                                    world.getWorldBorder().setCenter(0, 0);
                                                                                    world.getWorldBorder().setWarningDistance(25);

                                                                                    for (Player all : Bukkit.getOnlinePlayers()) {

                                                                                        all.playSound(all.getLocation(), Sound.ENDERDRAGON_GROWL, 1, Integer.MAX_VALUE);
                                                                                        all.sendMessage(Main.prefix + "§bBorder§c is shrinking!");

                                                                                    }

                                                                                    Main.border.add("yes");

                                                                                },
                                                                                (t) -> {

                                                                                }
                                                                        );
                                                                        border.scheduleTimer();

                                                                        CountdownWithInt pvp = new CountdownWithInt(Main.getInstance(),

                                                                                Options.pvpSeconds,
                                                                                () -> {
                                                                                },
                                                                                () -> {

                                                                                    if(!Bukkit.getWorld("world").getPVP() && !Bukkit.getWorld("world_nether").getPVP()) {

                                                                                        for (Player all : Bukkit.getOnlinePlayers()) {

                                                                                            all.playSound(all.getLocation(), Sound.SUCCESSFUL_HIT, 1, Integer.MAX_VALUE);
                                                                                            all.sendMessage(Main.prefix + "§bGrace period§c ended!");

                                                                                        }

                                                                                        Bukkit.getWorld("world").setPVP(true);
                                                                                        Bukkit.getWorld("world_nether").setPVP(true);

                                                                                    }

                                                                                },
                                                                                (t) -> {

                                                                                    int s = t.getSecondsLeft();

                                                                                    if(!Bukkit.getWorld("world").getPVP() && !Bukkit.getWorld("world_nether").getPVP() && !Bukkit.getWorld("world_the_end").getPVP()) {

                                                                                        if (s == 600 || s == 300 || s == 60) {

                                                                                            Bukkit.broadcastMessage(Main.prefix + "§cPvP will be activated in §7" + s / 60 + " minutes");

                                                                                            for (Player all : Bukkit.getOnlinePlayers()) {

                                                                                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);

                                                                                            }

                                                                                        } else if (s == 5 || s == 4 || s == 3 || s == 2 || s == 1) {

                                                                                            Bukkit.broadcastMessage(Main.prefix + "§cPvP will be activated in §7" + s + " seconds");

                                                                                            for (Player all : Bukkit.getOnlinePlayers()) {

                                                                                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);

                                                                                            }

                                                                                        }

                                                                                    }

                                                                                }
                                                                        );
                                                                        pvp.scheduleTimer();

                                                                    },
                                                                    (t) -> {

                                                                        for (Player all : Bukkit.getOnlinePlayers()) {

                                                                            String sec = "§8» §c" + t.getSecondsLeft() + "s§8 «";
                                                                            String text = "§4Start";
                                                                            IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\",color:" + ChatColor.YELLOW.name().toLowerCase() + "}");

                                                                            PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                                                                            PacketPlayOutTitle length = new PacketPlayOutTitle(0, 20, 0);


                                                                            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(title);
                                                                            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(length);

                                                                            IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + sec + "\",color:" + ChatColor.GREEN.name().toLowerCase() + "}");

                                                                            PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);

                                                                            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(subtitle);
                                                                            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(length);

                                                                            all.playSound(all.getLocation(), Sound.CLICK, 1, Integer.MAX_VALUE);

                                                                            all.sendMessage(Main.prefix + "§7Game starts in§c " + t.getSecondsLeft() + "s");

                                                                        }
                                                                    }
                                                            );
                                                            gtimer.scheduleTimer();

                                                        },
                                                        (t) -> {

                                                            if(t.getSecondsLeft() < 10) {

                                                                Bukkit.broadcastMessage(Main.prefix + "§7 Teleporting players in§cb " + t.getSecondsLeft() + "§7s!");

                                                            }

                                                        }
                                                );
                                                prep.scheduleTimer();

                                            },


                                            (t) -> {


                                            }

                                    );
                                    untilStart.scheduleTimer();

                                }

                            } else {

                                player.sendMessage(Main.prefix + "§cYou must be host to do that.");

                            }

                        } else {

                            player.sendMessage(Main.prefix + "§cPlease define host before doing that.");

                        }

                    } else {

                        player.sendMessage(Main.prefix + "§cWrong usage. Try /start");

                    }

                } else {

                    player.sendMessage(Main.prefix + "§cYou must be operator to do that.");

                }

            }

        }

        return false;
    }

}