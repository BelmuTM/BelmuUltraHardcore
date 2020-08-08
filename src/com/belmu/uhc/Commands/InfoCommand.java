package com.belmu.uhc.Commands;

import com.belmu.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("info")) {

                if (player.isOp()) {

                    if (args.length == 0) {

                        player.sendMessage(Main.prefix + "§cWrong usage ! Try /info (message).");

                    } else {

                        StringBuilder x = new StringBuilder();

                        for (int i = 0; i < args.length; i++) {

                            x.append(args[i].replace("&", "§") + " ");

                        }

                        Bukkit.broadcastMessage("§8[§cUHC§8]§7(§bInfo§7) " + player.getName() + "§8 »§f " + x.toString().trim());

                    }

                } else {

                    player.sendMessage(Main.prefix + "§cYou must be operator to do that.");

                }

            }

        }

        return false;

    }

}