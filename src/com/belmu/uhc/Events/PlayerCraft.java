package com.belmu.uhc.Events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class PlayerCraft implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {

        byte data = e.getRecipe().getResult().getData().getData();
        Material type = e.getRecipe().getResult().getType();

        if(type == Material.GOLDEN_APPLE && data == 1)
            e.getInventory().setResult(new ItemStack(Material.AIR));
    }

}
