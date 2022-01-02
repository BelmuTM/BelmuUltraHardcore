package com.belmu.uhc.Events;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Scenarios.Paranoia;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
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
        Player target = e.getEntity();
        Player killer = target.getKiller();
        UUID uuid     = target.getUniqueId();

        World world  = target.getWorld();
        Location loc = target.getLocation();

        if(plugin.game.running) {
            if(killer != null) plugin.common.addKills(killer, 1);

            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta headM = (SkullMeta) head.getItemMeta();

            headM.setDisplayName("§f" + target.getName() + "§7's head");
            headM.setOwner(target.getName());
            head.setItemMeta(headM);

            if(plugin.scenarios.contains("goldenhead"))
                if(!plugin.scenarios.contains("timebomb")) plugin.common.drop(loc, head);

            inv.put(uuid, target.getInventory().getContents());
            armorInv.put(uuid, target.getInventory().getArmorContents());

            deadLocations.put(target.getUniqueId(), loc);

            world.strikeLightningEffect(loc);

            for(Player all : Bukkit.getOnlinePlayers())
                all.playSound(all.getLocation(), Sound.AMBIENCE_THUNDER, 1.0f, 1.0f);

            String rip       = "§cR.I.P.";
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

            if(plugin.getMode() == 1) {
                Team team = plugin.sc.getPlayerTeam(target);
                playerTeam.put(uuid, team);

                team.removePlayer(target);
                if(team.getPlayers().size() == 0) {

                    Bukkit.broadcastMessage(plugin.prefix + team.getPrefix() + team.getDisplayName() + "§f team has been eliminated!");
                    plugin.teams.inGameTeams.remove(playerTeam.get(uuid));

                    for (Player all : Bukkit.getOnlinePlayers())
                        all.playSound(all.getLocation(), Sound.WITHER_DEATH, 1, Integer.MAX_VALUE);
                }
            }

            String msg      = e.getDeathMessage().replace(target.getDisplayName(), "§7" + target.getDisplayName() + "§r");
            String paranoia = " §7at §8[§7X: " + x + " Y: " + y + " Z: " + z + "§8]";

            String deathMessage;

            if(killer != null) {
                String msg2     = msg.replace(target.getDisplayName(), "§7" + killer.getDisplayName() + "§r");
                String finalMsg = msg2.replace(".", "");

                if(plugin.scenarios.contains("paranoia")) {
                    deathMessage = "";
                    Paranoia.sendBroadcast(plugin.prefix + Paranoia.prefix + "§7" + finalMsg + paranoia);

                } else { deathMessage = plugin.prefix + msg2; }

            } else {
                String finalMsg = msg.replace(".", "");

                if (plugin.scenarios.contains("paranoia")) {
                    deathMessage = "";
                    Paranoia.sendBroadcast(plugin.prefix + Paranoia.prefix + "§7" + finalMsg + paranoia);

                } else { deathMessage = plugin.prefix + "§f" + msg; }
            }
            e.setDeathMessage(deathMessage);

            plugin.common.giveCompass(target);
            target.getInventory().setArmorContents(null);

            plugin.common.setSpectator(target);
        }
    }
}
