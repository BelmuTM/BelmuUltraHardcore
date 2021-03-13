package com.belmu.uhc.Core;

import com.belmu.uhc.Commands.*;
import com.belmu.uhc.UHC;
import org.bukkit.command.CommandExecutor;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class CommandManager {

    public final UHC plugin;
    public CommandManager(UHC plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {

        cmd(new InfoCommand(plugin), "info");
        cmd(new HealCommand(plugin), "heal");
        cmd(new ReviveCommand(plugin), "revive");
        cmd(new SetHostCommand(plugin), "sethost");
        cmd(new HideCommand(plugin), "h");
        cmd(new ForcePvPCommand(plugin), "forcepvp");
        cmd(new CoordinatesCommand(plugin), "co");
        cmd(new ScenariosCommand(plugin), "scenarios");
    }

    public void cmd(CommandExecutor executor, String cmdName) {
        plugin.getCommand(cmdName).setExecutor(executor);
    }
}
