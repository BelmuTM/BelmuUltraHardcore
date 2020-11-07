package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class GoldenHead implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onEnable(PluginEnableEvent e) {

        if (Main.scenarios.contains("goldenhead")) {

            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta headM = (SkullMeta) head.getItemMeta();

            headM.setDisplayName("§6Golden Head");
            headM.setLore(Arrays.asList("§7Gives buffs."));
            headM.setOwner("LegendaryJulien");
            head.setItemMeta(headM);

            ShapedRecipe recipe = new ShapedRecipe(head);
            recipe.shape(
                    "---",
                    "-#-",
                    "---");
            recipe.setIngredient('-', Material.GOLD_INGOT);
            recipe.setIngredient('#', Material.SKULL_ITEM, 3);

            Bukkit.addRecipe(recipe);
        }
    }

}
