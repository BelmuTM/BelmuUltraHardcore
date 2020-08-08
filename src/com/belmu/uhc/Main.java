package com.belmu.uhc;

import com.belmu.uhc.Teams.Teams;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

@SuppressWarnings("ALL")
public class Main extends JavaPlugin implements Listener {

    ////////////////////////////////////////
    //                                    //
    //     *Plugin developed by Belmu*    //
    //        *Twitter : @BelmuTM*        //
    //                                    //
    ////////////////////////////////////////

    /*

        Don't ever try to steal my sources you fool ʕ•ᴥ•ʔ

     */

    private static Main instance;

    public static int timer = 10;
    public static int height = 150;
    public static int platformSize = 25;

    public static String prefix = "§8[§cUHC§8] » ";
    public static List<UUID> online = new ArrayList<>();

    public static List<String> partie = new ArrayList<>();
    public static List<String> début = new ArrayList<>();

    public static List<String> joueurs = new ArrayList<>();
    public static List<String> spectateurs = new ArrayList<>();
    public static List<UUID> inCooldown = new ArrayList<>();

    public static List<String> scenarios = new ArrayList<>();
    public static List<String> border = new ArrayList<>();

    public static List<Boolean> justTeleported = new ArrayList<>();
    public static List<Boolean> preparation = new ArrayList<>();
    public static List<Boolean> fell = new ArrayList<>();

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(this, this);
        instance = this;

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        World world = Bukkit.getWorld("world");

        Location loc1 = new Location(world, platformSize, height, -platformSize);
        Location loc2 = new Location(world, -platformSize, height, platformSize);

        UsefulMethods.setBlocksRegion(loc1, loc2, Material.BARRIER);

        Location center = new Location(world, 0, height, 0);
        Location center2 = new Location(world, 0, height + 1, 0);

        UsefulMethods.setBlocksRegionAsBorder(center, platformSize, Material.STAINED_GLASS);
        UsefulMethods.setBlocksRegionAsBorder(center2, platformSize, Material.CARPET);

        ListenerManager.registerListeners();
        CommandManager.registerCommands();

        scenarios.add("nocleanup");
        scenarios.add("diamondlimit");
        scenarios.add("vanillaplus");
        scenarios.add("rodless");
        scenarios.add("goldenhead");
        scenarios.add("finalheal");
        scenarios.add("timber");
        scenarios.add("cutclean");

        world.setMonsterSpawnLimit(0);

        Teams.initializeTeams();

        world.setTime(0);
        world.setDifficulty(Difficulty.NORMAL);

        Bukkit.getWorld("world").setPVP(false);
        Bukkit.getWorld("world_nether").setPVP(false);

        world.getWorldBorder().reset();

        this.reloadConfig();

        getConfig().set("Players", null);
        getConfig().set("Host", "None");
        getConfig().set("UHC", null);
        getConfig().set("UHC" + "." + "Mode", "Teams");

        joueurs.clear();
        spectateurs.clear();

    }

    @Override
    public void onDisable() {

        Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();

        for(Team team : s.getTeams()) {

            team.unregister();

        }

    }

}