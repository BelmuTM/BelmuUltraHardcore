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

    private final Map<UUID, Double> hunger = new HashMap<>();
    private final List<UUID> ate = new ArrayList<>();

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        Entity entity = e.getEntity();

        if(entity instanceof Player) {
            Player player = (Player) entity;
            UUID uuid = player.getUniqueId();

            if(plugin.game.running && !plugin.game.teleported) {

                if(plugin.players.contains(uuid)) {
                    if(checkCooldown(hunger, player)) {

                        setCooldown(hunger, player, 11.5);

                    } else e.setCancelled(true);
                } else e.setCancelled(true);
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

    public void setCooldown(Map<UUID, Double> cooldowns, Player player, double seconds){
        double delay = System.currentTimeMillis() + (seconds * 1000);
        cooldowns.put(player.getUniqueId(), delay);
    }

    public boolean checkCooldown(Map<UUID, Double> cooldowns, Player player){
        return !cooldowns.containsKey(player.getUniqueId()) || cooldowns.get(player.getUniqueId()) <= System.currentTimeMillis();
    }

}
