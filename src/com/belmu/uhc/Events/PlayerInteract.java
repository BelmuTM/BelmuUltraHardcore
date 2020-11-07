package com.belmu.uhc.Events;

import com.belmu.uhc.Main;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;

public class PlayerInteract implements Listener {

    @EventHandler
    public void interact(PlayerInteractEvent e) {

        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack it = e.getItem();

        if (it == null) return;

        if(Main.spectators.contains(player.getName())) {

            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {

                if (it.hasItemMeta()) {

                    if (it.getItemMeta().hasDisplayName()) {

                        if (it.getItemMeta().getDisplayName().equalsIgnoreCase("§fSpectate§7 (Right Click)")) {

                            Inventory inv = specInventory();
                            e.setCancelled(true);

                            for (Player all : Bukkit.getOnlinePlayers()) {

                                if (Main.players.contains(all.getName())) {

                                    if (!Main.spectators.contains(all.getName()))
                                        inv.addItem(UsefulMethods.getSkull(all.getName()));
                                }
                            }

                            try {
                                player.openInventory(inv);

                            } catch (NullPointerException exc) {
                                player.sendMessage(Main.prefix + "§cThere is no player alive!");
                            }

                        } else if (!it.getItemMeta().getDisplayName().equalsIgnoreCase("§fSpectate§7 (Right Click)"))
                            e.setCancelled(true);

                    } else if (!it.getItemMeta().hasDisplayName())
                        e.setCancelled(true);

                } else if (!it.hasItemMeta())
                    e.setCancelled(true);
            }

        } else if(!Main.spectators.contains(player.getName())) {

            if (it.hasItemMeta()) {

                if(it.getItemMeta().hasLore()) {

                    if (it.getType() == Material.SKULL_ITEM) {

                        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {

                            UsefulMethods.consumeItem(player, 1, it);

                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));

                            if(player.getHealth() < 20 &&  player.getHealth() > 0)
                                player.setHealth(player.getHealth() + 4);

                            for (Player all : Bukkit.getOnlinePlayers())
                                all.playSound(player.getLocation(), Sound.EAT, 1, Integer.MAX_VALUE);
                        }
                    }
                }
            }
        }
    }

    private Inventory specInventory() {

        int size = Main.players.size();

        if(size <= 9 && size > 0) {
            return Bukkit.createInventory(null, 9, "Spectator Menu");

        } else if(size <= 18 && size > 9) {
            return Bukkit.createInventory(null, 18, "Spectator Menu");

        } else if(Main.players.size() <= 27 && size > 18) {
            return Bukkit.createInventory(null, 27, "Spectator Menu");

        } else if(Main.players.size() <= 36 && size > 27) {
            return Bukkit.createInventory(null, 36, "Spectator Menu");

        } else if(Main.players.size() <= 45 && size > 36) {
            return Bukkit.createInventory(null, 45, "Spectator Menu");

        } else if(Main.players.size() <= 54 && size > 45) {
            return Bukkit.createInventory(null, 54, "Spectator Menu");

        }
        return null;
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {

        Player player = e.getPlayer();
        Entity target = e.getRightClicked();

        if(target instanceof Player) {

            if(Main.spectators.contains(player.getName())) {
                Inventory i = Bukkit.createInventory(null, 54, target.getName() + "'s Inventory");

                for(ItemStack c : ((Player) target).getInventory().getContents()) {

                    if(c != null)
                        i.addItem(c);
                }

                for(ItemStack ac : ((Player) target).getInventory().getArmorContents()) {

                    if(ac != null)
                        i.addItem(ac);
                }

                DecimalFormat format = new DecimalFormat("#");
                Double hlth = ((Player) target).getHealth();
                String h = format.format(hlth);

                ItemStack health = new ItemStack(Material.APPLE, 1);
                ItemMeta m = health.getItemMeta();

                m.setDisplayName("§fHealth§7 :§c " + h + "§4❤");
                health.setItemMeta(m);

                ItemStack food = new ItemStack(Material.COOKED_BEEF, 1);
                ItemMeta m2 = food.getItemMeta();

                m2.setDisplayName("§fFood§7 :§e " + ((Player) target).getFoodLevel() + "§6✚");
                food.setItemMeta(m2);

                i.setItem(45, food);
                i.setItem(46, health);

                player.openInventory(i);
            }
        }
    }

}
