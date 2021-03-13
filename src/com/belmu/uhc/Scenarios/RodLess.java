package com.belmu.uhc.Scenarios;

import com.belmu.uhc.UHC;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class RodLess implements Listener {

    public final UHC plugin;
    public RodLess(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        Material type = e.getRecipe().getResult().getType();

        if(plugin.scenarios.contains("rodless")) {

            if (type == Material.FISHING_ROD)
                e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

}
