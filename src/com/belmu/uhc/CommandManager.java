package com.belmu.uhc;

import com.belmu.uhc.Commands.*;
import org.bukkit.command.CommandExecutor;

public class CommandManager {

    public static void registerCommands() {

        cmd(new InfoCommand(), "info");
        cmd(new HealCommand(), "heal");
        cmd(new ReviveCommand(), "revive");
        cmd(new MenuCommand(), "menu");
        cmd(new SetHostCommand(), "sethost");
        cmd(new Start(), "start");
        cmd(new HideCommand(), "h");
        cmd(new ForcePvPCommand(), "forcepvp");
        cmd(new CoordinatesCommand(), "co");

    }

    public static void cmd(CommandExecutor executor, String cmdName) {

        Main.getInstance().getCommand(cmdName).setExecutor(executor);

    }
}
