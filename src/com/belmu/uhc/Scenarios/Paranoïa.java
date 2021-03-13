package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Commands.HideCommand;
import com.belmu.uhc.UHC;
import com.belmu.uhc.Core.Options;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class Paranoïa implements Listener {

    public final UHC plugin;
    public Paranoïa(UHC plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, Location> portal = new HashMap<>();
    public static String prefix = "§7[§cParanoïa§7] ";

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        FileConfiguration cfg = plugin.getConfig();

        Material type = e.getBlock().getType();
        Block block = e.getBlock();

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        if (plugin.scenarios.contains("paranoia")) {
            if (!e.isCancelled()) {

                if (type == Material.EMERALD_ORE)
                    sendBroadcast(plugin.prefix + prefix + "§7" + player.getName() + "§f mined an §aEmerald§f ore at §8[§7X: " + x + " Y: " + y + " Z: " + z + "§8]");

                if (type == Material.GOLD_ORE)
                    sendBroadcast(plugin.prefix + prefix + "§7" + player.getName() + "§f mined a §6Gold§f ore at §8[§7X: " + x + " Y: " + y + " Z: " + z + "§8]");

                if (type == Material.DIAMOND_ORE) {

                    if(!plugin.scenarios.contains("diamondlimit")) {
                        sendBroadcast(plugin.prefix + prefix + "§7" + player.getName() + "§f mined a §bDiamond§f ore at §8[§7X: " + x + " Y: " + y + " Z: " + z + "§8]");

                    } else if(plugin.scenarios.contains("diamondlimit")) {

                        int a = cfg.getInt("Players" + "." + player.getName() + "." + "diamond");
                        if(a >= (Options.diamondLimit - 1)) {

                            return;
                        } else
                            sendBroadcast(plugin.prefix + prefix + "§7" + player.getName() + "§f mined a §bDiamond§f ore at §8[§7X: " + x + " Y: " + y + " Z: " + z + "§8]");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {

        Material type = e.getRecipe().getResult().getType();
        HumanEntity player = e.getView().getPlayer();

        Location loc = player.getLocation();

        DecimalFormat format = new DecimalFormat("#");

        double xU = loc.getX();
        double yU = loc.getY();
        double zU = loc.getZ();

        String x = format.format(xU);
        String y = format.format(yU);
        String z = format.format(zU);

        if(player instanceof Player) {

            if (plugin.scenarios.contains("paranoia")) {

                if (type == Material.GOLDEN_APPLE)
                    sendBroadcast(plugin.prefix + prefix + "§7" + player.getName() + "§f crafted a §eGolden Apple§f at §8[§7X: " + x + " Y: " + y + " Z: " + z + "§8]");

                if (type == Material.BREWING_STAND_ITEM)
                    sendBroadcast(plugin.prefix + prefix + "§7" + player.getName() + "§f crafted a §dBrewing Stand§f at §8[§7X: " + x + " Y: " + y + " Z: " + z + "§8]");
            }
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        portal.put(uuid, player.getLocation());
    }

    @EventHandler
    public void onNether(PlayerChangedWorldEvent e) {

        try {
            Player player = e.getPlayer();
            Location loc = portal.get(player.getUniqueId());

            DecimalFormat format = new DecimalFormat("#");

            double xU = loc.getX();
            double yU = loc.getY();
            double zU = loc.getZ();

            String x = format.format(xU);
            String y = format.format(yU);
            String z = format.format(zU);

            if (plugin.scenarios.contains("paranoia")) {

                if (player.getWorld() == Bukkit.getWorld("world_nether"))
                    sendBroadcast(plugin.prefix + prefix + "§7" + player.getName() + "§f entered the §cNether§f at §8[§7X: " + x + " Y: " + y + " Z: " + z + "§8]");
            }

        } catch (NullPointerException ex) {
            System.out.println("Having trouble with the PlayerChangedWorldEvent...");
        }
    }

    public static void sendBroadcast(String msg) {

        for(Player all : Bukkit.getOnlinePlayers()) {
            if (!HideCommand.hideParanoia.containsKey(all.getUniqueId()))
                all.sendMessage(msg);
        }
    }

}
