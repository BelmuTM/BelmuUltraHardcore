package com.belmu.uhc.Commands;

import com.belmu.uhc.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("menu")) {

                if (args.length == 0) {

                    if (!player.isOp()) {
                        player.sendMessage(Main.prefix + "§cYou must be operator to do that.");

                    } else {

                        if(Main.getInstance().getConfig().get("Host") != player.getName()) {
                            player.sendMessage(Main.prefix + "§cYou must be host to do that.");

                        } else if(Main.getInstance().getConfig().get("Host") == player.getName()) {

                            if(Main.game.contains("preparing") || Main.game.contains("running"))
                                player.sendMessage(Main.prefix + "§cGame has already started.");

                        }
                    }

                } else
                    player.sendMessage(Main.prefix + "§cWrong usage. Try /menu");

            }
        }

        return false;
    }

}
