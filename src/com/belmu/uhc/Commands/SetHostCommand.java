package com.belmu.uhc.Commands;

import com.belmu.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class SetHostCommand implements CommandExecutor {

    public final UHC plugin;
    public SetHostCommand(UHC plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("sethost")) {
                if(args.length == 1) {

                    if (!player.isOp())
                        player.sendMessage(plugin.prefix + "§cYou must be operator to do that.");

                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {

                        if(!plugin.game.preparing || !plugin.game.running) {

                            if (plugin.getConfig().get("Host") != null) {
                                plugin.getConfig().set("Host", null);
                            }
                            plugin.getConfig().set("Host", target.getName());
                            player.sendMessage(plugin.prefix + "§fHost has been successfully assigned to§7 " + target.getName());

                        } else
                            player.sendMessage(plugin.prefix + "§cGame has already started.");
                    } else
                        player.sendMessage(plugin.prefix + "§cUnknown player.");
                } else
                    player.sendMessage(plugin.prefix + "§cWrong usage. Try /sethost (player)");
            }
        }
        return false;
    }

}
