package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class RodLess implements Listener {

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {

        Material type = e.getRecipe().getResult().getType();

        if(Main.scenarios.contains("rodless")) {

            if (type == Material.FISHING_ROD) {

                e.getInventory().setResult(new ItemStack(Material.AIR));

            }

        }

    }

}
