package com.belmu.uhc.Scenarios;

import com.belmu.uhc.UHC;
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

import static org.bukkit.Material.*;
import static org.bukkit.entity.EntityType.*;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class CutClean implements Listener {

    public final UHC plugin;
    public CutClean(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        Block block   = e.getBlock();
        Material type = block.getType();

        Location loc    = block.getLocation();
        Location orbLoc = new Location(plugin.world, loc.getX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5);

        ItemStack gold  = new ItemStack(GOLD_INGOT, 1);
        ItemStack iron  = new ItemStack(IRON_INGOT, 1);
        ItemStack flint = new ItemStack(FLINT, 1);
        ItemStack torch = new ItemStack(TORCH, 4);
        ItemStack glass = new ItemStack(GLASS, 1);

        if(plugin.scenarios.contains("cutclean")) {
            if (player.getGameMode() == GameMode.SURVIVAL && !e.isCancelled()) {
                Random random = new Random();
                int upper = 2;

                ItemStack item = null;
                int exp        = 0;

                switch(type) {
                    case GRAVEL:
                        item = flint;
                        break;
                    case SAND:
                        item = glass;
                        exp  = 1;
                        break;
                    case COAL_ORE:
                        item = torch;
                        exp  = 2;
                        break;
                    case IRON_ORE:
                        item = iron;
                        exp  = 2;
                        break;
                    case GOLD_ORE:
                        item = gold;
                        exp  = 3;
                        break;
                }

                block.setType(AIR);
                e.setCancelled(true);

                if(item != null) plugin.common.drop(loc, item);
                if(random.nextInt(upper) == 1) {
                    ExperienceOrb orb = (ExperienceOrb) plugin.world.spawnEntity(orbLoc, EXPERIENCE_ORB);
                    orb.setExperience(exp);
                }
            }
        }
    }

    private final Material[] swords = {
            WOOD_SWORD,
            STONE_SWORD,
            IRON_SWORD,
            GOLD_SWORD,
            DIAMOND_SWORD
    };

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        Player player = e.getEntity().getKiller();

        if(plugin.scenarios.contains("cutclean") && player != null) {

            for(ItemStack i : e.getDrops()) {
                switch(i.getType()) {
                    case PORK:
                        i.setType(GRILLED_PORK);
                        break;
                    case MUTTON:
                        i.setType(COOKED_MUTTON);
                        break;
                    case RABBIT:
                        i.setType(COOKED_RABBIT);
                        break;
                    case RAW_BEEF:
                        i.setType(COOKED_BEEF);
                        break;
                    case RAW_CHICKEN:
                        i.setType(COOKED_CHICKEN);
                        break;
                }
            }

            Material material = null;
            switch(entity.getType()) {
                case COW:
                    material = LEATHER;
                    break;
                case CHICKEN:
                    material = FEATHER;
                    break;
            }

            if(material != null) {
                ItemStack item = player.getItemInHand();
                Random r = new Random();

                for (Material sword : swords) {
                    if (item.getType() == sword) {
                        if (item.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) {

                            int max = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);

                            ItemStack drop = new ItemStack(material, r.nextInt(max + 2) + 1);
                            plugin.common.drop(entity.getLocation(), drop);
                            return;
                        }
                    }
                }
                ItemStack drop = new ItemStack(material, r.nextInt(3) + 1);
                plugin.common.drop(entity.getLocation(), drop);
            }
        }
    }
}
