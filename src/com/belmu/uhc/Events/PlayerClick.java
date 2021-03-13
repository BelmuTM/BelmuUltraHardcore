package com.belmu.uhc.Events;

import com.belmu.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class PlayerClick implements Listener {

    public final UHC plugin;
    public PlayerClick(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Inventory inv = e.getClickedInventory();
        ItemStack cur = e.getCurrentItem();
        HumanEntity player = e.getWhoClicked();

        if (inv == null) return;
        if (cur == null) return;

        if (e.getInventory().getName().equals("Spectator Menu")) {

            if (!cur.hasItemMeta()) return;
            e.setCancelled(true);

            if (cur.getType() == Material.SKULL_ITEM) {

                HumanEntity target = Bukkit.getPlayer(cur.getItemMeta().getDisplayName());
                player.teleport(target.getLocation());
                player.sendMessage("§7You have been teleported to§f " + cur.getItemMeta().getDisplayName() + "§7.");
            }

        } else
            if(!plugin.players.contains(player.getUniqueId())) e.setCancelled(true);
    }

}
