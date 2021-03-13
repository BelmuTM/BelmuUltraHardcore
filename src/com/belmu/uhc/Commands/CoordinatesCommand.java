package com.belmu.uhc.Commands;

import com.belmu.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class CoordinatesCommand implements CommandExecutor {

    public final UHC plugin;
    public CoordinatesCommand(UHC plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("co")) {
                if (plugin.game.running) {
                    if (args.length == 0) {

                        if(plugin.getMode().equalsIgnoreCase("Teams")) {

                            ScoreboardManager m = Bukkit.getScoreboardManager();
                            Scoreboard s = m.getMainScoreboard();

                            Location loc = player.getLocation();

                            long x = loc.getBlockX();
                            long y = loc.getBlockY();
                            long z = loc.getBlockZ();

                            if(!plugin.players.contains(player.getUniqueId())) {

                                for (OfflinePlayer p : s.getPlayerTeam(player).getPlayers()) {
                                    if (p instanceof Player)
                                        ((Player) p).sendMessage("§8[§cUHC§8]§b " + player.getName() + "§8 » X:§7" + x + "§8 Y:§7" + y + "§8 Z:§7" + z);
                                }
                            } else
                                player.sendMessage(plugin.prefix + "§cYou can not do that as a spectator.");

                        } else if(plugin.getMode().equalsIgnoreCase("Solo"))
                            player.sendMessage(plugin.prefix + "§cYou don't have any team mates!");
                    } else
                        player.sendMessage(plugin.prefix + "§cWrong usage. Try /co");
                } else
                    player.sendMessage(plugin.prefix + "§cGame hasn't started yet.");
            }
        }

        return false;
    }

}
