package com.belmu.uhc;

import com.belmu.uhc.Core.Start;
import com.belmu.uhc.Core.CommandManager;
import com.belmu.uhc.Core.ListenerManager;
import com.belmu.uhc.Core.Packets.GameScoreboard;
import com.belmu.uhc.Core.Packets.Title;
import com.belmu.uhc.TeamsManager.*;
import com.belmu.uhc.Utils.Lag;
import com.belmu.uhc.Utils.Properties;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class UHC extends JavaPlugin implements Listener {

    public int timer = 10;
    public int height = 150;
    public int platformSize = 25;

    public String prefix = "§8[§cUHC§8] »§7 ";

    public List<UUID> inCooldown = new ArrayList<>();
    public List<UUID> players = new ArrayList<>();
    public Map<UUID, Integer> kills = new HashMap<>();
    public List<String> scenarios = new ArrayList<>();

    public Start game;
    public GameScoreboard gameScoreboard;
    public Title title;

    private static UHC instance;
    public static UHC getInstance() { return instance; }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        instance = this;
        game = new Start(this);
        gameScoreboard = new GameScoreboard(this);
        title = new Title(this);

        game.running = false;
        getCommand("start").setExecutor(game);

        this.reloadConfig();
        getConfig().set("Players", null);
        getConfig().set("Host", "N/A");
        getConfig().set("UHC", null);
        getConfig().set("UHC" + "." + "Mode", "Solo");
        getConfig().set("UHC" + "." + "TeamPicking", "Normal");

        Properties.setServerProperty(Properties.ServerProperty.ANNOUNCE_PLAYER_ACHIEVEMENTS, false);
        Properties.setServerProperty(Properties.ServerProperty.FLIGHT, true);
        Properties.setServerProperty(Properties.ServerProperty.SPAWN_PROTECTION, 0);

        ListenerManager listenerManager = new ListenerManager(this);
        CommandManager commandManager = new CommandManager(this);
        listenerManager.registerListeners();
        commandManager.registerCommands();

        Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();
        for(Team team : s.getTeams()) team.unregister();

        Teams.initializeTeams();
        players.clear();

        addScenarios();
        worldSettings();
    }

    public String getMode() { return getConfig().get("UHC" + "." + "Mode").toString(); }
    public String getTeamPicking() { return getConfig().get("UHC" + "." + "TeamPicking").toString(); }

    public void worldSettings() {
        UsefulMethods usefulMethods = new UsefulMethods(this);

        World world = Bukkit.getWorld("world");
        World nether = Bukkit.getWorld("world_nether");

        Location loc1 = new Location(world, platformSize, height, -platformSize);
        Location loc2 = new Location(world, -platformSize, height, platformSize);
        usefulMethods.setBlocksRegion(loc1, loc2, Material.BARRIER);

        Location center = new Location(world, 0, height, 0);
        Location center2 = new Location(world, 0, height + 1, 0);

        usefulMethods.setBlocksRegionAsBorder(center, platformSize, Material.STAINED_GLASS);
        usefulMethods.setBlocksRegionAsBorder(center2, platformSize, Material.CARPET);

        world.setTime(0);
        world.setMonsterSpawnLimit(0);
        world.setDifficulty(Difficulty.NORMAL);
        world.setSpawnLocation(0, height + 1, 0);

        world.setPVP(false);
        nether.setPVP(false);
    }

    public void addScenarios() {
        scenarios.add("nocleanup");
        scenarios.add("diamondlimit");
        scenarios.add("vanilla+");
        scenarios.add("goldenhead");
        scenarios.add("finalheal");
        scenarios.add("timber");
        scenarios.add("cutclean");
    }

}
