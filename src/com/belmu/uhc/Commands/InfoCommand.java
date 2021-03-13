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
public class InfoCommand implements CommandExecutor {

    public final UHC plugin;
    public InfoCommand(UHC plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("info")) {
                if (player.isOp()) {

                    if (args.length == 0) {
                        player.sendMessage(plugin.prefix + "§cWrong usage. Try /info (message)");
                    } else {
                        StringBuilder x = new StringBuilder();
                        for(int i = 0; i < args.length; i++) {
                            x.append(args[i].replace("&", "§") + " ");
                        }
                        Bukkit.broadcastMessage("§8[§cUHC§8]§7(§bInfo§7) " + player.getName() + "§8 »§f " + x.toString().trim());
                    }
                } else
                    player.sendMessage(plugin.prefix + "§cYou must be operator to do that.");
            }
        }
        return false;
    }

}