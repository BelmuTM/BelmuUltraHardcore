package com.belmu.uhc.Scenarios;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;

import java.util.Random;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class FastMelting implements Listener {

    public final UHC plugin;
    public FastMelting(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFurnaceBurn(final FurnaceBurnEvent e) {
        UsefulMethods usefulMethods = new UsefulMethods(plugin);
        Random r = new Random();

        if(plugin.scenarios.contains("fastsmelting")) {
            usefulMethods.startUpdate((Furnace) e.getBlock().getState(), r.nextBoolean() ? 1 : 2);
        }
    }
}
