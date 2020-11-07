package com.belmu.uhc.Events;

import com.belmu.uhc.Main;
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

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        Player player = e.getPlayer();
        World world = Bukkit.getWorld("world");

        Location loc = new Location(world, 0, Main.height + 1.500, 0);

        if(!Main.game.contains("running")) {

            if (player.getLocation().getY() < Main.height) player.teleport(loc);

        } else {

            if(Main.preparation) {

                if (player.getLocation().getY() < Main.height) player.teleport(loc);
            }
        }

        if(Main.getMode().equalsIgnoreCase("Solo")) {

            if (Main.justTeleported) {

                if (UsefulMethods.tpLocation.containsKey(player.getUniqueId())) {

                    Location tpLoc = UsefulMethods.tpLocation.get(player.getUniqueId());
                    Location pLoc = player.getLocation();

                    if (pLoc.getX() != tpLoc.getX() && pLoc.getZ() != tpLoc.getZ()) player.teleport(tpLoc);
                }
            }

        } else if(Main.getMode().equalsIgnoreCase("Teams")) {

            if (Main.justTeleported) {

                ScoreboardManager m = Bukkit.getScoreboardManager();
                Scoreboard s = m.getMainScoreboard();

                Team team = s.getPlayerTeam(player);

                if (UsefulMethods.tpLocationTeams.containsKey(team)) {

                    Location tpLoc = UsefulMethods.tpLocationTeams.get(team);
                    Location pLoc = player.getLocation();

                    if (pLoc.getX() != tpLoc.getX() && pLoc.getZ() != tpLoc.getZ()) player.teleport(tpLoc);
                }
            }
        }
    }

}
