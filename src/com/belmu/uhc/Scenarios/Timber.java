package com.belmu.uhc.Scenarios;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Utils.TreeCutter;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class Timber implements Listener {

    public final UHC plugin;
    public Timber(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();

        if(!e.isCancelled()) {
            if (plugin.scenarios.contains("timber")) {

                if (b.getType().equals(Material.LOG) || b.getType().equals(Material.LOG_2)) {
                    if(e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                        e.setCancelled(true);
                        new TreeCutter(b, plugin);

                        ItemStack item = e.getPlayer().getItemInHand();
                        for(Material tool : tools) {

                            if (item.getType() == tool)
                                item.setDurability((short) (item.getDurability() - 15));
                        }
                    }
                }
            }
        }
    }

    private Material[] tools = {
            Material.DIAMOND_AXE,
            Material.IRON_AXE,
            Material.WOOD_AXE,
            Material.STONE_AXE,
            Material.GOLD_AXE,

            Material.DIAMOND_PICKAXE,
            Material.IRON_PICKAXE,
            Material.WOOD_PICKAXE,
            Material.STONE_PICKAXE,
            Material.GOLD_PICKAXE,

            Material.DIAMOND_SWORD,
            Material.IRON_SWORD,
            Material.WOOD_SWORD,
            Material.STONE_SWORD,
            Material.GOLD_SWORD,

            Material.DIAMOND_HOE,
            Material.IRON_HOE,
            Material.WOOD_HOE,
            Material.STONE_HOE,
            Material.GOLD_HOE,

            Material.DIAMOND_SPADE,
            Material.IRON_SPADE,
            Material.WOOD_SPADE,
            Material.STONE_SPADE,
            Material.GOLD_SPADE,

            Material.SHEARS,
            Material.FLINT_AND_STEEL
    };

}
