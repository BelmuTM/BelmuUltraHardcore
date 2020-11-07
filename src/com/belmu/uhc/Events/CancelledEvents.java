package com.belmu.uhc.Events;

import com.belmu.uhc.Main;
import com.belmu.uhc.Utils.Options;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.text.DecimalFormat;
import java.util.Random;

public class CancelledEvents implements Listener {

    @EventHandler(priority=EventPriority.HIGH)
    public void onWeatherChange(WeatherChangeEvent event) {
        boolean rain = event.toWeatherState();

        if(rain) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent e) {
        Entity player = e.getEntity();

        if(Main.game.contains("running")) {

            if(player instanceof Player) {

                EntityRegainHealthEvent.RegainReason r = e.getRegainReason();

                if (r == EntityRegainHealthEvent.RegainReason.SATIATED || r == EntityRegainHealthEvent.RegainReason.EATING)
                    e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority= EventPriority.HIGH)
    public void onThunderChange(ThunderChangeEvent event) {
        boolean storm = event.toThunderState();

        if(storm) event.setCancelled(true);
    }

    @EventHandler
    public void bed(PlayerBedEnterEvent e) {

        e.setCancelled(true);
        UsefulMethods.sendPacket(e.getPlayer(), "§cYou can not sleep!");
    }

    private String[] cmds = {
            "?",
            "help",
            "version",
            "icanhasbukkit",
            "about",
            "ver",
            "tell",
            "me",
            "tps",
    };

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        if(cmdCancelled(player, e.getMessage())) {
            e.setCancelled(true);
            player.sendMessage(Main.prefix + "§cYou must be operator to do that!");

        } else if (!cmdCancelled(player, e.getMessage())) {

            if(e.getMessage().equalsIgnoreCase("/reload") || e.getMessage().equalsIgnoreCase("/rl")) {

                UsefulMethods.kickAll("§7§m                              " + "\n§cServer is reloading...\n" + "§7§m                              ");
            }
        }
    }

    private boolean cmdCancelled(Player player, String message) {

        for(String cmd : cmds) {

            if (message.equalsIgnoreCase("/" + cmd) && !player.isOp()
                    || message.equalsIgnoreCase("/bukkit:" + cmd) && !player.isOp()
                    || message.equalsIgnoreCase("/minecraft:" + cmd) && !player.isOp()) {

                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        if(!Main.game.contains("running") || Main.spectators.contains(player.getName())) e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        if(!Main.game.contains("running") || Main.spectators.contains(player.getName())) {
            e.setCancelled(true);

        } else {

            if (e.getBlock().getType() == Material.SKULL) {
                e.setCancelled(true);
                return;
            }

            if(e.getBlock().getY() > 120) {

                e.setCancelled(true);
                UsefulMethods.sendPacket(player, "§cYou have reached the height limit! (§b120§c)");
            }
        }
    }

    @EventHandler
    public void entityDamage(EntityDamageEvent e) {
        Entity dmgd = e.getEntity();

        if(dmgd instanceof Player) {

            Player player = (Player) e.getEntity();

            if (!Main.game.contains("running") || Main.spectators.contains(player.getName())) e.setCancelled(true);
            if(Main.fell || Main.preparation || Main.justTeleported) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {

        Player player = e.getPlayer();
        if(!Main.players.contains(player.getName())) e.setCancelled(true);
    }

    @EventHandler
    public void target(EntityTargetLivingEntityEvent e) {
        Entity player = e.getTarget();

        if(player instanceof Player) {

            if (!Main.game.contains("running") || Main.spectators.contains(player.getName())) {

                e.setTarget(null);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void pickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();

        if(!Main.game.contains("running") || Main.spectators.contains(player.getName())) e.setCancelled(true);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void damage(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        Entity dmgd = e.getEntity();

        if (!Main.game.contains("running")) {
            e.setCancelled(true);
            return;
        }

        if (Main.spectators.contains(damager.getName())) {
            e.setCancelled(true);
            return;
        }

        if(dmgd instanceof Player && damager instanceof Player) {

            if(Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(Bukkit.getOfflinePlayer(dmgd.getUniqueId())) == Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(Bukkit.getOfflinePlayer(damager.getUniqueId()))) {

                e.setCancelled(true);
                damager.sendMessage(Main.prefix + "§c You can't hit your teammate!");
            }
        }

        if (damager instanceof Arrow) {

            Arrow arrow = (Arrow) damager;
            if (arrow.getShooter() instanceof Player) {

                if(dmgd instanceof Player) {

                    DecimalFormat format = new DecimalFormat("#");
                    Double health = ((Player) dmgd).getHealth();
                    String output = format.format(health);

                    ((Player) arrow.getShooter()).sendMessage(Main.prefix + "§7" + dmgd.getName() + "§f is now at§c " + output + "§4❤");

                }
            }
        }
    }

    @EventHandler
    public void onAchievement(PlayerAchievementAwardedEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onChunkUnload(ChunkUnloadEvent e) {
        Chunk chunk = e.getChunk();

        if(Main.preparation) {

            if(chunk.getX() > 0 && chunk.getX() < (Options.borderScale / 2) && chunk.getZ() > 0 && chunk.getZ() < (Options.borderScale / 2)) {

                if(chunk.getX() > 0 && chunk.getX() < -(Options.borderScale / 2) && chunk.getZ() > 0 && chunk.getZ() < -(Options.borderScale / 2)) {
                    chunk.load();
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if(Main.spectators.contains(player.getName())) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPortal(PlayerChangedWorldEvent e) {

        Player player = e.getPlayer();
        World world = player.getWorld();
        World overWorld = Bukkit.getWorld("world");

        if(world == overWorld) {

            if (UsefulMethods.isOutsideOfBorder(player)) {

                int upperX = (int) overWorld.getWorldBorder().getSize();
                Random randomX = new Random();

                int upperZ = (int) overWorld.getWorldBorder().getSize();
                Random randomZ = new Random();

                int x = randomX.nextInt(upperX);
                int z = randomZ.nextInt(upperZ);
                int y = overWorld.getHighestBlockYAt(x, z);

                player.teleport(new Location(overWorld, x, y, z));
                player.sendMessage(Main.prefix + "§7§oYou have been teleported to a random point cause your nether portal was outside the border.");
            }
        }
    }

    @EventHandler
    public void onEnd(PlayerPortalEvent e) {
        if(e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) e.setCancelled(true);
    }

}
