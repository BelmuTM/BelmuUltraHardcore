package com.belmu.uhc.Events;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Utils.Countdown;
import com.belmu.uhc.Utils.EasyCountdown;
import com.belmu.uhc.Core.Options;
import com.belmu.uhc.TeamsManager.*;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class PlayerQuit implements Listener {

    public final UHC plugin;
    public PlayerQuit(UHC plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        World world = Bukkit.getWorld("world");

        String playerSize = " §r§7(§c" + (Bukkit.getOnlinePlayers().size() - 1) + "§7/§c" + Bukkit.getMaxPlayers() + "§7)";
        String mainQuitMsg = plugin.prefix + player.getDisplayName() + "§r§f has quit the game" + playerSize;

        if(plugin.game.running) {
            if(plugin.players.contains(player.getUniqueId())) {

                plugin.inCooldown.add(player.getUniqueId());
                mainQuitMsg = plugin.prefix + "§7" + player.getName() + " §fhas quit the game §7(§c" + Math.round((float) Options.beforeElimination / 60) + "m §fleft before elimination§7)";

                Countdown elimination = new Countdown(plugin,
                        Options.beforeElimination,
                        () -> {},
                        () -> {
                        if(plugin.inCooldown.contains(player.getUniqueId())) {

                            plugin.players.remove(player.getUniqueId());
                            plugin.inCooldown.remove(player.getUniqueId());

                            world.strikeLightningEffect(player.getLocation());

                            for(Player all : Bukkit.getOnlinePlayers())
                                all.playSound(all.getLocation(), Sound.AMBIENCE_THUNDER, 1.0f, Integer.MAX_VALUE);

                            Bukkit.broadcastMessage(plugin.prefix + "§7" + player.getDisplayName() + "§f has been eliminated for §c§linactivity");

                            if(plugin.getMode().equalsIgnoreCase("Teams")) {

                                ScoreboardManager m = Bukkit.getScoreboardManager();
                                Scoreboard s = m.getMainScoreboard();

                                s.getPlayerTeam(player).removePlayer(player);
                                if(s.getPlayerTeam(player).getPlayers() == null) {

                                    EasyCountdown eliminationTeam = new EasyCountdown(plugin,
                                        1D,
                                        () -> {
                                            Bukkit.broadcastMessage(plugin.prefix + s.getPlayerTeam(player).getPrefix() + s.getPlayerTeam(player).getDisplayName() + "§f team has been eliminated!");

                                            Teams.inGameTeams.remove(PlayerDeath.playerTeam.get(player.getUniqueId()));

                                            for(Player all : Bukkit.getOnlinePlayers())
                                                all.playSound(all.getLocation(), Sound.WITHER_DEATH, 1, Integer.MAX_VALUE);
                                        }
                                    );
                                    eliminationTeam.scheduleTimer();
                                }
                            }
                        }
                    },
                        (t) -> {}
                );
                elimination.scheduleTimer();
            } else {
                PacketPlayOutPlayerInfo tablistInfo = new PacketPlayOutPlayerInfo
                        (PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle());

                for(Player all : Bukkit.getOnlinePlayers())
                    ((CraftPlayer) all).getHandle().playerConnection.sendPacket(tablistInfo);
            }
        }
        e.setQuitMessage(mainQuitMsg);
    }

}
