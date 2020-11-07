package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Main;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;

import java.util.Random;

public class FastMelting implements Listener {

    @EventHandler
    public void onFurnaceBurn(final FurnaceBurnEvent e) {
        Random r = new Random();

        if(Main.scenarios.contains("fastsmelting")) {
            UsefulMethods.startUpdate((Furnace) e.getBlock().getState(), r.nextBoolean() ? 1 : 2);
        }
    }
}
