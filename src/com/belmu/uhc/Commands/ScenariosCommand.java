package com.belmu.uhc.Commands;

import com.belmu.uhc.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScenariosCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, java.lang.String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("scenarios")) {

                if (args.length == 0) {

                    StringBuilder a = new StringBuilder();
                    for(int i = 0; i < Main.scenarios.size(); i++) {

                        a.append(" §f|§7 " + Main.scenarios.get(i));
                    }
                    player.sendMessage(Main.prefix + "§bActive Scenarios§7: " + a.toString().trim());

                } else
                    player.sendMessage(Main.prefix + "§cWrong usage. Try /sc");
            }
        }

        return false;
    }

}
