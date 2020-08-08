package com.belmu.uhc;

import com.belmu.uhc.Events.*;
import com.belmu.uhc.Main;
import com.belmu.uhc.Scenarios.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {

    public static void registerListeners() {

        //Events
        reg(new PlayerJoin());
        reg(new PlayerQuit());
        reg(new PlayerInteract());
        reg(new PlayerDeath());
        reg(new PlayerCraft());
        reg(new PlayerClick());
        reg(new PlayerChat());
        reg(new FoodEvent());
        reg(new CancelledEvents());
        reg(new PlayerMove());

        //Scenarios
        reg(new Timber());
        reg(new Paranoïa());
        reg(new CutClean());
        reg(new DiamondLimit());
        reg(new FastMelting());
        reg(new FireLess());
        reg(new GoldenHead());
        reg(new NoCleanUp());
        reg(new NoFall());
        reg(new RodLess());
        reg(new VanillaPlus());
        reg(new TimeBomb());
        reg(new Netheribus());

    }

    private static void reg(Listener listener) {

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(listener, Main.getInstance());

    }

}
