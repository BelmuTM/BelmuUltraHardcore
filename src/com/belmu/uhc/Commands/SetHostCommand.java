package com.belmu.uhc.Commands;

import com.belmu.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHostCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("sethost")) {

                if(args.length == 1) {

                    if (!player.isOp()) {

                        player.sendMessage(Main.prefix + "§cYou must be operator to do that.");

                    }

                    Player target = Bukkit.getPlayer(args[0]);

                    if (target != null) {

                        if(!Main.partie.contains("willlancée") || !Main.partie.contains("lancée")) {

                            if(Main.getInstance().getConfig().get("Host") == null) {

                                Main.getInstance().getConfig().set("Host", target.getName());
                                player.sendMessage(Main.prefix + "§fHost has been successfully assigned to§7 " + target.getName() + "§f.");

                            } else {

                                Main.getInstance().getConfig().set("Host", null);
                                Main.getInstance().getConfig().set("Host", target.getName());
                                player.sendMessage(Main.prefix + "§fHost has been successfully assigned to§7 " + target.getName() + "§f.");

                            }

                        } else {

                            player.sendMessage(Main.prefix + "§cGame has already started.");

                        }

                    } else {

                        player.sendMessage(Main.prefix + "§cUnknown player.");

                    }

                } else {

                    player.sendMessage(Main.prefix + "§cWrong usage. Try /sethost (player).");

                }

            }

        }

        return false;
    }

}
