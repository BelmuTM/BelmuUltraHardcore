package com.belmu.uhc.Scoreboard;

import com.belmu.uhc.Main;
import com.belmu.uhc.Teams.Teams;
import com.belmu.uhc.Utils.Options;
import com.belmu.uhc.Utils.UsefulMethods;
import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class GameScoreboard {

    private static FileConfiguration cfg = Main.getInstance().getConfig();

    public static void createGameScoreboard(Player player) {

        BPlayerBoard board = Netherboard.instance().createBoard(player, "uhcScoreboard");

        if(cfg.get("UHC" + "." + "Mode").equals("Teams")) {

            board.setName("§c§lUHC Anciens");

        } else if(cfg.get("UHC" + "." + "Mode").equals("Solo")) {

            board.setName("§c§lUHC Solo");

        } else {

            board.setName("§c§lUHC");

        }

    }

    public static void createLobbyScoreboard(Player player) {

        BPlayerBoard board = Netherboard.instance().createBoard(player, "lobbyScoreboard");
        board.setName("§c§lUHC");

    }

    public static void updateLobbyScoreboard(Player player) {

        BPlayerBoard b = Netherboard.instance().getBoard(player);

        FileConfiguration cfg = Main.getInstance().getConfig();

        String g = "§7";
        String m = "§m";
        String l = g+m;

        String u = l+"---------------";
        String u2 = l+"--------------";

        b.set(u2, 4);
        b.set("§7Online§7»§c " + Bukkit.getOnlinePlayers().size() + "§7/§c" + Bukkit.getMaxPlayers(), 3);
        b.set("§7Host§7»§a " + cfg.get("Host"), 2);
        b.set(u, 1);

    }

    public static void updateGameScoreboard(Player player) {

        BPlayerBoard b = Netherboard.instance().getBoard(player);

        World world = Bukkit.getWorld("world");

        Location loc = new Location(player.getWorld(), 0, player.getLocation().getY(), 0);

        String g = "§7";
        String m = "§m";
        String l = g + m;
        String c = "§c";
        String q = "§a";
        String t = "§f";
        String r = "§r";
        String fo = "§4";

        String z = "§c:§7 ";
        String re = "§8»";

        String f1 = l + "------»" + r;
        String e1 = l + "«------";

        String f2 = l + "-----»" + r;
        String e2 = l + "«------";

        String u = l + "------------------";

        DecimalFormat format = new DecimalFormat("#");
        Double bord = world.getWorldBorder().getSize() / 2;
        String o = format.format(bord);

        Double dis = player.getLocation().distance(loc);
        String w = format.format(dis);

        String y = "±" + o;

        String v = UsefulMethods.calculateRelativeDirectionFromPlayerTo(player, loc);
        String h = v + g + " (" + w + ")";

        String ti = GameScoreboard.getTimer();

        String uf = fo + "OFF";
        String of = q + "ON";

        String p = g + " (" + (Options.pvpSeconds / 60) + "m)";
        String d = g + " (" + (Options.borderSeconds / 60) + "m)";

        int k = UsefulMethods.getKills(player);

        b.set(f1 + c + " Time " + e1, 11);

        if (world.getPVP()) {

            b.set(re + c + "PvP" + g + z + of + p, 10);

        } else {

            b.set(re + c + "PvP" + g + z + uf + p, 10);

        }

        if (Main.border.contains("yes")) {

            b.set(re + c + "Border" + g + z + of + d, 9);

        } else {

            b.set(re + c + "Border" + g + z + uf + d, 9);

        }

        b.set(re + c + "Timer" + g + z + t + ti, 8);


        b.set(f2 + c + " Stats " + e2, 7);

        int si = Main.joueurs.size();
        int go = Teams.inGameTeams.size();

        if (cfg.get("UHC" + "." + "Mode").equals("Solo")) {

            b.set(re + c + "Players" + g + z + si, 6);

        } else if (cfg.get("UHC" + "." + "Mode").equals("Teams")) {

            b.set(re + c + "Teams" + z + t + go + g + " (" + si + ")", 6);

        }

        b.set(re + c + "Kills" + g + z + k, 5);

        b.set(f1 + c + " Map " + e1, 4);

        b.set(re + c + "Border" + g + z + t + y, 3);
        b.set(re + c + "Center" + g + z + t + h, 2);

        b.set(u, 1);

    }

    private static int timer = 0;

    public static void startScoreboardTimer() {

        new BukkitRunnable() {

            @Override
            public void run() {
                timer++;
            }

        }.runTaskTimer(Main.getInstance(), 20, 20);

    }

    private static String getTimer() {

        return new SimpleDateFormat("mm:ss").format(timer * 1000);

    }

    public static void initializeHealth(Player player) {

        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();

        if(sb.getObjective("showhealth") == null) {

            Objective objective = sb.registerNewObjective("showhealth", "health");

            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName(ChatColor.RED + "❤");

            Objective objective2 = sb.registerNewObjective("showhealth2", "health");

            objective2.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        }

        player.setScoreboard(sb);

    }

}
