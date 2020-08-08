package com.belmu.uhc.Commands;

import com.belmu.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class CoordinatesCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("co")) {

                if (Main.partie.contains("lancée")) {

                    if (args.length == 0) {

                        if(Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                            ScoreboardManager m = Bukkit.getScoreboardManager();
                            Scoreboard s = m.getMainScoreboard();

                            Location loc = player.getLocation();

                            long x = loc.getBlockX();
                            long y = loc.getBlockY();
                            long z = loc.getBlockZ();

                            if(!Main.spectateurs.contains(player.getName())) {

                                for (OfflinePlayer p : s.getPlayerTeam(player).getPlayers()) {

                                    if (p instanceof Player) {

                                        ((Player) p).sendMessage("§8[§cUHC§8]§b " + player.getName() + "§8 » X:§7" + x + "§8 Y:§7" + y + "§8 Z:§7" + z);

                                    }

                                }

                            } else {

                                player.sendMessage(Main.prefix + "§cYou can not do that as a spectator.");

                            }

                        } else if(!Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                            player.sendMessage(Main.prefix + "§cGame isn't in Teams mode.");

                        }

                    } else {

                        player.sendMessage(Main.prefix + "§cWrong usage. Try /co");

                    }

                } else {

                    player.sendMessage(Main.prefix + "§cGame hasn't started yet.");

                }

            }

        }

        return false;

    }

}
