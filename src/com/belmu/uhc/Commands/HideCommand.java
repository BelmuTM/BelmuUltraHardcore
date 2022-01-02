package com.belmu.uhc.Commands;

import com.belmu.uhc.UHC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class HideCommand implements CommandExecutor {

    public final UHC plugin;
    public HideCommand(UHC plugin) {
        this.plugin = plugin;
    }

    public static final Map<UUID, Boolean> hideParanoia = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("h")) {
                if(plugin.game.running) {
                    if(plugin.scenarios.contains("paranoia")) {

                        if (args.length == 0) {
                            UUID uuid = player.getUniqueId();

                            if (hideParanoia.containsKey(uuid)) {
                                hideParanoia.remove(uuid);
                                player.sendMessage(plugin.prefix + "§cParanoïa§f messages are now§a ON");

                            } else {
                                hideParanoia.put(uuid, true);
                                player.sendMessage(plugin.prefix + "§cParanoïa§f messages are now§c OFF");
                            }
                        } else
                            player.sendMessage(plugin.prefix + "§cWrong usage. Try /h");
                    } else {
                        player.sendMessage(plugin.prefix + "§cParanoïa isn't activated.");
                    }
                } else {
                    player.sendMessage(plugin.prefix + "§cGame hasn't started yet.");
                }
            }
        }
        return false;
    }
}
