package com.belmu.uhc.Scenarios;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Utils.Countdown;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class TimeBomb implements Listener {

    public final UHC plugin;
    public TimeBomb(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        UsefulMethods usefulMethods = new UsefulMethods(plugin);
        Player player = e.getEntity();
        World world = Bukkit.getWorld("world");

        Location loc = player.getLocation();
        Location chestLoc_1 = new Location(world, loc.getX(), loc.getY() + 1, loc.getZ());
        Location chestLoc_2 = new Location(world, loc.getX(), loc.getY() + 1, loc.getZ() + 1);

        Location standLoc = new Location(world, chestLoc_1.getBlockX() + 0.5, chestLoc_1.getBlockY() - 1, chestLoc_1.getBlockZ() + 1);

        if(plugin.scenarios.contains("timebomb")) {

            usefulMethods.setBlock(chestLoc_1, Material.CHEST);
            usefulMethods.setBlock(chestLoc_2, Material.CHEST);

            Chest c = (Chest)world.getBlockAt(chestLoc_1).getState();
            e.getDrops().clear();

            if(plugin.scenarios.contains("goldenhead")) {

                ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                SkullMeta headM = (SkullMeta) head.getItemMeta();

                headM.setDisplayName("§f" + player.getName() + "§7's head");
                headM.setOwner(player.getName());
                head.setItemMeta(headM);

                c.getBlockInventory().addItem(head);
            }
            for(int i = 0; i < player.getInventory().getContents().length; i++) {

                if(player.getInventory().getContents()[i] != null)
                    c.getBlockInventory().addItem(player.getInventory().getContents()[i]);
            }

            for(int i = 0; i < player.getInventory().getArmorContents().length; i++) {

                if(player.getInventory().getArmorContents()[i] != null)
                    c.getBlockInventory().addItem(player.getInventory().getArmorContents()[i]);
            }

            CraftArmorStand s = (CraftArmorStand) world.spawnEntity(standLoc, EntityType.ARMOR_STAND);
            int time = 140;

            Countdown explode = new Countdown(plugin,
                    time,

                    () -> {
                        s.setGravity(false);
                        s.setVisible(false);
                        s.setCustomNameVisible(true);
                    },
                    () -> {
                        world.createExplosion(chestLoc_1, 6);
                        s.remove();
                    },
                    (t) -> {
                        s.setCustomName("§7Exploding in §c" + Math.round(t.getSecondsLeft()) + "s");

                        if(t.getSecondsLeft() == 1) {

                            for(ItemStack items : c.getBlockInventory().getContents()) {

                                if(items != null)
                                    c.getBlockInventory().remove(items);
                            }
                        }
                    }
            );
            explode.scheduleTimer();
        }
    }

}
