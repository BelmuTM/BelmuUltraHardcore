package com.belmu.uhc.Commands;

import com.belmu.uhc.UHC;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class ScenariosCommand implements CommandExecutor {

    public final UHC plugin;
    public ScenariosCommand(UHC plugin) {
        this.plugin = plugin;
    }

    private String[] validScenarios = {
            "CatEyes",
            "CutClean",
            "DiamondLimit",
            "FastSmelting",
            "FinalHeal",
            "Fireless",
            "GoldenHead",
            "Netheribus",
            "NoCleanup",
            "NoFall",
            "Paranoia",
            "RodLess",
            "Timber",
            "TimeBomb",
            "Vanilla+"
    };

    public boolean containsCaseInsensitive(String s, List<String> l) {
        for (String string : l) {
            if (string.equalsIgnoreCase(s)) return true;
        }
        return false;
    }

    public boolean onCommand(CommandSender sender, Command cmd, java.lang.String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("scenarios")) {
                if (args.length == 0) {
                    String scenarios;

                    if(plugin.scenarios.size() == 0) {
                        scenarios = "None";
                    } else {
                        StringBuilder a = new StringBuilder();
                        for (int i = 0; i < plugin.scenarios.size(); i++) {

                            a.append(" §f|§7 ").append(plugin.scenarios.get(i));
                        }
                        scenarios = a.toString().trim();
                    }

                    player.sendMessage(plugin.prefix + "§bActive Scenarios§7: " + scenarios);
                } else if(args.length == 2) {
                    if (player.isOp()) {
                        if(plugin.game.running) {
                            player.sendMessage(plugin.prefix + "§cGame has already started.");
                            return false;
                        }

                        if (args[0].equalsIgnoreCase("add")) {
                            if (plugin.scenarios.contains(args[1])) {
                                player.sendMessage(plugin.prefix + "§cScenario already exists.");
                                return false;
                            }

                            if(!containsCaseInsensitive(args[1], Arrays.asList(validScenarios))) {
                                StringBuilder a = new StringBuilder();
                                for(String validScenario : validScenarios) {
                                    a.append(" §f|§7 ").append(validScenario);
                                }

                                player.sendMessage(plugin.prefix + "§cInvalid scenario.\n§bValid Scenarios:\n" + a.toString().trim());
                                return false;
                            }
                            plugin.scenarios.add(args[1].toLowerCase());

                            player.sendMessage(plugin.prefix + "§fSuccessfully added §a" + args[1] + "§f to the game scenarios");
                        } else if (args[0].equalsIgnoreCase("remove")) {
                            if (!plugin.scenarios.contains(args[1])) {
                                player.sendMessage(plugin.prefix + "§cUnknown scenario.");
                                return false;
                            }
                            plugin.scenarios.remove(args[1]);

                            player.sendMessage(plugin.prefix + "§fSuccessfully removed §a" + args[1] + "§f from the game scenarios");
                        } else {
                            player.sendMessage(plugin.prefix + "§cWrong usage. Try /sc (add-remove) (scenarioName)");
                        }
                    } else {
                        player.sendMessage(plugin.prefix + "§cYou must be operator to do that.");
                    }
                } else {
                    player.sendMessage(plugin.prefix + "§cWrong usage. Try /sc");
                }
            }
        }
        return false;
    }
}
