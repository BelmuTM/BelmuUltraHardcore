package com.belmu.uhc.Events;

import com.belmu.uhc.UHC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class PlayerChat implements Listener {

    public final UHC plugin;
    public PlayerChat(UHC plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String msg = e.getMessage().replaceAll("<3", "§c❤§r").replaceAll("gg", "§agg§r")
                .replaceAll("ez", "Y'all are good players!");

        for(Player all : Bukkit.getOnlinePlayers())
            if(StringUtils.containsIgnoreCase(msg.toLowerCase(), all.getName().toLowerCase())) {
                Player mentioned = Bukkit.getPlayer(all.getName());

                if(mentioned != player) {
                    msg = msg.replaceAll("(?i)" + mentioned.getName(), "§e@" + mentioned.getName() + "§r");
                    mentioned.playSound(all.getLocation(), Sound.NOTE_PLING, 1f, Integer.MAX_VALUE);
                }
            }
        e.setMessage(msg);

        if(plugin.getMode().equalsIgnoreCase("Solo")) {
            e.setFormat(player.getDisplayName() + "§8 »§f " + e.getMessage());

        } else if(plugin.getMode().equalsIgnoreCase("Teams")) {

            if(plugin.game.running) {
                if(plugin.players.contains(player.getUniqueId())) {

                    if(msg.startsWith("!") && msg.length() <= 1) {
                        String finalMsg = msg.substring(1);
                        e.setFormat("§7[Global] " + player.getDisplayName() + "§8 »§f " + finalMsg);

                    } else {
                        ScoreboardManager m = Bukkit.getScoreboardManager();
                        Scoreboard s = m.getMainScoreboard();

                        for (OfflinePlayer p : s.getPlayerTeam(player).getPlayers()) {
                            if (p instanceof Player) {
                                e.setCancelled(true);
                                ((Player) p).sendMessage("§7[Team] " + player.getDisplayName() + "§8 »§f " + msg);
                            }
                        }
                    }
                } else if(!plugin.players.contains(player.getUniqueId()))
                    e.setFormat(player.getDisplayName() + "§8 »§f " + e.getMessage());
            } else
                e.setFormat(player.getDisplayName() + "§8 »§f " + e.getMessage());
        }
    }

}
