package com.belmu.uhc.Events;

import com.belmu.uhc.Core.Options;
import com.belmu.uhc.UHC;
import com.belmu.uhc.Scenarios.Paranoïa;
import com.belmu.uhc.TeamsManager.Teams;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class PlayerDeath implements Listener {

    public final UHC plugin;
    public PlayerDeath(UHC plugin) {
        this.plugin = plugin;
    }

    public static Map<UUID, ItemStack[]> inv = new HashMap<>();
    public static Map<UUID, ItemStack[]> armorInv = new HashMap<>();
    public static Map<UUID, Location> deadLocations = new HashMap<>();

    public static Map<UUID, Team> playerTeam = new HashMap<>();

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        UsefulMethods usefulMethods = new UsefulMethods(plugin);

        Player target = e.getEntity();
        Player killer = target.getKiller();
        UUID uuid = target.getUniqueId();

        World world = target.getWorld();
        Location loc = target.getLocation();

        if(plugin.game.running) {
            if(killer != null) usefulMethods.addKills(killer, 1);

            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta headM = (SkullMeta) head.getItemMeta();

            headM.setDisplayName("§f" + target.getName() + "§7's head");
            headM.setOwner(target.getName());
            head.setItemMeta(headM);

            if(plugin.scenarios.contains("goldenhead"))
                if(!plugin.scenarios.contains("timebomb")) usefulMethods.drop(loc, head);

            inv.put(uuid, target.getInventory().getContents());
            armorInv.put(uuid, target.getInventory().getArmorContents());

            deadLocations.put(target.getUniqueId(), loc);

            world.strikeLightningEffect(loc);

            for(Player all : Bukkit.getOnlinePlayers())
                all.playSound(all.getLocation(), Sound.AMBIENCE_THUNDER, 1.0f, Integer.MAX_VALUE);

            String rip = "§cR.I.P.";
            String spectator = "§7You are spectator";

            plugin.title.sendTitle(target, rip, ChatColor.BLACK, 5, 75, 5);
            plugin.title.sendSubTitle(target, spectator, ChatColor.BLACK, 5, 75, 5);

            double xU = loc.getX();
            double yU = loc.getY();
            double zU = loc.getZ();

            DecimalFormat format = new DecimalFormat("#");
            String x = format.format(xU);
            String y = format.format(yU);
            String z = format.format(zU);

            ScoreboardManager m = Bukkit.getScoreboardManager();
            Scoreboard s = m.getMainScoreboard();

            String msg = e.getDeathMessage().replace(target.getDisplayName(), "§7" + target.getDisplayName() + "§r§f");
            String paranoia = " §7at §8[§7X: " + x + " Y: " + y + " Z: " + z + "§8]";

            String deathMessage = "";

            if(plugin.getMode().equalsIgnoreCase("Teams")) {

                Team team = s.getPlayerTeam(target);
                playerTeam.put(uuid, team);

                if(killer != null) {

                    String msg2 = msg.replace(killer.getDisplayName(), killer.getDisplayName() + "§r§f");
                    String finalMsg = msg2.replace(".", "");

                    if (plugin.scenarios.contains("paranoia")) {
                        deathMessage = "";
                        Paranoïa.sendBroadcast(plugin.prefix + Paranoïa.prefix + "§f" + finalMsg + paranoia);

                    } else if (!plugin.scenarios.contains("paranoia"))
                        deathMessage = plugin.prefix + msg2;

                } else {
                    String finalMsg = msg.replace(".", "");

                    if(plugin.scenarios.contains("paranoia")) {
                        deathMessage = "";
                        Paranoïa.sendBroadcast(plugin.prefix + Paranoïa.prefix + "§f" + finalMsg + paranoia);

                    } else if(!plugin.scenarios.contains("paranoia"))
                        deathMessage = plugin.prefix + "§f" + msg;
                }

                team.removePlayer(target);
                if(team.getPlayers().size() == 0) {

                    Bukkit.broadcastMessage(plugin.prefix + team.getPrefix() + team.getDisplayName() + "§f team has been eliminated!");
                    Teams.inGameTeams.remove(playerTeam.get(uuid));

                    for (Player all : Bukkit.getOnlinePlayers())
                        all.playSound(all.getLocation(), Sound.WITHER_DEATH, 1, Integer.MAX_VALUE);
                }

            } else if(plugin.getMode().equalsIgnoreCase("Solo")) {
                if(killer != null) {

                    String msg2 = msg.replace(killer.getName(), killer.getName() + "§f");
                    String finalMsg = msg2.replace(".", "");

                    if(plugin.scenarios.contains("paranoia")) {
                        deathMessage = "";
                        Paranoïa.sendBroadcast(plugin.prefix + Paranoïa.prefix + "§7" + finalMsg + paranoia);

                    } else if (!plugin.scenarios.contains("paranoia"))
                        deathMessage = plugin.prefix + msg2;
                } else {
                    String finalMsg = msg.replace(".", "");

                    if (plugin.scenarios.contains("paranoia")) {
                        deathMessage = "";
                        Paranoïa.sendBroadcast(plugin.prefix + Paranoïa.prefix + "§7" + finalMsg + paranoia);

                    } else if (!plugin.scenarios.contains("paranoia"))
                        deathMessage = plugin.prefix + "§f" + msg;
                }
            }
            if(killer != null) deathMessage.replaceAll(killer.getName(), killer.getDisplayName() + "§r");

            e.setDeathMessage(deathMessage.replaceAll(target.getName(), target.getDisplayName() + "§r"));

            usefulMethods.giveCompass(target);
            target.getInventory().setArmorContents(null);

            usefulMethods.setSpectator(target);
        }
    }

}
