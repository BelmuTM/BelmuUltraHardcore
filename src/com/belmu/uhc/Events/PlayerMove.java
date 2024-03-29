package com.belmu.uhc.Events;

import com.belmu.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class PlayerMove implements Listener {

    public final UHC plugin;
    public PlayerMove(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location loc  = new Location(plugin.world, 0, plugin.height + 1.500, 0);

        if(!plugin.game.running && !plugin.game.teleported)
            if (player.getLocation().getY() < plugin.height) player.teleport(loc);
        else
            if(plugin.game.preparing && !plugin.game.teleported) if(player.getLocation().getY() < plugin.height) player.teleport(loc);
    }
}
