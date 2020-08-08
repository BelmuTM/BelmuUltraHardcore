package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Main;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class CutClean implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e){

        Player player = e.getPlayer();
        Block block = e.getBlock();

        Material type = block.getType();

        World world = Bukkit.getWorld("world");
        Location loc = block.getLocation();
        Location orbLoc = new Location(world, loc.getX() + 0.5000, loc.getBlockY() + 0.500, loc.getBlockZ() + 0.500);

        ItemStack gold = new ItemStack(Material.GOLD_INGOT, 1);
        ItemStack iron = new ItemStack(Material.IRON_INGOT, 1);
        ItemStack flint = new ItemStack(Material.FLINT, 1);
        ItemStack torch = new ItemStack(Material.TORCH, 4);
        ItemStack glass = new ItemStack(Material.GLASS, 1);

        if(Main.scenarios.contains("cutclean")) {

            if (!e.isCancelled()) {

                if(player.getGameMode() == GameMode.SURVIVAL) {

                    int upper = 2;

                    Random random = new Random();

                    if (type == Material.GOLD_ORE) {

                        block.setType(Material.AIR);
                        e.setCancelled(true);

                        UsefulMethods.drop(loc, gold);

                        if (random.nextInt(upper) == 1) {

                            ExperienceOrb orb = (ExperienceOrb) world.spawnEntity(orbLoc, EntityType.EXPERIENCE_ORB);

                            orb.setExperience(2);

                        }

                    }

                    if (type == Material.IRON_ORE) {

                        block.setType(Material.AIR);
                        e.setCancelled(true);

                        UsefulMethods.drop(loc, iron);

                        if (random.nextInt(upper) == 1) {

                            ExperienceOrb orb = (ExperienceOrb) world.spawnEntity(orbLoc, EntityType.EXPERIENCE_ORB);

                            orb.setExperience(2);

                        }

                    }

                    if (type == Material.GRAVEL) {

                        block.setType(Material.AIR);
                        e.setCancelled(true);

                        UsefulMethods.drop(loc, flint);

                    }

                    if (type == Material.COAL_ORE) {

                        block.setType(Material.AIR);
                        e.setCancelled(true);

                        UsefulMethods.drop(loc, torch);

                        if (random.nextInt(upper) == 1) {

                            ExperienceOrb orb = (ExperienceOrb) world.spawnEntity(orbLoc, EntityType.EXPERIENCE_ORB);

                            orb.setExperience(2);

                        }

                    }

                    if (type == Material.SAND) {

                        block.setType(Material.AIR);
                        e.setCancelled(true);

                        UsefulMethods.drop(loc, glass);

                        if (random.nextInt(upper) == 1) {

                            ExperienceOrb orb = (ExperienceOrb) world.spawnEntity(orbLoc, EntityType.EXPERIENCE_ORB);

                            orb.setExperience(1);

                        }

                    }

                }

            }

        }

    }


    private Material swords[] = {

            Material.WOOD_SWORD,
            Material.STONE_SWORD,
            Material.IRON_SWORD,
            Material.GOLD_SWORD,
            Material.DIAMOND_SWORD

    };

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {

        Entity entity = e.getEntity();
        Player player = e.getEntity().getKiller();

        World world = Bukkit.getWorld("world");

        if(Main.scenarios.contains("cutclean")) {

            if (entity instanceof Pig) {

                for (ItemStack i : e.getDrops()) {

                    if (i.getType() == Material.PORK) {

                        i.setType(Material.GRILLED_PORK);

                    }

                }

            }

            if (entity instanceof Sheep) {

                for (ItemStack i : e.getDrops()) {

                    if (i.getType() == Material.MUTTON) {

                        i.setType(Material.COOKED_MUTTON);

                    }

                }

            }

            if (entity instanceof Rabbit) {

                for (ItemStack i : e.getDrops()) {

                    if (i.getType() == Material.RABBIT) {

                        i.setType(Material.COOKED_RABBIT);

                    }

                }

            }

            if (entity instanceof Cow) {

                for (ItemStack i : e.getDrops()) {

                    if (i.getType() == Material.RAW_BEEF) {

                        i.setType(Material.COOKED_BEEF);

                    }

                }

                if(player instanceof Player) {

                    ItemStack item = player.getItemInHand();

                    for (int i = 0; i < swords.length; i++) {

                        if (item.getType() == swords[i]) {

                            if (item.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) {

                                int max = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);

                                Random r = new Random();
                                int upper = ((max - 2) + 1) + 2; //((max - min) + 1) + min;

                                ItemStack leather = new ItemStack(Material.LEATHER, r.nextInt(upper));
                                UsefulMethods.dropCutClean(entity.getLocation(), leather);

                                return;

                            }

                        }

                    }

                }

                Random r = new Random();
                int upper = ((3 - 2) + 1) + 2; //((max - min) + 1) + min;

                ItemStack leather = new ItemStack(Material.LEATHER, r.nextInt(upper));
                Item item = UsefulMethods.dropCutClean(entity.getLocation(), leather);

                item.setPickupDelay(0);

            }

            if (entity instanceof Chicken) {

                for (ItemStack i : e.getDrops()) {

                    if (i.getType() == Material.RAW_CHICKEN) {

                        i.setType(Material.COOKED_CHICKEN);

                    }

                }

                if(player instanceof Player) {

                    ItemStack item = player.getItemInHand();

                    for (int i = 0; i < swords.length; i++) {

                        if (item.getType() == swords[i]) {

                            if (item.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) {

                                int max = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
                                Random r = new Random();
                                int upper = ((max - 2) + 1) + 2; //((max - min) + 1) + min;

                                ItemStack feather = new ItemStack(Material.FEATHER, r.nextInt(upper));
                                UsefulMethods.dropCutClean(entity.getLocation(), feather);

                                return;

                            }

                        }

                    }

                }

                Random r = new Random();
                int upper = ((3 - 2) + 1) + 2; //((max - min) + 1) + min;

                ItemStack feather = new ItemStack(Material.FEATHER, r.nextInt(upper));
                UsefulMethods.dropCutClean(entity.getLocation(), feather);

            }
        }

    }

}
