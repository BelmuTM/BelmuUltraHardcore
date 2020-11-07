package com.belmu.uhc.Events;

import com.belmu.uhc.Main;
import com.belmu.uhc.Teams.Teams;
import com.belmu.uhc.Utils.CountdownWithInt;
import com.belmu.uhc.Utils.EasyCountdown;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerQuit implements Listener {

    int beforeElimination = 600;

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        Player player = e.getPlayer();
        World world = Bukkit.getWorld("world");

        Main.online.remove(player.getUniqueId());
        String mainQuitMsg = Main.prefix + player.getDisplayName() + "§r§f has quit!";

        if(Main.game.contains("running")) {

            if(Main.game.contains(player.getName())) {

                if(Main.spectators.contains(player.getName())) {

                    e.setQuitMessage(mainQuitMsg);

                    Main.game.remove(player.getName());
                    return;
                }

                Main.inCooldown.add(player.getUniqueId());
                e.setQuitMessage(Main.prefix + "§7" + player.getName() + "§f has quit the game. §7(§7" + beforeElimination / 60 + "§f left before elimination§7)");

                CountdownWithInt elimination = new CountdownWithInt(Main.getInstance(),
                        beforeElimination,
                        () -> {
                        },
                        () -> {

                    if(Main.inCooldown.contains(player.getUniqueId())) {

                        Main.players.remove(player.getName());
                        Main.spectators.add(player.getName());
                        Main.inCooldown.remove(player.getUniqueId());

                        world.strikeLightningEffect(player.getLocation());

                        for(Player all : Bukkit.getOnlinePlayers())
                            all.playSound(all.getLocation(), Sound.AMBIENCE_THUNDER, 1.0f, Integer.MAX_VALUE);

                        Bukkit.broadcastMessage(Main.prefix + "§7" + player.getDisplayName() + "§f has been eliminated for §cinactivity§f.");

                        if(Main.getMode().equalsIgnoreCase("Teams")) {

                            ScoreboardManager m = Bukkit.getScoreboardManager();
                            Scoreboard s = m.getMainScoreboard();

                            s.getPlayerTeam(player).removePlayer(player);
                            if(s.getPlayerTeam(player).getPlayers().size() == 0) {

                                EasyCountdown eliminationTeam = new EasyCountdown(Main.getInstance(),
                                        1D,
                                        () -> {
                                            Bukkit.broadcastMessage(Main.prefix + s.getPlayerTeam(player).getPrefix() + s.getPlayerTeam(player).getDisplayName() + "§f team has been eliminated!");

                                            Teams.inGameTeams.remove(PlayerDeath.playerTeam.get(player.getUniqueId()));

                                            for (Player all : Bukkit.getOnlinePlayers())
                                                all.playSound(all.getLocation(), Sound.WITHER_DEATH, 1, Integer.MAX_VALUE);
                                        }
                                );
                                eliminationTeam.scheduleTimer();

                            }
                        }
                    }
                        },
                        (t) -> {

                        }
                );
                elimination.scheduleTimer();

            } else if(!Main.players.contains(player.getName())) {

                e.setQuitMessage(mainQuitMsg);
            }

        } else if(!Main.game.contains("running")) {

            e.setQuitMessage(mainQuitMsg);
        }
    }

}
