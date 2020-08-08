package com.belmu.uhc.Commands;

import com.belmu.uhc.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HideCommand implements CommandExecutor {

    public static final Map<UUID, Boolean> hideParanoia = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("h")) {

                if(Main.partie.contains("lancée")) {

                    if(Main.scenarios.contains("paranoia")) {

                        if (args.length == 0) {

                            UUID uuid = player.getUniqueId();

                            if (hideParanoia.containsKey(uuid)) {

                                hideParanoia.remove(uuid);
                                player.sendMessage(Main.prefix + "§cParanoïa§f messages are now§a ON§f.");

                            } else if (!hideParanoia.containsKey(uuid)) {

                                hideParanoia.put(uuid, true);
                                player.sendMessage(Main.prefix + "§cParanoïa§f messages are now§c OFF§f.");

                            }

                        } else {

                            player.sendMessage(Main.prefix + "§cWrong usage. Try /h");

                        }

                    } else if(!Main.scenarios.contains("paranoia")) {

                        player.sendMessage(Main.prefix + "§cParanoïa isn't activated.");

                    }
                } else {

                    player.sendMessage(Main.prefix + "§cGame hasn't started yet.");

                }
            }
        }
        return false;
    }
}
