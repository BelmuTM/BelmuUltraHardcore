package com.belmu.uhc.Events;

import com.belmu.uhc.Core.Options;
import com.belmu.uhc.UHC;
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

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class PlayerInteract implements Listener {

    public final UHC plugin;
    public PlayerInteract(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack it  = e.getItem();

        if (it == null) return;

        if(!plugin.players.contains(player.getUniqueId())) {

            if (it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase(Options.compassName)) {
                if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    Inventory inv = specInventory();
                    if(inv == null) return;

                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (plugin.players.contains(all.getUniqueId()))
                            inv.addItem(plugin.common.getSkull(Bukkit.getPlayer(all.getUniqueId()).getName()));
                    }

                    if (plugin.players.size() != 0) player.openInventory(inv);
                    else player.sendMessage(plugin.prefix + "§cThere are no players alive!");
                }
                e.setCancelled(true);
            }

        } else {
            if (it.hasItemMeta() && it.getItemMeta().hasLore() && it.getType() == Material.SKULL_ITEM) {
                if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {

                    plugin.common.consumeItem(player, 1, it);

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

    private Inventory specInventory() {
        int size    = plugin.players.size();
        String name = "Spectator Menu";

        if(size <= 9 && size > 0)
            return Bukkit.createInventory(null, 9, name);

        else if(size <= 18 && size > 9)
            return Bukkit.createInventory(null, 18, name);

        else if(plugin.players.size() <= 27 && size > 18)
            return Bukkit.createInventory(null, 27, name);

        else if(plugin.players.size() <= 36 && size > 27)
            return Bukkit.createInventory(null, 36, name);

        else if(plugin.players.size() <= 45 && size > 36)
            return Bukkit.createInventory(null, 45, name);
        else
            return Bukkit.createInventory(null, 54, name);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Entity target = e.getRightClicked();

        if(target instanceof Player) {

            if(plugin.game.running && !plugin.players.contains(player.getUniqueId())) {
                Inventory i = Bukkit.createInventory(null, 54, target.getName() + "'s Inventory");

                ItemStack[] contents = ((Player) target).getInventory().getContents();
                if(contents != null)
                    for(ItemStack items : contents) if(items != null) i.addItem(items);

                ItemStack[] armorContents = ((Player) target).getInventory().getArmorContents();
                if(contents != null)
                    for(ItemStack items : armorContents) if(items != null) i.addItem(items);

                DecimalFormat format = new DecimalFormat("#");
                Double hlth = ((Player) target).getHealth();
                String h    = format.format(hlth);

                ItemStack health = new ItemStack(Material.APPLE, 1);
                ItemMeta m       = health.getItemMeta();

                m.setDisplayName("§fHealth §7: §c" + h + "§4❤");
                health.setItemMeta(m);

                ItemStack food = new ItemStack(Material.COOKED_BEEF, 1);
                ItemMeta m2    = food.getItemMeta();

                m2.setDisplayName("§fFood §7: §e" + ((Player) target).getFoodLevel() + "§6✚");
                food.setItemMeta(m2);

                i.setItem(45, food);
                i.setItem(46, health);

                player.openInventory(i);
            }
        }
    }
}
