package com.belmu.uhc.Events;

import com.belmu.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerChat implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        String msg = e.getMessage();

        if(Main.getMode().equalsIgnoreCase("Solo")) {

            if (player.isOp()) {
                e.setFormat(player.getDisplayName() + "§8 »§f " + e.getMessage());

            } else
                e.setFormat(player.getDisplayName() + "§8 »§7 " + e.getMessage());

        } else if(Main.getMode().equalsIgnoreCase("Teams")) {

            if(Main.game.contains("lancée")) {

                if(!Main.spectators.contains(player.getName())) {

                    if (msg.startsWith("!")) {
                        String finalMsg = msg.replace("!", "");

                        if(player.isOp()) {
                            e.setFormat("§7[Global] " + player.getDisplayName() + "§8 »§f " + finalMsg);

                        } else
                            e.setFormat("§7[Global] " + player.getDisplayName() + "§8 »§7 " + finalMsg);

                    } else {
                        ScoreboardManager m = Bukkit.getScoreboardManager();
                        Scoreboard s = m.getMainScoreboard();

                        for (OfflinePlayer p : s.getPlayerTeam(player).getPlayers()) {

                            if (p instanceof Player) {
                                e.setCancelled(true);

                                if(player.isOp()) {
                                    ((Player) p).sendMessage("§7[Team] " + player.getDisplayName() + "§8 »§f " + msg);

                                } else
                                    ((Player) p).sendMessage("§7[Team] " + player.getDisplayName() + "§8 »§7 " + msg);
                            }
                        }
                    }

                } else if(Main.spectators.contains(player.getName())) {

                    if (player.isOp()) {
                        e.setFormat(player.getDisplayName() + "§8 »§f " + e.getMessage());

                    } else
                        e.setFormat(player.getDisplayName() + "§8 »§7 " + e.getMessage());
                }

            } else {

                if (player.isOp()) {
                    e.setFormat(player.getDisplayName() + "§8 »§f " + e.getMessage());

                } else
                    e.setFormat(player.getDisplayName() + "§8 »§7 " + e.getMessage());
            }
        }
    }

}
