package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Main;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class VanillaPlus implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        if(Main.scenarios.contains("vanilla+")) {

            if(!e.isCancelled()) {

                int data = e.getBlock().getData();
                if (e.getBlock().getType() == Material.LEAVES) {

                    if (player.getItemInHand().getType() != Material.SHEARS) {

                        if (data == 0 || data == 4 || data == 8 || data == 12) {
                            UsefulMethods.dropApple(player, e.getBlock());
                        }
                    }

                }else if(e.getBlock().getType() == Material.LEAVES_2) {

                    if (data == 1 || data == 5 || data == 9 || data == 13) {
                        UsefulMethods.dropApple(player, e.getBlock());
                    }

                } else if (e.getBlock().getType() == Material.GRAVEL) {

                    if(!Main.scenarios.contains("cutclean")) {

                        int upper = 9;
                        Random random = new Random();

                        if (random.nextInt(upper) == 1) {

                            Location loc = e.getBlock().getLocation();
                            World world = Bukkit.getWorld("world");
                            ItemStack flint = new ItemStack(Material.FLINT, 1);

                            e.setCancelled(true);

                            world.getBlockAt(loc).setType(Material.AIR);
                            UsefulMethods.drop(loc, flint);
                        }
                    }
                }
            }
        }
    }

}
