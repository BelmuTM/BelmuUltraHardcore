package com.belmu.uhc.Core;

import com.belmu.uhc.Events.*;
import com.belmu.uhc.Scenarios.*;
import com.belmu.uhc.TeamsManager.TeamChooser;
import com.belmu.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class ListenerManager {

    public final UHC plugin;
    public ListenerManager(UHC plugin) {
        this.plugin = plugin;
    }

    public void registerListeners() {

        //Events
        reg(new CancelledEvents(plugin));
        reg(new FoodEvent(plugin));
        reg(new PlayerJoin(plugin));
        reg(new PlayerQuit(plugin));
        reg(new PlayerInteract(plugin));
        reg(new PlayerDeath(plugin));
        reg(new PlayerCraft());
        reg(new PlayerClick(plugin));
        reg(new PlayerChat(plugin));
        reg(new PlayerMove(plugin));
        reg(new TeamChooser(plugin));

        //Scenarios
        reg(new Timber(plugin));
        reg(new Paranoia(plugin));
        reg(new CutClean(plugin));
        reg(new DiamondLimit(plugin));
        reg(new FastMelting(plugin));
        reg(new FireLess(plugin));
        reg(new GoldenHead(plugin));
        reg(new NoCleanUp(plugin));
        reg(new NoFall(plugin));
        reg(new RodLess(plugin));
        reg(new VanillaPlus(plugin));
        reg(new TimeBomb(plugin));
        reg(new Netheribus(plugin));
    }

    private void reg(Listener listener) {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(listener, plugin);
    }
}
