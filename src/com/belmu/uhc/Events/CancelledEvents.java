package com.belmu.uhc.Events;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Core.Options;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.*;
import org.bukkit.block.Skull;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class CancelledEvents implements Listener {

    public final UHC plugin;
    public CancelledEvents(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onWeatherChange(WeatherChangeEvent event) {
        boolean rain = event.toWeatherState();
        if(rain) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onThunderChange(ThunderChangeEvent event) {
        boolean storm = event.toThunderState();
        if(storm) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent e) {
        Entity player = e.getEntity();

        if (player instanceof Player) {
            EntityRegainHealthEvent.RegainReason r = e.getRegainReason();

            if (r == EntityRegainHealthEvent.RegainReason.SATIATED || r == EntityRegainHealthEvent.RegainReason.EATING)
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBed(PlayerBedEnterEvent e) {
        UsefulMethods usefulMethods = new UsefulMethods(plugin);
        usefulMethods.sendPacket(e.getPlayer(), "§cYou can not sleep!");
        e.setCancelled(true);
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
            "tps"
    };

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        UsefulMethods usefulMethods = new UsefulMethods(plugin);
        Player player = e.getPlayer();

        if(cmdCancelled(player, e.getMessage())) {
            e.setCancelled(true);
            player.sendMessage(plugin.prefix + "§cYou must be operator to do that!");

        } else if (!cmdCancelled(player, e.getMessage())) {

            if(e.getMessage().equalsIgnoreCase("/reload") || e.getMessage().equalsIgnoreCase("/rl")) {
                String space = "                              ";
                usefulMethods.kickAll("§7§m" + space + "\n§cServer is reloading...\n" + "§7§m" + space);
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
        if(!isAble(player)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        UsefulMethods usefulMethods = new UsefulMethods(plugin);
        Player player = e.getPlayer();

        if(!isAble(player)) {
            e.setCancelled(true);
        } else {
            if (e.getBlock().getType() == Material.SKULL) {
                e.setCancelled(true);
                return;
            }

            if(e.getBlock().getY() > Options.heightLimit) {
                e.setCancelled(true);
                usefulMethods.sendPacket(player, "§cYou have reached the height limit! (§b" + Options.heightLimit + "§c)");
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if(!isAble(player)) e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        if(!isAble(player)) e.setCancelled(true);
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent e) {
        Entity player = e.getTarget();

        if(player instanceof Player) {
            if(!isAble((Player) player)) {
                e.setTarget(null);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {

        if(e.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) e.getEntity();
            if(plugin.game.fell || !isAble(player)) e.setCancelled(true);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        Entity damager = e.getDamager();
        Entity dmgd = e.getEntity();

        if(!plugin.game.running || plugin.game.teleported) e.setCancelled(true);

        if(damager instanceof Player) {
            if (!isAble((Player) damager)) {
                e.setCancelled(true);
                return;
            }
        }

        if(dmgd instanceof Player && damager instanceof Player) {
            if(!isAble((Player) damager) || !isAble((Player) dmgd)) {
                e.setCancelled(true);
                return;
            }

            if(plugin.getMode().equals("Teams")) {
                if(sb.getPlayerTeam((Player) damager) == sb.getPlayerTeam((Player) dmgd)) {

                    e.setCancelled(true);
                    damager.sendMessage(plugin.prefix + " §cYou can't hit your teammate!");
                }
            }
        }

        if(damager instanceof Arrow) {
            Arrow arrow = (Arrow) damager;
            if(arrow.getShooter() instanceof Player) {

                if(dmgd instanceof Player) {
                    DecimalFormat format = new DecimalFormat("#");
                    Double health = ((Player) dmgd).getHealth();
                    String output = format.format(health);

                    ((Player) arrow.getShooter()).sendMessage(plugin.prefix + "§7" + dmgd.getName() + "§f is now at§c " + output + "§4❤");
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) if(!isAble(player)) e.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(plugin.game.teleported)
            e.setTo(e.getFrom());
    }

    @EventHandler
    public void onPortal(PlayerChangedWorldEvent e) {
        UsefulMethods usefulMethods = new UsefulMethods(plugin);

        Player player = e.getPlayer();
        World world = player.getWorld();
        World overWorld = Bukkit.getWorld("world");

        if(world == overWorld) {
            if(usefulMethods.isOutsideOfBorder(player)) {

                int upperX = (int) overWorld.getWorldBorder().getSize();
                Random randomX = new Random();

                int upperZ = (int) overWorld.getWorldBorder().getSize();
                Random randomZ = new Random();

                int x = randomX.nextInt(upperX);
                int z = randomZ.nextInt(upperZ);
                int y = overWorld.getHighestBlockYAt(x, z);

                player.teleport(new Location(overWorld, x, y, z));
                player.sendMessage(plugin.prefix + "§7§oYou have been teleported to a random location because your nether portal was outside the border.");
            }
        }
    }

    @EventHandler
    public void onEnd(PlayerPortalEvent e) {
        if(e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) e.setCancelled(true);
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        Entity entity = e.getEntity();

        if(entity instanceof Monster) {
            Random r = new Random();
            if(r.nextInt(Options.monsterSpawnRate) == 1) e.setCancelled(true);
        }
    }

    public boolean isAble(Player player) {
        return plugin.game.running && !plugin.game.teleported && plugin.players.contains(player.getUniqueId());
    }

}
