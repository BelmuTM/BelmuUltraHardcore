package com.belmu.uhc.Events;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

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
        World world = Bukkit.getWorld("world");
        Location loc = new Location(world, 0, plugin.height + 1.500, 0);

        if(!plugin.game.running && !plugin.game.teleported)
            if (player.getLocation().getY() < plugin.height) player.teleport(loc);
        else
            if(plugin.game.preparing && !plugin.game.teleported) if(player.getLocation().getY() < plugin.height) player.teleport(loc);
    }

}
