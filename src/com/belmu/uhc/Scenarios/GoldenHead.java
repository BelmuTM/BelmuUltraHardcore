package com.belmu.uhc.Scenarios;

import com.belmu.uhc.UHC;
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
import java.util.Collections;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class GoldenHead implements Listener {

    public final UHC plugin;
    public GoldenHead(UHC plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onEnable(PluginEnableEvent e) {

        if (plugin.scenarios.contains("goldenhead")) {
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta headM = (SkullMeta) head.getItemMeta();

            headM.setDisplayName("§6Golden Head");
            headM.setLore(Collections.singletonList("§7Gives buffs."));
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
