package com.belmu.uhc.Commands;

import com.belmu.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForcePvPCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;
            World world = Bukkit.getWorld("world");
            World nether = Bukkit.getWorld("world_nether");

            if (cmd.getName().equalsIgnoreCase("forcepvp")) {

                if (player.isOp()) {

                    if(Main.game.contains("running")) {

                        if (args.length == 0) {

                            if (world.getPVP() && nether.getPVP()) {
                                player.sendMessage(Main.prefix + "§cPvP is already activated.");

                            } else if (!world.getPVP() && !nether.getPVP()) {

                                world.setPVP(true);
                                nether.setPVP(true);

                                Bukkit.broadcastMessage(Main.prefix + "§bPvP§c was forcibly §7activated§c.");
                            }

                        } else
                            player.sendMessage(Main.prefix + "§cWrong usage ! Try /forcepvp");

                    } else
                        player.sendMessage(Main.prefix + "§cGame hasn't started yet.");

                } else
                    player.sendMessage(Main.prefix + "§cYou must be operator to do that.");

            }
        }

        return false;
    }

}
