package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CatEyes {

    public void catEyes() {

        if(Main.scenarios.contains("cateyes")) {

            for (Player all : Bukkit.getOnlinePlayers())
                all.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 2));
        }
    }

}
