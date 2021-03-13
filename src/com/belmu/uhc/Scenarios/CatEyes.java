package com.belmu.uhc.Scenarios;

import com.belmu.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class CatEyes {

    public final UHC plugin;
    public CatEyes(UHC plugin) {
        this.plugin = plugin;
    }

    public void catEyes() {

        if(plugin.scenarios.contains("cateyes")) {

            for (Player all : Bukkit.getOnlinePlayers())
                all.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 2));
        }
    }

}
