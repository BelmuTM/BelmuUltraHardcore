package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CatEyes {

    public void catEyes() {

        if(Main.scenarios.contains("cateyes")) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                online.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 2));

            }

        }

    }

}
