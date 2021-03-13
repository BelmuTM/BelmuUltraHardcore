package com.belmu.uhc.Commands;

import com.belmu.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class ForcePvPCommand implements CommandExecutor {

    public final UHC plugin;
    public ForcePvPCommand(UHC plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            World world = Bukkit.getWorld("world");
            World nether = Bukkit.getWorld("world_nether");

            if (cmd.getName().equalsIgnoreCase("forcepvp")) {

                if (player.isOp()) {
                    if(plugin.game.running) {
                        if (args.length == 0) {

                            if (world.getPVP() && nether.getPVP()) {
                                player.sendMessage(plugin.prefix + "§cPVP is already activated.");

                            } else if (!world.getPVP() && !nether.getPVP()) {

                                world.setPVP(true);
                                nether.setPVP(true);

                                Bukkit.broadcastMessage(plugin.prefix + "§bPVP§c was forcibly §a§lactivated");
                            }
                        } else
                            player.sendMessage(plugin.prefix + "§cWrong usage ! Try /forcepvp");
                    } else
                        player.sendMessage(plugin.prefix + "§cGame hasn't started yet.");
                } else
                    player.sendMessage(plugin.prefix + "§cYou must be operator to do that.");
            }
        }
        return false;
    }

}
