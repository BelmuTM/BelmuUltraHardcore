package com.belmu.uhc.Events;

import com.belmu.uhc.Core.Packets.Tablist.Tablist;
import com.belmu.uhc.TeamsManager.Teams;
import com.belmu.uhc.UHC;
import com.belmu.uhc.Utils.UsefulMethods;
import fr.minuskube.netherboard.Netherboard;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

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
        FileConfiguration cfg = plugin.getConfig();
        UsefulMethods usefulMethods = new UsefulMethods(plugin);

        Player player = e.getPlayer();
        World world = Bukkit.getWorld("world");
        Netherboard.instance().removeBoard(player);

        String name = null;
        String joinMessage;

        CraftPlayer cp = ((CraftPlayer) player);
        cp.getHandle().triggerHealthUpdate();

        if(plugin.getMode().equals("Teams")) {
            if(plugin.getTeamPicking().equals("Normal")) {

                if(plugin.game.preparing) {
                    ItemStack ch = new ItemStack(Material.MELON, 1);
                    ItemMeta chM = ch.getItemMeta();

                    chM.setDisplayName("§fChoose Team §7(Right Click)");
                    ch.setItemMeta(chM);

                    if(!Teams.playersToSpread.contains(player.getUniqueId()))
                        Teams.playersToSpread.add(player.getUniqueId());

                    player.getInventory().setItem(0, ch);
                }
            }
        }

        if(player.isOp()) {
            String opName = "§c[OP]§7 " + player.getName();
            String opSpecName = "§7[S]§c[OP]§7 " + player.getName();

            if(!plugin.players.contains(player.getUniqueId())) {
                if(!plugin.game.running) name = opName;
                else name = opSpecName;

            } else name = opName;
        } else {
            String pName = "§7" + player.getName();
            String pSpecName = "§7[S] " + player.getName();

            if(!plugin.players.contains(player.getUniqueId())) {
                if(!plugin.game.running) name = pName;
                else name = pSpecName;

            } else name = pName;
        }

        if(plugin.players.contains(player.getUniqueId()) && plugin.inCooldown.contains(player.getUniqueId())) {
            joinMessage = plugin.prefix + player.getDisplayName() + " §fhas §areconnected";

        } else {
            String joinMsg = "§r§f joined the game";
            String playerSize = " §r§7(§c" + Bukkit.getOnlinePlayers().size() + "§7/§c" + Bukkit.getMaxPlayers() + "§7)";
            joinMessage = plugin.prefix + player.getDisplayName() + joinMsg + playerSize;
        }

        if(!plugin.players.contains(player.getUniqueId())) {
            if(plugin.game.running || plugin.game.preparing) {
                usefulMethods.setSpectator(player);

            } else {
                usefulMethods.clearEffects(player);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.getInventory().clear();
                player.spigot().setCollidesWithEntities(true);

                for(ItemStack i : player.getInventory().getArmorContents())
                    i.setType(Material.AIR);
            }
        } else {
            if(plugin.getConfig().get("UHC" + "." + "Mode").equals("Teams")) {
                Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();

                for(Team t : s.getTeams()) {
                    for(OfflinePlayer p : t.getPlayers())
                        name = t.getPrefix() + p.getPlayer().getName();
                }
            }
        }

        if(!cfg.contains(player.getName())) {
            cfg.set("Players", player.getName());
            plugin.saveConfig();
        }

        if(!plugin.game.running) {
            player.teleport(new Location(world, 0, world.getHighestBlockYAt(0, 0) + 2.5, 0));
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().setArmorContents(null);
        }
        Tablist tablist = new Tablist(plugin);
        tablist.animate(player);

        initializeScoreboard(player);
        player.setDisplayName(name);
        player.setPlayerListName(player.getDisplayName());

        e.setJoinMessage(joinMessage);
        plugin.inCooldown.remove(player.getUniqueId());
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
            }.runTaskTimer(plugin, 5, 15);
        }
    }

}
