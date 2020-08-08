package com.belmu.uhc.Events;

import com.belmu.uhc.Main;
import com.belmu.uhc.Teams.Teams;
import com.belmu.uhc.Utils.CountdownWithInt;
import com.belmu.uhc.Utils.EasyCountdown;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        Player player = e.getPlayer();

        Main.online.remove(player.getUniqueId());

        if(Main.partie.contains("lancée")) {

            if(Main.joueurs.contains(player.getName())) {

                Main.inCooldown.add(player.getUniqueId());
                e.setQuitMessage(Main.prefix + "§7" + player.getName() + "§f has quit the game. §7(§75m§c left before§4 elimination§7)");

                CountdownWithInt elimination = new CountdownWithInt(Main.getInstance(),
                        600,
                        () -> {

                        },

                        () -> {

                    if(Main.inCooldown.contains(player.getUniqueId())) {

                        if(Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                            ScoreboardManager m = Bukkit.getScoreboardManager();
                            Scoreboard s = m.getMainScoreboard();

                            s.getPlayerTeam(player).removePlayer(player);

                            if(s.getPlayerTeam(player).getPlayers().size() == 0) {

                                EasyCountdown eliminationTeam = new EasyCountdown(Main.getInstance(),
                                        1D,
                                        () -> {

                                            Bukkit.broadcastMessage(Main.prefix + s.getPlayerTeam(player).getPrefix() + s.getPlayerTeam(player).getDisplayName() + "§f team has been eliminated!");

                                            Teams.inGameTeams.remove(PlayerDeath.playerTeam.get(player.getUniqueId()));

                                            for (Player all : Bukkit.getOnlinePlayers()) {

                                                all.playSound(all.getLocation(), Sound.WITHER_DEATH, 1, Integer.MAX_VALUE);

                                            }

                                        }

                                );
                                eliminationTeam.scheduleTimer();

                            }

                        } else if(!Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                            Main.joueurs.remove(player.getName());
                            Main.spectateurs.add(player.getName());

                            Bukkit.getWorld("world").strikeLightningEffect(player.getLocation());

                            Main.inCooldown.remove(player.getUniqueId());

                        }

                    }
                        },
                        (t) -> {

                        }
                );
                elimination.scheduleTimer();

            } else if(!Main.joueurs.contains(player.getName())) {

                e.setQuitMessage(Main.prefix + player.getDisplayName() + "§r§f has quit!");

            }

        } else if(!Main.partie.contains("lancée")) {

            e.setQuitMessage(Main.prefix + player.getDisplayName() + "§r§f has quit!");

        }

    }

}
