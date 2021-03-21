package com.belmu.uhc.Core;

import org.bukkit.Bukkit;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class Options {

    public static int pvpSeconds = 20 * 60;
    public static int finalHealSeconds = pvpSeconds - 300;

    public static int borderSeconds = 45 * 60;
    public static int borderShrinkingSeconds = 60 * 60;
    public static int borderScale = (92 * Bukkit.getOnlinePlayers().size()) * 2;

    public static int monstersSpawn = pvpSeconds / 4;

    public static int pPerTeam = 2;

    public static int noCleanUpHearts = 2;

    public static int diamondLimit = 20;
    public static int heightLimit = 100;

    public static double untilGameStarts = 120;
    public static int beforeElimination = 5 * 60;

    public static String compassName = "§fSpectate §7(Right Click)";
    public static String teamChooserName = "§fChoose Team §7(Right Click)";
}
