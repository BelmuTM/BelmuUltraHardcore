package com.belmu.uhc.Events;

import com.belmu.uhc.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FoodEvent implements Listener {

    private final Map<UUID, Long> hunger = new HashMap<>();
    private final List<UUID> ate = new ArrayList<>();

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {

        Entity player = e.getEntity();

        if(player instanceof Player) {
            UUID uuid = player.getUniqueId();

            if (Main.game.contains("running")) {

                if (Main.justTeleported || Main.preparation || Main.fell) {
                    e.setCancelled(true);

                } else {

                    if (Main.players.contains(player.getName())) {

                        if (hunger.containsKey(uuid)) {

                            long a = 30 * 1000;

                            if (hunger.get(uuid) <= (System.currentTimeMillis() * 1000) - a) {

                                e.setCancelled(true);

                                ((Player) player).setFoodLevel(((Player) player).getFoodLevel() - (20 / 19));

                                hunger.remove(uuid);

                            } else
                                e.setCancelled(true);

                        } else {

                            if (!ate.contains(uuid)) {

                                e.setCancelled(true);
                                hunger.put(uuid, System.currentTimeMillis() * 1000);

                            } else
                                ate.remove(uuid);
                        }

                    } else if (!Main.players.contains(player.getName())) {

                        e.setCancelled(true);
                    }
                }

            } else
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack item = e.getItem();

        if(item.getType().isEdible()) {

            e.setCancelled(false);

            hunger.remove(player.getUniqueId());

            ate.add(uuid);
        }
    }

}
