package com.belmu.uhc.Events;

import com.belmu.uhc.Core.Packets.Tablist.Tablist;
import com.belmu.uhc.UHC;
import com.belmu.uhc.Utility.Common;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class PlayerJoin implements Listener {

    public final UHC plugin;
    public PlayerJoin(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player         = e.getPlayer();
        FileConfiguration cfg = plugin.getConfig();

        fr.minuskube.netherboard.Netherboard.instance().removeBoard(player);

        String name;
        String joinMessage;

        CraftPlayer cp = ((CraftPlayer) player);
        cp.getHandle().triggerHealthUpdate();

        if(player.isOp()) {
            String opName = "§c[OP]§7 " + player.getName();
            String opSpecName = "§7[S]§c[OP]§7 " + player.getName();

            if (!plugin.players.contains(player.getUniqueId())) {
                if (!plugin.game.running) name = opName;
                else name = opSpecName;

            } else name = opName;
        } else {
            String pName = "§7" + player.getName();
            String pSpecName = "§7[S] " + player.getName();

            if (!plugin.players.contains(player.getUniqueId())) {
                if (!plugin.game.running) name = pName;
                else name = pSpecName;

            } else name = pName;
        }

        if(plugin.getMode() == 1 && plugin.sc.getPlayerTeam(player) != null)
            name = plugin.sc.getPlayerTeam(player).getPrefix() + player.getName();

        if(plugin.players.contains(player.getUniqueId()) && plugin.inCooldown.contains(player.getUniqueId())) {
            joinMessage = plugin.prefix + name + " §fhas §areconnected";

        } else {
            String joinMsg = "§r§f joined the game";
            String playerSize = " §r§7(§c" + Bukkit.getOnlinePlayers().size() + "§7/§c" + Bukkit.getMaxPlayers() + "§7)";
            joinMessage = plugin.prefix + name + joinMsg + playerSize;
        }

        if(!plugin.players.contains(player.getUniqueId())) {
            if (plugin.game.running || plugin.game.preparing && plugin.game.teleported) {
                plugin.common.setSpectator(player);

            } else {
                plugin.common.clearEffects(player);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.getInventory().clear();
                player.spigot().setCollidesWithEntities(true);

                for (ItemStack i : player.getInventory().getArmorContents()) i.setType(Material.AIR);
            }
        }
        player.setDisplayName(name);
        player.setPlayerListName(name);

        if (!cfg.contains(player.getName())) {
            cfg.set("Players", player.getName());
            plugin.saveConfig();
        }

        if (!plugin.game.running) {
            player.teleport(new Location(plugin.world, 0, plugin.world.getHighestBlockYAt(0, 0) + 2.5, 0));
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().setArmorContents(null);
        }
        Tablist tablist = new Tablist(plugin);
        tablist.animate(player);

        initializeScoreboard(player);

        e.setJoinMessage(joinMessage);
        plugin.inCooldown.remove(player.getUniqueId());

        if (plugin.getMode() == 1) {
            if (plugin.getTeamPicking() == 0) {

                if(!plugin.players.contains(player.getUniqueId())) {
                    if(plugin.game.preparing && !plugin.game.teleported) {
                        plugin.common.giveTeamChooser(player);

                        if (!plugin.teams.playersToSpread.contains(player.getUniqueId()))
                            plugin.teams.playersToSpread.add(player.getUniqueId());
                    }
                }
            }
        }
    }

    public void initializeScoreboard(Player player) {

        if(!plugin.game.running) {
            plugin.gameScoreboard.createLobbyScoreboard(player);
            new BukkitRunnable() {

                @Override
                public void run() {
                    plugin.gameScoreboard.updateLobbyScoreboard(player);
                    if(plugin.game.running) this.cancel();
                }
            }.runTaskTimer(plugin, 5, 15);
        } else {

            plugin.gameScoreboard.createGameScoreboard(player);
            plugin.gameScoreboard.initializeHealth(player);
            new BukkitRunnable() {

                @Override
                public void run() {
                    plugin.gameScoreboard.updateGameScoreboard(player);
                    if (!plugin.game.running || !plugin.game.preparing) this.cancel();
                }
            }.runTaskTimer(plugin, 10, 20);
        }
    }

}
