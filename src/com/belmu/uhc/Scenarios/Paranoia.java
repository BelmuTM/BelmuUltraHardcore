package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Commands.HideCommand;
import com.belmu.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
public class Paranoia implements Listener {

    public final UHC plugin;
    public Paranoia(UHC plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, Location> portal = new HashMap<>();
    public static String prefix = "§7[§cParanoïa§7] ";

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        Material type = e.getBlock().getType();
        Block block   = e.getBlock();

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        if (plugin.scenarios.contains("paranoia") && !e.isCancelled()) {
            String ore = "";
            switch(type) {
                case EMERALD_ORE:
                    ore = "§aEmerald";
                    break;
                case GOLD_ORE:
                    ore = "§6Gold";
                    break;
                case DIAMOND_ORE:
                    ore = "§bDiamond";
                    break;
            }
            sendBroadcast(plugin.prefix + prefix + "§7" + player.getName() + "§f mined a " + ore + "§f ore at §8[§7X: " + x + " Y: " + y + " Z: " + z + "§8]");
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Material type      = e.getRecipe().getResult().getType();
        HumanEntity player = e.getView().getPlayer();
        Location loc       = player.getLocation();

        DecimalFormat format = new DecimalFormat("#");

        double xU = loc.getX();
        double yU = loc.getY();
        double zU = loc.getZ();

        String x = format.format(xU);
        String y = format.format(yU);
        String z = format.format(zU);

        if(player instanceof Player) {

            if (plugin.scenarios.contains("paranoia")) {
                String item = "";

                switch(type) {
                    case GOLDEN_APPLE:
                        item = "§eGolden Apple";
                        break;
                    case BREWING_STAND_ITEM:
                        item = "§dBrewing Stand";
                        break;
                }
                sendBroadcast(plugin.prefix + prefix + "§7" + player.getName() + "§f crafted a " + item + "§f at §8[§7X: " + x + " Y: " + y + " Z: " + z + "§8]");
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

                if (player.getWorld() == plugin.nether)
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
