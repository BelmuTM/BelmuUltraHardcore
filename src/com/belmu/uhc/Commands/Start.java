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
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Map;

public class Start implements CommandExecutor {

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("start")) {

                if (player.isOp()) {

                    if (args.length == 0) {
                        FileConfiguration cfg = Main.getInstance().getConfig();

                        if(cfg.get("Host") != null) {

                            if(cfg.get("Host").equals(player.getName())) {

                                if (Main.game.contains("running") || Main.start.contains("preparing")) {
                                    player.sendMessage(Main.prefix + "§cGame has already started.");

                                } else {
                                    World world = Bukkit.getWorld("world");
                                    World nether = Bukkit.getWorld("world_nether");

                                    String victory = "§6VI§eC§fT§eO§6RY";
                                    IChatBaseComponent chatVictory = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + victory + "\",color:" + ChatColor.YELLOW.name().toLowerCase() + "}");

                                    PacketPlayOutTitle titleVictory = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatVictory);
                                    PacketPlayOutTitle length = new PacketPlayOutTitle(0, 20, 0);

                                    String defeat = "§4DE§cFE§4AT";
                                    IChatBaseComponent chatDefeat = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + defeat + "\",color:" + ChatColor.YELLOW.name().toLowerCase() + "}");

                                    PacketPlayOutTitle titleDefeat = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatDefeat);

                                    String bar_1 = "§8§m                                                       \n";
                                    String bar_2 = "\n§8§m                                                       ";
                                    int time = 60;

                                    String kickallMessage = Main.prefix + "§cKicking all the players in§b " + (time / 60) + " minutes§c!";
                                    String thxForPlaying = "§7§m                              " + "\n§cThanks for Playing!\n" + "§7§m                              ";
                                    String sortError = "§cError: Failed to sort kills.";

                                    if(Main.getMode().equalsIgnoreCase("Solo")) {

                                        new BukkitRunnable() {

                                            @Override
                                            public void run() {

                                                if (Main.players.size() == 1 && Main.online.size() > 1) {

                                                    if(Main.game.contains("running")) {
                                                        this.cancel();

                                                        CountdownWithDouble gtimer = new CountdownWithDouble(Main.getInstance(),
                                                                0.25,
                                                                () -> {
                                                                },
                                                                () -> {

                                                                    StringBuilder a = new StringBuilder();

                                                                    for(int i = 0; i < 4; i++) {

                                                                        try {

                                                                            Map<Player, Integer> sortedKills = UsefulMethods.sortedKills();

                                                                            Player p = (Player) sortedKills.keySet().toArray()[i];
                                                                            int kills = sortedKills.get(i);

                                                                            a.append("§7» " + p.getName() + "§7: §c" + kills + "§f kills" + "\n");

                                                                        } catch (ArrayIndexOutOfBoundsException aioobe) {
                                                                            Bukkit.broadcastMessage(Main.prefix + sortError);
                                                                        }
                                                                    }

                                                                    Player winner = Bukkit.getPlayer(Main.players.get(0));

                                                                    ((CraftPlayer) winner).getHandle().playerConnection.sendPacket(titleVictory);
                                                                    ((CraftPlayer) winner).getHandle().playerConnection.sendPacket(length);

                                                                    Bukkit.broadcastMessage(bar_1 + "§c§l                UHC Solo" + "\n" + "§7            Thanks for playing!" + "\n " + "\n " + "          §cWinner :§7 " + winner + a.toString().trim() + bar_2);

                                                                    for (Player all : Bukkit.getOnlinePlayers())
                                                                        all.playSound(all.getLocation(), Sound.ENDERDRAGON_GROWL, 1, Integer.MAX_VALUE);

                                                                    CountdownWithDouble kickall = new CountdownWithDouble(Main.getInstance(),
                                                                            time,
                                                                            () -> Bukkit.broadcastMessage(kickallMessage),

                                                                            () -> {
                                                                                UsefulMethods.kickAll(thxForPlaying);

                                                                                EasyCountdown shutdown = new EasyCountdown(Main.getInstance(),
                                                                                        2.30D,
                                                                                        UsefulMethods::deleteWorlds

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

                                            }
                                        }.runTaskTimer(Main.getInstance(), 0, 100);

                                    } else if(Main.getMode().equalsIgnoreCase("Teams")) {

                                        new BukkitRunnable() {

                                            @Override
                                            public void run() {

                                                if (Teams.inGameTeams.size() == 1 && Main.online.size() > 1) {

                                                    if(Main.game.contains("running")) {

                                                        if(Teams.teamsAtStart.size() != Teams.inGameTeams.size()) {
                                                            this.cancel();

                                                            CountdownWithDouble gtimer = new CountdownWithDouble(Main.getInstance(),
                                                                    0.25,
                                                                    () -> {
                                                                    },
                                                                    () -> {
                                                                        StringBuilder a = new StringBuilder();

                                                                        for (int i = 0; i < 4; i++) {
                                                                            try {
                                                                                Map<Player, Integer> sortedKills = UsefulMethods.sortedKills();

                                                                                Player p = (Player) sortedKills.keySet().toArray()[i];
                                                                                int kills = sortedKills.get(i);

                                                                                a.append("§7» " + p.getName() + "§7: §c" + kills + "§f kills" + "\n");

                                                                            } catch (ArrayIndexOutOfBoundsException aioobe) {
                                                                                Bukkit.broadcastMessage(Main.prefix + sortError);
                                                                            }
                                                                        }
                                                                        Team lastTeam = Teams.inGameTeams.get(0);

                                                                        for(OfflinePlayer winners : lastTeam.getPlayers()) {

                                                                            ((CraftPlayer) winners).getHandle().playerConnection.sendPacket(titleDefeat);
                                                                            ((CraftPlayer) winners).getHandle().playerConnection.sendPacket(length);
                                                                        }

                                                                        Bukkit.broadcastMessage(bar_1 + "§c§l               UHC Teams" + "\n" + "§7            Thanks for playing!" + "\n " + "          §cWinner : "
                                                                                + lastTeam.getPrefix() + lastTeam.getDisplayName() + "§7 Team" + a.toString().trim() + bar_2);

                                                                        for (Player all : Bukkit.getOnlinePlayers())
                                                                            all.playSound(all.getLocation(), Sound.ENDERDRAGON_GROWL, 1, Integer.MAX_VALUE);

                                                                        CountdownWithDouble kickall = new CountdownWithDouble(Main.getInstance(),
                                                                                time,
                                                                                () -> Bukkit.broadcastMessage(kickallMessage),

                                                                                () -> {
                                                                                    UsefulMethods.kickAll(thxForPlaying);

                                                                                    EasyCountdown shutdown = new EasyCountdown(Main.getInstance(),
                                                                                            2.30D,
                                                                                                UsefulMethods::deleteWorlds

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
                                                }
                                            }
                                        }.runTaskTimer(Main.getInstance(), 0, 100);

                                    }
                                    Main.start.add("preparing");

                                    double preparationTime = Bukkit.getOnlinePlayers().size() * 1.7D;

                                    CountdownWithDouble untilStart = new CountdownWithDouble(Main.getInstance(),
                                            Options.untilGameStarts,

                                            () -> {
                                                Bukkit.broadcastMessage(Main.prefix + "§7Teleporting players in§c " + (Options.untilGameStarts / 60) + "§7 minutes!");

                                                if(Main.getMode().equalsIgnoreCase("Teams")) {

                                                    if (Main.getTeamPicking().equalsIgnoreCase("Normal")) {

                                                        ItemStack ch = new ItemStack(Material.MELON, 1);
                                                        ItemMeta chM = ch.getItemMeta();

                                                        chM.setDisplayName("§fChoose Team§7 (Right Click)");
                                                        ch.setItemMeta(chM);

                                                        for (Player online : Bukkit.getOnlinePlayers()) {

                                                            Teams.playersToSpread.add(online.getUniqueId());
                                                            online.getInventory().setItem(0, ch);
                                                        }

                                                        Main.startTeams = true;
                                                    }
                                                    Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();

                                                    for(Team team : s.getTeams())
                                                        try {
                                                            team.getPlayers().clear();

                                                        } catch(Exception e) {
                                                            Bukkit.broadcastMessage(Main.prefix + "§cError: Failed to clear players from teams.");
                                                        }
                                                }

                                                for (Player online : Bukkit.getOnlinePlayers())
                                                    online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);

                                            },

                                            () -> {

                                                CountdownWithDouble prep = new CountdownWithDouble(Main.getInstance(),
                                                        preparationTime,

                                                        () -> {

                                                            for(Player all : Bukkit.getOnlinePlayers())
                                                                UsefulMethods.sendPacket(all, "§a§oGenerating chunks... §7§o(Estimated time: " + preparationTime + "s)");

                                                            if(Main.getMode().equalsIgnoreCase("Teams")) {

                                                                if (Main.getTeamPicking().equalsIgnoreCase("Random")) {

                                                                    for (Player online : Bukkit.getOnlinePlayers())
                                                                        Teams.addPlayersToTeams(online);

                                                                } else {
                                                                    Main.startTeams = false;

                                                                    if (!Teams.playersToSpread.isEmpty()) {

                                                                        for (int i = 0; i < Teams.playersToSpread.size(); i++) {

                                                                            Player toSpread = Bukkit.getPlayer(Teams.playersToSpread.get(i));
                                                                            Teams.addPlayersToTeams(toSpread);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            Main.game.add("running");
                                                            Main.start.clear();
                                                            Main.preparation = true;

                                                            EasyCountdown oof = new EasyCountdown(Main.getInstance(),
                                                                    1.0D,
                                                                    UsefulMethods::prepareTp
                                                            );
                                                            oof.scheduleTimer();

                                                            for (Player all : Bukkit.getOnlinePlayers()) {

                                                                Main.players.add(all.getName());
                                                                all.playSound(all.getLocation(), Sound.CHICKEN_EGG_POP, 1, Integer.MAX_VALUE);
                                                            }

                                                        },

                                                        () -> {
                                                            Main.preparation = false;

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
                                                                                        Bukkit.broadcastMessage(Main.prefix + "§cError: Troubles occurred when loading scoreboard...");
                                                                                    }

                                                                                }
                                                                            }.runTaskTimer(Main.getInstance(), 0, 10);

                                                                            DiamondLimit.dLimit.put(all.getUniqueId(), 0);

                                                                            GameScoreboard.initializeHealth(all);
                                                                            all.setHealth(all.getHealth());
                                                                        }
                                                                        Netheribus.startNetheribus();
                                                                        FinalHeal.finalHeal(Options.finalHealSeconds);

                                                                        GameScoreboard.startScoreboardTimer();

                                                                        if(Main.scenarios.contains("paranoia"))
                                                                            Bukkit.broadcastMessage(Main.prefix + "§2Tip :§a Use /h to hide the§c Paranoïa§a alerts.");

                                                                        if(Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams"))
                                                                            Bukkit.broadcastMessage(Main.prefix + "§2Tip :§a Type ! before your message to talk in the global chat.");

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

                                                                                    if(!world.getPVP() && !nether.getPVP()) {

                                                                                        for (Player all : Bukkit.getOnlinePlayers()) {

                                                                                            all.playSound(all.getLocation(), Sound.SUCCESSFUL_HIT, 1, Integer.MAX_VALUE);
                                                                                            all.sendMessage(Main.prefix + "§bGrace period§c ended!");
                                                                                        }
                                                                                        world.setPVP(true);
                                                                                        nether.setPVP(true);
                                                                                    }

                                                                                },
                                                                                (t) -> {
                                                                                    int s = t.getSecondsLeft();

                                                                                    if(!world.getPVP() && !nether.getPVP()) {

                                                                                        if (s == 600 || s == 300 || s == 60) {

                                                                                            Bukkit.broadcastMessage(Main.prefix + "§cPvP will be activated in §7" + s / 60 + " minutes");

                                                                                            for (Player all : Bukkit.getOnlinePlayers())
                                                                                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);

                                                                                        } else if (s <= 5) {

                                                                                            Bukkit.broadcastMessage(Main.prefix + "§cPvP will be activated in §7" + s + " seconds");

                                                                                            for (Player all : Bukkit.getOnlinePlayers())
                                                                                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);

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

                                                            if(t.getSecondsLeft() < 5)
                                                                Bukkit.broadcastMessage(Main.prefix + "§7 Teleporting players in§c " + Math.round(t.getSecondsLeft()) + "§7s!");

                                                        }
                                                );
                                                prep.scheduleTimer();

                                            },

                                            (t) -> {

                                                if(t.getSecondsLeft() == 60)  {

                                                    Bukkit.broadcastMessage(Main.prefix + "§7Teleporting players in§c " + ((int) t.getSecondsLeft() / 60) + "§7 minutes!");

                                                    for (Player online : Bukkit.getOnlinePlayers())
                                                        online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);
                                                }
                                            }
                                    );
                                    untilStart.scheduleTimer();
                                }
                            } else
                                player.sendMessage(Main.prefix + "§cYou must be host to do that.");
                        } else
                            player.sendMessage(Main.prefix + "§cPlease define host before doing that.");
                    } else
                        player.sendMessage(Main.prefix + "§cWrong usage. Try /start");
                } else
                    player.sendMessage(Main.prefix + "§cYou must be operator to do that.");
            }
        }
        return false;
    }

}
