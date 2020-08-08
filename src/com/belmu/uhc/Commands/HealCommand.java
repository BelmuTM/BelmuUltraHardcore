package com.belmu.uhc.Commands;

import com.belmu.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("heal")) {
                if (player.isOp()) {
                    if (args.length == 0) {
                        player.setFoodLevel(20);
                        player.setHealth(20);
                        player.sendMessage(Main.prefix + "§aHealed.");
                    }
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);

                        if (target != null) {
                            if (target != player) {
                                player.sendMessage(Main.prefix + "§7" + target.getName() + "§a has been healed.");
                                target.sendMessage(Main.prefix + "§aYou have been healed by §7 " + player.getName() + "§a.");
                                target.setFoodLevel(20);
                                target.setHealth(20);
                            } else {
                                player.setFoodLevel(20);
                                player.setHealth(20);
                                player.sendMessage(Main.prefix + "§aHealed.");
                            }
                        } else {
                            player.sendMessage(Main.prefix + "§cUnknown player.");
                        }
                    }
                    if (args.length > 1) {
                        player.sendMessage(Main.prefix + "§cWrong usage. Try /heal");
                    }
                } else {
                    player.sendMessage(Main.prefix + "§cYou must be operator to do that.");
                }
            }
        }
        return false;
    }
}