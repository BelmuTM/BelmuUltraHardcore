package com.belmu.uhc.Teams;

import org.bukkit.ChatColor;

public enum TeamsList {

    BLUE("blue", "§9✽ ", ChatColor.BLUE),
    RED("red", "§c❤ ", ChatColor.RED),
    GREEN("green", "§a✿ ", ChatColor.GREEN),
    PURPLE("purple", "§5✪ ", ChatColor.DARK_PURPLE),
    PINK("pink", "§d⚔ ", ChatColor.LIGHT_PURPLE),
    ORANGE("orange", "§6☯ ", ChatColor.GOLD),
    YELLOW("yellow", "§e☼ ", ChatColor.YELLOW),
    WHITE("white", "§f☢ ", ChatColor.WHITE),
    GRAY("gray", "§7♣ ", ChatColor.DARK_GRAY),
    BLACK("black", "§0◆ ", ChatColor.BLACK);

    public String teamName, prefix;

    public ChatColor teamColor;

    private TeamsList(String teamName, String prefix, ChatColor teamColor){

        this.teamName = teamName;
        this.prefix = prefix;
        this.teamColor = teamColor;

    }

}
