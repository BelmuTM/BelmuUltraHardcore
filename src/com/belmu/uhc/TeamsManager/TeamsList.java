package com.belmu.uhc.TeamsManager;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public enum TeamsList {

    BLUE("blue", "§9✽ ", ChatColor.BLUE, DyeColor.BLUE),
    RED("red", "§c❤ ", ChatColor.RED, DyeColor.RED),
    GREEN("green", "§a✿ ", ChatColor.GREEN, DyeColor.GREEN),
    PURPLE("purple", "§5✪ ", ChatColor.DARK_PURPLE, DyeColor.PURPLE),
    PINK("pink", "§d⚔ ", ChatColor.LIGHT_PURPLE, DyeColor.PINK),
    ORANGE("orange", "§6☯ ", ChatColor.GOLD, DyeColor.ORANGE),
    YELLOW("yellow", "§e☼ ", ChatColor.YELLOW, DyeColor.YELLOW),
    WHITE("white", "§f☢ ", ChatColor.WHITE, DyeColor.WHITE),
    GRAY("gray", "§7♣ ", ChatColor.DARK_GRAY, DyeColor.GRAY),
    BLACK("black", "§0◆ ", ChatColor.BLACK, DyeColor.BLACK);

    public String teamName, prefix;
    public ChatColor teamColor;
    public DyeColor teamDyeColor;

    TeamsList(String teamName, String prefix, ChatColor teamColor, DyeColor teamDyeColor){

        this.teamName = teamName;
        this.prefix = prefix;
        this.teamColor = teamColor;
        this.teamDyeColor = teamDyeColor;
    }

}
