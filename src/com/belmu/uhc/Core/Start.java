package com.belmu.uhc.Core;

import com.belmu.uhc.Scenarios.CatEyes;
import com.belmu.uhc.UHC;
import com.belmu.uhc.Scenarios.DiamondLimit;
import com.belmu.uhc.Scenarios.FinalHeal;
import com.belmu.uhc.Scenarios.Netheribus;
import com.belmu.uhc.Utility.*;
import fr.minuskube.netherboard.Netherboard;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class Start implements CommandExecutor {

    public final UHC plugin;
    public Start(UHC plugin) {
        this.plugin = plugin;
    }

    public double untilStartSec;
    public double untilStartCt;

    public boolean preparing;
    public boolean running;
    public boolean teleported;
    public boolean fell;
    public boolean border;

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("start")) {
                if(player.isOp()) {

                    if(args.length == 0) {
                        FileConfiguration cfg = plugin.getConfig();

                        if(cfg.get("Host") != null) {
                            if(cfg.get("Host").equals(player.getName())) {

                                if(running || preparing) {
                                    player.sendMessage(plugin.prefix + "§cGame has already started.");
                                } else {
                                    World world = Bukkit.getWorld("world");
                                    World nether = Bukkit.getWorld("world_nether");

                                    /*
                                    WIN CHECK
                                     */
                                    new BukkitRunnable() {

                                        @Override
                                        public void run() {

                                            if(running) {
                                                plugin.common.gameChecks();

                                                if(plugin.getMode() == 0) {
                                                    if(plugin.players.size() == 1 && Bukkit.getOnlinePlayers().size() > 1) {

                                                        Player winner = Bukkit.getPlayer(plugin.players.get(0));

                                                        if(plugin.game.running) {
                                                            this.cancel();

                                                            for (UUID uuid : plugin.players) {
                                                                Player player = Bukkit.getPlayer(uuid);
                                                                plugin.title.winTitle(player, player == winner);
                                                            }

                                                            plugin.common.sendWinMessage(winner.getName());
                                                            endGame();
                                                        }
                                                    }
                                                } else if (plugin.getMode() == 1) {

                                                    if(plugin.teams.inGameTeams.size() == 1 && Bukkit.getOnlinePlayers().size() > 1) {
                                                        if(plugin.game.running) {

                                                            if(plugin.teams.teamsAtStart.size() != plugin.teams.inGameTeams.size()) {
                                                                this.cancel();

                                                                Team lastTeam = plugin.teams.inGameTeams.get(0);

                                                                for (UUID uuid : plugin.players) {
                                                                    Player player = Bukkit.getPlayer(uuid);
                                                                    for (OfflinePlayer winners : lastTeam.getPlayers())
                                                                        plugin.title.winTitle(player, player == winners);
                                                                }

                                                                plugin.common.sendWinMessage(lastTeam.getPrefix() + " " + lastTeam.getName());
                                                                endGame();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }.runTaskTimer(plugin, 40, 40);

                                    preparing = true;
                                    int a = Bukkit.getOnlinePlayers().size();

                                    if(plugin.getMode() == 0) {
                                        if(a > 0 && 4 >= a) untilStartCt = 20;
                                        else if(a > 4 && 8 >= a) untilStartCt = 30;
                                        else if(a > 8 && 12 >= a) untilStartCt = 40;
                                        else untilStartCt = 60;

                                    } else untilStartCt = Options.untilGameStarts;

                                    Countdown untilStart = new Countdown(plugin,
                                            untilStartCt,

                                            () -> {
                                                Bukkit.broadcastMessage(plugin.prefix + "§7Game is currently §apreparing §8§o(§7§o" + (int) untilStartCt + "s§8§o)");

                                                if(plugin.getMode() == 1) {
                                                    if(plugin.getTeamPicking() == 0) {
                                                        for(Player online : Bukkit.getOnlinePlayers()) {
                                                            plugin.teams.playersToSpread.add(online.getUniqueId());
                                                            plugin.common.giveTeamChooser(online);
                                                        }
                                                    }

                                                    for(Team team : plugin.sc.getTeams())
                                                        try {
                                                            if(!team.getPlayers().isEmpty()) {
                                                                for(OfflinePlayer p : team.getPlayers()) team.removePlayer(p);
                                                            }
                                                        } catch(Exception e) {
                                                            Bukkit.broadcastMessage(plugin.prefix + "§cError: Failed to clear players from teams.");
                                                        }
                                                }
                                                for (Player online : Bukkit.getOnlinePlayers())
                                                    online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);
                                            },

                                            () -> {

                                                if(plugin.getMode() == 1) {
                                                    if(plugin.getTeamPicking() == 1) {

                                                        for (Player online : Bukkit.getOnlinePlayers()) plugin.teams.addPlayersToTeams(online);
                                                    } else {

                                                        if (!plugin.teams.playersToSpread.isEmpty()) {
                                                            for (int i = 0; i < plugin.teams.playersToSpread.size(); i++) {

                                                                Player toSpread = Bukkit.getPlayer(plugin.teams.playersToSpread.get(i));
                                                                plugin.teams.addPlayersToTeams(toSpread);
                                                            }
                                                        }
                                                    }
                                                    plugin.teams.inGameTeams.removeIf(team -> team.getPlayers() == null);
                                                }
                                                for(Player all : Bukkit.getOnlinePlayers()) {
                                                    plugin.players.add(all.getUniqueId());
                                                    all.getInventory().clear();

                                                    plugin.common.sendPacket(all, "§7Loading game...");
                                                    all.playSound(all.getLocation(), Sound.CHICKEN_EGG_POP, 1, Integer.MAX_VALUE);
                                                }
                                                preparing = true;
                                                plugin.common.prepareTp();

                                                new BukkitRunnable() {

                                                    @Override
                                                    public void run() {

                                                        if(plugin.getMode() == 1
                                                                && plugin.common.tpLocationTeams.size() == plugin.teams.inGameTeams.size() && plugin.teams.playersToSpread.isEmpty()
                                                        || plugin.getMode() == 0
                                                                && plugin.common.tpLocation.size() == plugin.players.size()) {
                                                            this.cancel();

                                                            running = true;

                                                            Countdown gtimer = new Countdown(plugin,
                                                                    plugin.timer,

                                                                    () -> {
                                                                        int a = plugin.platformSize;
                                                                        int b = plugin.height;

                                                                        Location loc3 = new Location(world, a, b + 1, -a);
                                                                        Location loc4 = new Location(world, -a, b + 1, a);
                                                                        plugin.common.setBlocksRegion(loc3, loc4, Material.AIR);

                                                                        Location loc1 = new Location(world, a, b, -a);
                                                                        Location loc2 = new Location(world, -a, b, a);
                                                                        plugin.common.setBlocksRegion(loc1, loc2, Material.AIR);

                                                                        plugin.common.tp();
                                                                        plugin.common.start(plugin.timer);
                                                                    },

                                                                    () -> {

                                                                        teleported = false;

                                                                        for (Player all : Bukkit.getOnlinePlayers()) {
                                                                            all.getInventory().clear();
                                                                            all.setGameMode(GameMode.SURVIVAL);

                                                                            CraftPlayer cp = ((CraftPlayer) all);
                                                                            cp.getHandle().triggerHealthUpdate();
                                                                        }
                                                                        world.setTime(0);
                                                                        world.getWorldBorder().setSize(Options.borderScale);
                                                                        world.getWorldBorder().setCenter(0, 0);
                                                                        world.getWorldBorder().setWarningDistance(25);

                                                                        for(Player all : Bukkit.getOnlinePlayers()) {
                                                                            all.playSound(all.getLocation(), Sound.ENDERDRAGON_GROWL, 1, Integer.MAX_VALUE);
                                                                            all.sendMessage(plugin.prefix + "§aGood luck!");

                                                                            DiamondLimit.dLimit.put(all.getUniqueId(), 0);

                                                                            plugin.gameScoreboard.initializeHealth(all);

                                                                            all.setScoreboard(Netherboard.instance().getBoard(all).getScoreboard());
                                                                            all.setHealth(all.getHealth());

                                                                            CraftPlayer cp = ((CraftPlayer) all);
                                                                            cp.getHandle().triggerHealthUpdate();
                                                                        }
                                                                        CatEyes catEyes       = new CatEyes(plugin);
                                                                        FinalHeal finalHeal   = new FinalHeal(plugin);
                                                                        Netheribus netheribus = new Netheribus(plugin);

                                                                        catEyes.execute();
                                                                        finalHeal.execute(Options.finalHealTime);
                                                                        netheribus.execute();

                                                                        plugin.gameScoreboard.startTimer();
                                                                        plugin.gameScoreboard.createGameScoreboard(player);
                                                                        new BukkitRunnable() {

                                                                            @Override
                                                                            public void run() {
                                                                                for(Player all : Bukkit.getOnlinePlayers()) plugin.gameScoreboard.updateGameScoreboard(all);
                                                                                if(!running || !preparing) this.cancel();
                                                                            }
                                                                        }.runTaskTimer(plugin, 5, 15);

                                                                        if(plugin.scenarios.contains("paranoia"))
                                                                            Bukkit.broadcastMessage(plugin.prefix + "§2Tip :§a Use /h to hide the§c Paranoïa§a alerts");

                                                                        if(plugin.getConfig().get("UHC" + "." + "Mode").equals("Teams"))
                                                                            Bukkit.broadcastMessage(plugin.prefix + "§2Tip :§a Type ! before your message to talk in the global chat");

                                                                        Countdown monsters = new Countdown(plugin,
                                                                                Options.monstersSpawn,
                                                                                () -> Bukkit.broadcastMessage(plugin.prefix + "§cMonsters§f will spawn in§7 " + Math.round((float) Options.monstersSpawn / 60) + " minute(s)"),

                                                                                () -> {
                                                                                    world.setMonsterSpawnLimit(70);
                                                                                    Bukkit.broadcastMessage(plugin.prefix + "§cMonsters§f will now §7spawn");
                                                                                },
                                                                                (t) -> {}
                                                                        );
                                                                        monsters.scheduleTimer();

                                                                        Countdown borderCd = new Countdown(plugin,
                                                                                Options.borderTime,
                                                                                () -> {},
                                                                                () -> {
                                                                                    world.getWorldBorder().setSize(250, Options.borderShrinkTime);
                                                                                    world.getWorldBorder().setCenter(0, 0);
                                                                                    world.getWorldBorder().setWarningDistance(25);

                                                                                    for(Player all : Bukkit.getOnlinePlayers()) {
                                                                                        all.playSound(all.getLocation(), Sound.ENDERDRAGON_GROWL, 1, Integer.MAX_VALUE);
                                                                                        all.sendMessage(plugin.prefix + "§bBorder§c is shrinking!");
                                                                                    }
                                                                                    border = true;
                                                                                },
                                                                                (t) -> {}
                                                                        );
                                                                        borderCd.scheduleTimer();

                                                                        Countdown pvp = new Countdown(plugin,
                                                                                Options.pvpTime,
                                                                                () -> {},
                                                                                () -> {
                                                                                    if(!world.getPVP() && !nether.getPVP()) {
                                                                                        for(Player all : Bukkit.getOnlinePlayers()) {

                                                                                            all.playSound(all.getLocation(), Sound.SUCCESSFUL_HIT, 1, Integer.MAX_VALUE);
                                                                                            all.sendMessage(plugin.prefix + "§bGrace period§c ended!");
                                                                                        }
                                                                                        world.setPVP(true);
                                                                                        nether.setPVP(true);
                                                                                    }
                                                                                },
                                                                                (t) -> {
                                                                                    int s = (int) t.getSecondsLeft();
                                                                                    if(!world.getPVP() && !nether.getPVP()) {

                                                                                        if(s == 600 || s == 300 || s == 60) {
                                                                                            Bukkit.broadcastMessage(plugin.prefix + "§bPVP §cwill be activated in §7" + s / 60 + " minute(s)");

                                                                                            for(Player all : Bukkit.getOnlinePlayers())
                                                                                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);

                                                                                        } else if(s <= 5) {
                                                                                            Bukkit.broadcastMessage(plugin.prefix + "§bPVP §cwill be activated in §7" + s + "s");

                                                                                            for(Player all : Bukkit.getOnlinePlayers())
                                                                                                all.playSound(all.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);
                                                                                        }
                                                                                    }
                                                                                }
                                                                        );
                                                                        pvp.scheduleTimer();
                                                                    },
                                                                    (t) -> {

                                                                        for(Player all : Bukkit.getOnlinePlayers()) {
                                                                            String text = "§4Start";
                                                                            String sec = "§8» §c" + t.getSecondsLeft() + "s§8 «";

                                                                            plugin.title.sendTitle(all, text, ChatColor.BLACK, 0, 30, 0);
                                                                            plugin.title.sendSubTitle(all, sec, ChatColor.BLACK, 0, 30, 0);

                                                                            all.playSound(all.getLocation(), Sound.CLICK, 1, Integer.MAX_VALUE);
                                                                            all.sendMessage(plugin.prefix + "§7Game starts in§c " + (int) t.getSecondsLeft() + "s");
                                                                        }
                                                                    }
                                                            );
                                                            gtimer.scheduleTimer();
                                                        }
                                                    }
                                                }.runTaskTimer(plugin, 0, 10);
                                            },

                                            (t) -> {
                                                untilStartSec = t.getSecondsLeft();

                                                if(t.getSecondsLeft() % 30 == 0 || t.getSecondsLeft() <= 10)  {
                                                    Bukkit.broadcastMessage(plugin.prefix + "§7Teleporting players in §c" + Math.round(t.getSecondsLeft()) + " §7second(s)!");

                                                    for (Player online : Bukkit.getOnlinePlayers())
                                                        online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, Integer.MAX_VALUE);
                                                }
                                            }
                                    );
                                    untilStart.scheduleTimer();
                                }
                            } else {
                                player.sendMessage(plugin.prefix + "§cYou must be host to do that.");
                            }
                        } else {
                            player.sendMessage(plugin.prefix + "§cPlease define host before doing that.");
                        }
                    } else {
                        player.sendMessage(plugin.prefix + "§cWrong usage. Try /start");
                    }
                } else {
                    player.sendMessage(plugin.prefix + "§cYou must be operator to do that.");
                }
            }
        }
        return false;
    }

    public void endGame() {
        String kickallMessage = plugin.prefix + "§cKicking players in§b " + (Options.kickTime / 60) + " minute(s)";
        String thxForPlaying = "§7§m                              " + "\n§cThanks for Playing!\n" + "§7§m                              ";

        border = false;

        Countdown kickall = new Countdown(plugin,
                Options.kickTime,
                () -> Bukkit.broadcastMessage(kickallMessage),
                () -> {
                    plugin.common.kickAll(thxForPlaying);

                    EasyCountdown shutdown = new EasyCountdown(plugin,
                            2.30D,
                            plugin.common::deleteWorlds
                    );
                    shutdown.scheduleTimer();
                },
                (t) -> {}
        );
        kickall.scheduleTimer();
    }
}
