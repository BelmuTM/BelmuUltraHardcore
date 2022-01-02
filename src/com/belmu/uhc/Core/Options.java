package com.belmu.uhc.Core;

import org.bukkit.Bukkit;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class Options {

    public static final int pvpTime = 20 * 60;
    public static final int finalHealTime = pvpTime - 300;

    public static final int borderTime = 45 * 60;
    public static final int borderShrinkTime = 60 * 60;
    public static final int borderScale = (92 * Bukkit.getOnlinePlayers().size()) * 2;

    public static final int monstersSpawn = pvpTime / 4;
    public static final int monsterSpawnRate = 3; // 1 out of X

    public static int pPerTeam = 2;

    public static final int noCleanUpHearts = 2;

    public static final int diamondLimit = 20;
    public static final int heightLimit = 100;

    public static final double untilGameStarts = 120;
    public static final int beforeElimination = 5 * 60;

    public static final int kickTime = 60;
    public static final int timeBombTime = 140;

    public static final double hungerDelay = 32.5D;

    public static final String compassName     = "§fSpectate §7(Right Click)";
    public static final String teamChooserName = "§fChoose Team §7(Right Click)";
}
