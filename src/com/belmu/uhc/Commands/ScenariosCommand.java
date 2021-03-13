package com.belmu.uhc.Commands;

import com.belmu.uhc.UHC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class ScenariosCommand implements CommandExecutor {

    public final UHC plugin;
    public ScenariosCommand(UHC plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, java.lang.String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("scenarios")) {
                if (args.length == 0) {

                    StringBuilder a = new StringBuilder();
                    for(int i = 0; i < plugin.scenarios.size(); i++) {

                        a.append(" §f|§7 ").append(plugin.scenarios.get(i));
                    }
                    player.sendMessage(plugin.prefix + "§bActive Scenarios§7: " + a.toString().trim());
                } else
                    player.sendMessage(plugin.prefix + "§cWrong usage. Try /sc");
            }
        }
        return false;
    }

}
