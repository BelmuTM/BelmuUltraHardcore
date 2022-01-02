package com.belmu.uhc.Core.Packets;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Core.Options;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class GameScoreboard {

    public final UHC plugin;
    public GameScoreboard(UHC plugin) {
        this.plugin = plugin;
    }

    public int timer;

    public void createGameScoreboard(Player player) {
        BPlayerBoard board = fr.minuskube.netherboard.Netherboard.instance().createBoard(player, "uhcScoreboard");

        String name;
        switch(plugin.getMode()) {
            case 0:
                name = "§c§lUHC Solo";
                break;
            case 1:
                name = "§c§lUHC Teams";
                break;
            default:
                name = "§c§lUHC";
                break;
        }
        board.setName(name);
    }

    public void createLobbyScoreboard(Player player) {
        BPlayerBoard board = fr.minuskube.netherboard.Netherboard.instance().createBoard(player, "lobbyScoreboard");
        board.setName("§c§lUHC Lobby");
    }

    public void updateLobbyScoreboard(Player player) {
        BPlayerBoard b = fr.minuskube.netherboard.Netherboard.instance().getBoard(player);
        FileConfiguration cfg = plugin.getConfig();

        String gray = "§7";
        String l1 = gray + "§m" + "---------------";
        String l2 = gray + "§m" + "---------------§r";

        b.set("§7Online §7»§c " + Bukkit.getOnlinePlayers().size() + "§7/§c" + Bukkit.getMaxPlayers(), 3);

        if(plugin.game.untilStartSec > 1 && !plugin.game.running) b.set("§7Start §7»§a " + (int) plugin.game.untilStartSec + "s", 2);
        else b.set("§7Host §7»§a " + cfg.get("Host"), 2);

        b.set(l1, 4);
        b.set(l2, 1);
    }

    public void updateGameScoreboard(Player player) {
        BPlayerBoard b = fr.minuskube.netherboard.Netherboard.instance().getBoard(player);
        Location loc   = new Location(player.getWorld(), 0, player.getLocation().getY(), 0);

        int kills = plugin.common.getKills(player);
        int alive = plugin.players.size();
        int teams = plugin.teams.inGameTeams.size();

        String white = "§f";
        String gray = "§7";
        String red = "§c";

        String separ = "§c:§7 ";
        String arrow = "§8»";

        String l1 = gray + "§m" + "------»" + "§r";
        String l2 = gray + "§m" + "«------";
        String endL = gray + "§m" + "------------------";

        DecimalFormat format = new DecimalFormat("#");
        Double bord          = plugin.world.getWorldBorder().getSize() / 2;

        String formattedBorder = format.format(bord);
        String borderSize      = "±" + formattedBorder;

        Double dist              = player.getLocation().distance(loc);
        String formattedDistance = format.format(dist);

        String compass     = plugin.common.calculateRelativeDirectionFromPlayerTo(player, loc);
        String compassLine = compass + gray + " (" + formattedDistance + ")";

        String timer = getTimer();

        String off = "§4" + "OFF";
        String on = "§a" + "ON";

        b.set(l1 + red + " Time " + l2, 11);

        String pvpMinutes    = gray + " (" + (Options.pvpTime / 60) + "m)";
        String borderMinutes = gray + " (" + (Options.borderTime / 60) + "m)";

        String pvp;
        if(plugin.world.getPVP())
            pvp = arrow + red + "PvP" + gray + separ + on;
        else pvp = arrow + red + "PvP" + gray + separ + off;

        b.set(pvp + pvpMinutes, 10);

        String border;
        if(plugin.game.border)
            border = arrow + red + "Border" + gray + separ + on;
        else border = arrow + red + "Border" + gray + separ + off;

        b.set(border + borderMinutes, 9);

        b.set(arrow + red + "Timer" + gray + separ + white + timer, 8);
        b.set(l1 + red + " Stats " + l2, 7);

        String playersLeft = "";

        if(plugin.getMode() == 0)
            playersLeft = arrow + red + "Players" + gray + separ + alive;
        else if(plugin.getMode() == 1)
            playersLeft = arrow + red + "Teams" + separ + white + teams + gray + " (" + alive + ")";

        b.set(playersLeft, 6);

        b.set(arrow + red + "Kills" + gray + separ + kills, 5);
        b.set(l1 + red + " Map " + l2, 4);
        b.set(arrow + red + "Border" + gray + separ + white + borderSize, 3);
        b.set(arrow + red + "Center" + gray + separ + white + compassLine, 2);
        b.set(endL, 1);
    }

    public void startTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                timer++;
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    private String getTimer() {
        return new SimpleDateFormat("HH:mm:ss").format(timer * 1000);
    }

    public void initializeHealth(Player player) {
        Scoreboard sb = fr.minuskube.netherboard.Netherboard.instance().getBoard(player).getScoreboard();

        if(sb.getObjective("showhealth") == null) {

            Objective objective = sb.registerNewObjective("showhealth", "health");
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName(ChatColor.RED + "❤");

            Objective objective2 = sb.registerNewObjective("showhealth2", "health");
            objective2.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }
        player.setScoreboard(sb);

        for(Player all : Bukkit.getOnlinePlayers()) {
            all.setHealth(all.getHealth());

            CraftPlayer cp = ((CraftPlayer) player);
            cp.getHandle().triggerHealthUpdate();
        }
    }
}
