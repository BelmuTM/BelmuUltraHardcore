package com.belmu.uhc.Scenarios;

import com.belmu.uhc.Main;
import com.belmu.uhc.Utils.Options;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DiamondLimit implements Listener {

    public static Map<UUID, Integer> dLimit = new HashMap<>();

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {

        Player player = e.getPlayer();
        Block block = e.getBlock();
        World world = player.getWorld();

        if(Main.scenarios.contains("diamondlimit")) {

            if (player.getGameMode() == GameMode.SURVIVAL) {

                if (block.getType() == Material.DIAMOND_ORE) {

                    UUID uuid = player.getUniqueId();

                    if(DiamondLimit.dLimit.containsKey(uuid)) {
                        int a = DiamondLimit.dLimit.get(uuid);

                        if (a == Options.diamondLimit) {

                            e.setCancelled(true);

                            block.setType(Material.AIR);
                            UsefulMethods.sendPacket(player, "§cYou have reached your diamond limit! (§b" + Options.diamondLimit + "§c)");

                            ExperienceOrb orb = (ExperienceOrb) world.spawnEntity(block.getLocation(), EntityType.EXPERIENCE_ORB);
                            orb.setExperience(2);

                        } else if (a < Options.diamondLimit)
                            DiamondLimit.dLimit.put(uuid, a + 1);

                    } else if(!DiamondLimit.dLimit.containsKey(uuid))
                        DiamondLimit.dLimit.put(uuid, 1);
                }
            }
        }
    }

}
