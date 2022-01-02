package com.belmu.uhc;

import com.belmu.uhc.Core.Start;
import com.belmu.uhc.Core.CommandManager;
import com.belmu.uhc.Core.ListenerManager;
import com.belmu.uhc.Core.Packets.GameScoreboard;
import com.belmu.uhc.Core.Packets.Title;
import com.belmu.uhc.TeamsManager.*;
import com.belmu.uhc.Utility.Lag;
import com.belmu.uhc.Utility.Properties;
import com.belmu.uhc.Utility.Common;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class UHC extends JavaPlugin implements Listener {

    public final int timer        = 10;
    public final int height       = 150;
    public final int platformSize = 25;

    public final String prefix = "§8[§cUHC§8] »§7 ";

    public List<UUID> inCooldown    = new ArrayList<>();
    public List<UUID> players       = new ArrayList<>();
    public Map<UUID, Integer> kills = new HashMap<>();
    public List<String> scenarios   = new ArrayList<>();

    public Start game;
    public GameScoreboard gameScoreboard;
    public Title title;
    public Teams teams;
    public Common common;
    public World world;
    public World nether;
    public ScoreboardManager scm;
    public Scoreboard sc;

    private static UHC instance;
    public static UHC getInstance() { return instance; }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        instance       = this;
        game           = new Start(this);
        gameScoreboard = new GameScoreboard(this);
        title          = new Title(this);
        teams          = new Teams(this);
        common         = new Common(this);

        world          = Bukkit.getWorld("world");
        nether         = Bukkit.getWorld("world_nether");

        scm            = Bukkit.getScoreboardManager();
        sc             = Bukkit.getScoreboardManager().getMainScoreboard();

        game.running = false;
        getCommand("start").setExecutor(game);

        this.reloadConfig();
        getConfig().set("Players", null);
        getConfig().set("Host", "N/A");
        getConfig().set("UHC", null);
        getConfig().set("UHC" + "." + "Mode", 0); // 0 = Solo | 1 = Teams
        getConfig().set("UHC" + "." + "TeamPicking", "Normal"); // 0 = Normal | 1 = Random

        Properties.setServerProperty(Properties.ServerProperty.ANNOUNCE_PLAYER_ACHIEVEMENTS, false);
        Properties.setServerProperty(Properties.ServerProperty.FLIGHT, true);
        Properties.setServerProperty(Properties.ServerProperty.SPAWN_PROTECTION, 0);
        Properties.savePropertiesFile();

        ListenerManager listenerManager = new ListenerManager(this);
        CommandManager commandManager   = new CommandManager(this);
        listenerManager.registerListeners();
        commandManager.registerCommands();

        for(Team team : sc.getTeams()) team.unregister();
        teams.initializeTeams();

        players.clear();
        worldSettings();
    }

    public int getMode()        { return Integer.parseInt(getConfig().get("UHC" + "." + "Mode").toString());        }
    public int getTeamPicking() { return Integer.parseInt(getConfig().get("UHC" + "." + "TeamPicking").toString()); }

    public void worldSettings() {
        Location loc1 = new Location(world, platformSize, height, -platformSize);
        Location loc2 = new Location(world, -platformSize, height, platformSize);
        common.setBlocksRegion(loc1, loc2, Material.BARRIER);

        Location center  = new Location(world, 0, height, 0);
        Location center2 = new Location(world, 0, height + 1, 0);
        common.setBlocksRegionAsBorder(center, platformSize, Material.STAINED_GLASS);
        common.setBlocksRegionAsBorder(center2, platformSize, Material.CARPET);

        world.setTime(0);
        world.setMonsterSpawnLimit(0);
        world.setDifficulty(Difficulty.NORMAL);
        world.setSpawnLocation(0, height + 1, 0);

        world.setPVP(false);
        nether.setPVP(false);
    }
}
