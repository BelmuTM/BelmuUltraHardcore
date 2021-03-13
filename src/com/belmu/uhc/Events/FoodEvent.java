package com.belmu.uhc.Events;

import com.belmu.uhc.UHC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class FoodEvent implements Listener {

    public final UHC plugin;
    public FoodEvent(UHC plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, Long> hunger = new HashMap<>();
    private final List<UUID> ate = new ArrayList<>();

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        Entity player = e.getEntity();

        if(player instanceof Player) {
            UUID uuid = player.getUniqueId();

            if(plugin.game.running) {

                if(plugin.game.teleported || plugin.game.preparing || plugin.game.fell) {
                    e.setCancelled(true);

                } else {
                    if (plugin.players.contains(player.getUniqueId())) {
                        if (hunger.containsKey(uuid)) {
                            long a = 15 * 1000;

                            if (hunger.get(uuid) <= (System.currentTimeMillis()) - a) {

                                e.setCancelled(true);
                                ((Player) player).setFoodLevel(((Player) player).getFoodLevel() - (20 / 19));
                                hunger.remove(uuid);
                            } else e.setCancelled(true);

                        } else {
                            if (!ate.contains(uuid)) {
                                e.setCancelled(true);
                                hunger.put(uuid, System.currentTimeMillis() * 1000);
                            } else
                                ate.remove(uuid);
                        }
                    } else if (!plugin.players.contains(player.getUniqueId()))
                        e.setCancelled(true);
                }
            } else e.setCancelled(true);
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
