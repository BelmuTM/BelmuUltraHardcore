package com.belmu.uhc.Scenarios;

import com.belmu.uhc.UHC;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Random;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class VanillaPlus implements Listener {

    public final UHC plugin;
    public VanillaPlus(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnable(PluginEnableEvent e) {

        if (plugin.scenarios.contains("vanilla+")) {

            ItemStack string = new ItemStack(Material.STRING, 3);

            ShapedRecipe recipe1 = new ShapedRecipe(string);
            recipe1.shape(
                    "   ",
                    "   ",
                    "---");

            ShapedRecipe recipe2 = new ShapedRecipe(string);
            recipe2.shape(
                    "   ",
                    "---",
                    "   ");

            ShapedRecipe recipe3 = new ShapedRecipe(string);
            recipe3.shape(
                    "---",
                    "   ",
                    "   ");

            recipe1.setIngredient('-', Material.WOOL);
            recipe2.setIngredient('-', Material.WOOL);
            recipe3.setIngredient('-', Material.WOOL);

            Bukkit.addRecipe(recipe1);
            Bukkit.addRecipe(recipe2);
            Bukkit.addRecipe(recipe3);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        if(plugin.scenarios.contains("vanilla+")) {
            if(!e.isCancelled()) {

                int data = e.getBlock().getData();
                if (e.getBlock().getType() == Material.LEAVES) {

                    if (player.getItemInHand().getType() != Material.SHEARS) {

                        if (data == 0 || data == 4 || data == 8 || data == 12)
                            plugin.common.dropApple(player, e.getBlock());
                    }

                }else if(e.getBlock().getType() == Material.LEAVES_2) {

                    if (data == 1 || data == 5 || data == 9 || data == 13)
                        plugin.common.dropApple(player, e.getBlock());

                } else if (e.getBlock().getType() == Material.GRAVEL) {

                    if(!plugin.scenarios.contains("cutclean")) {
                        Random random = new Random();
                        int upper = 9;

                        if (random.nextInt(upper) == 1) {

                            Location loc = e.getBlock().getLocation();
                            World world = Bukkit.getWorld("world");
                            ItemStack flint = new ItemStack(Material.FLINT, 1);

                            e.setCancelled(true);

                            world.getBlockAt(loc).setType(Material.AIR);
                            plugin.common.drop(loc, flint);
                        }
                    }
                }
            }
        }
    }
}
