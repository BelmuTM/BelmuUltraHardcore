package com.belmu.uhc.Utils;

import com.belmu.uhc.Main;

public class Options {

    public static int pvpSeconds = 20 * 60; // minutes * 60 = nombre de secondes
    public static int finalHealSeconds = pvpSeconds - 300;

    public static int borderSeconds = 45 * 60;// minutes * 60 = nombre de secondes
    public static int borderShrinkingSeconds = 60 * 60;// minutes * 60 = nombre de secondes
    public static int borderScale = (92 * Main.online.size()) * 2;

    public static int monstersSpawn = pvpSeconds / 4;

    public static int pPerTeam = 2;

    public static int noCleanUpHearts = 2;

    public static int diamondLimit = 17;

}
