package com.belmu.uhc.Events;

import com.belmu.uhc.Main;
import com.belmu.uhc.Scenarios.Paranoïa;
import com.belmu.uhc.Teams.Teams;
import com.belmu.uhc.Utils.CountdownWithDouble;
import com.belmu.uhc.Utils.EasyCountdown;
import com.belmu.uhc.Utils.UsefulMethods;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.text.DecimalFormat;
import java.util.*;

public class PlayerDeath implements Listener {

    public static final Map<UUID, ItemStack[]> inv = new HashMap<>();
    public static final Map<UUID, ItemStack> armorInv = new HashMap<>();

    public static final Map<UUID, Team> playerTeam = new HashMap<>();

    public static int xLoc = 0;
    public static int yLoc = 0;
    public static int zLoc = 0;

    public static List<ItemStack[]> itemsInventory = new ArrayList<>();
    private static ItemStack[] itemStack =  itemsInventory.toArray(new ItemStack[itemsInventory.size()]);

    public static ItemStack helmet;
    public static ItemStack chestplate;
    public static ItemStack leggings;
    public static ItemStack boots;

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        FileConfiguration cfg = Main.getInstance().getConfig();

        Player target = e.getEntity();
        Player killer = target.getKiller();
        UUID uuid = target.getUniqueId();

        World world = target.getWorld();
        Location loc = target.getLocation();

        if(!Main.preparation || !Main.fell) {

            try {
                UsefulMethods.addKill(killer, 1);

            } catch (NullPointerException exc) {

                cfg.set("Players" + "." + killer.getName() + ".kills", 0);
                UsefulMethods.addKill(killer, 1);
            }

            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta headM = (SkullMeta) head.getItemMeta();

            headM.setDisplayName("§f" + target.getName() + "§7's head");
            headM.setOwner(target.getName());
            head.setItemMeta(headM);

            if (Main.scenarios.contains("goldenhead")) {
                if (!Main.scenarios.contains("timebomb")) {

                    Location headLoc = new Location(world, loc.getX() + 0.5, loc.getY() + 0.5, loc.getZ() + 0.5);
                    UsefulMethods.drop(headLoc, head);
                }
            }

            xLoc = target.getLocation().getBlockX();
            yLoc = target.getLocation().getBlockY();
            zLoc = target.getLocation().getBlockZ();

            ItemStack[] c = target.getInventory().getContents();
            itemsInventory.add(c);

            inv.put(uuid, itemStack);

            helmet = target.getInventory().getHelmet();
            chestplate = target.getInventory().getChestplate();
            leggings = target.getInventory().getLeggings();
            boots = target.getInventory().getBoots();

            armorInv.put(uuid, helmet);
            armorInv.put(uuid, chestplate);
            armorInv.put(uuid, leggings);
            armorInv.put(uuid, boots);

            Main.spectators.add(target.getName());
            Main.players.remove(target.getName());

            world.strikeLightningEffect(loc);

            for(Player all : Bukkit.getOnlinePlayers())
                all.playSound(all.getLocation(), Sound.AMBIENCE_THUNDER, 1.0f, Integer.MAX_VALUE);

            target.setGameMode(GameMode.SPECTATOR);
            target.sendMessage(Main.prefix + "§aYou are now spectator!");
            target.setHealth(20);
            target.setFoodLevel(20);

            target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2), true);
            target.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2), true);

            target.hidePlayer(target);

            target.spigot().setCollidesWithEntities(false);

            for(Player all : Bukkit.getOnlinePlayers())
                all.hidePlayer(target);

            CountdownWithDouble timer = new CountdownWithDouble(Main.getInstance(),
                    0.5D,
                    () -> {
                    },
                    () -> {
                        ItemStack spec = new ItemStack(Material.COMPASS, 1);
                        ItemMeta specM = spec.getItemMeta();

                        specM.setDisplayName("§fSpectate§7 (Right Click)");
                        spec.setItemMeta(specM);
                        target.getInventory().setItem(0, spec);
                        target.sendMessage(Main.prefix + "§7You got your spectator tool.");

                        target.setFireTicks(0);
                        target.setGameMode(GameMode.ADVENTURE);
                        target.setAllowFlight(true);
                        target.setFlying(true);

                        if(world.getBlockAt(target.getLocation()).getType() != Material.AIR) {
                            Location specLoc = new Location(world, loc.getBlockX() + 0.5, world.getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()), loc.getBlockZ() + 0.5);
                            target.teleport(specLoc);
                        }

                    },
                    (t) -> {
                    }
            );
            timer.scheduleTimer();

            String ti = "R.I.P.";
            String sub = "You are spectator";
            IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ti + "\",color:" + ChatColor.RED.name().toLowerCase() + ",\"bold\":true" + "}");
            PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);

            PacketPlayOutTitle length = new PacketPlayOutTitle(5, 60, 20);

            IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + sub + "\",color:" + ChatColor.WHITE.name().toLowerCase() + "}");
            PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);

            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(subtitle);
            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(length);

            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(title);
            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(length);

            DecimalFormat format = new DecimalFormat("#");

            double xU = loc.getX();
            double yU = loc.getY();
            double zU = loc.getZ();

            String x = format.format(xU);
            String y = format.format(yU);
            String z = format.format(zU);

            if (target.isOp()) {
                String opSpecName = "§7[S]§c[OP]§7 " + target.getName();

                target.setDisplayName(opSpecName);
                target.setPlayerListName(target.getDisplayName());

            } else {
                String pSpecName = "§7[S] " + target.getName();

                target.setDisplayName(pSpecName);
                target.setPlayerListName(target.getDisplayName());
            }

            ScoreboardManager m = Bukkit.getScoreboardManager();
            Scoreboard s = m.getMainScoreboard();

            String msg = e.getDeathMessage().replace(target.getName(), "§f" + target.getName() + "§f");
            String paranoia = " §fat §8[§7X: " + x + " Y: " + y + " Z: " + z + "§8]";

            if(Main.getMode().equalsIgnoreCase("Teams")) {

                Team team = s.getPlayerTeam(target);
                playerTeam.put(uuid, team);

                if (killer instanceof Player && target instanceof Player) {

                    String msg2 = msg.replace(killer.getName(), killer.getName() + "§f");
                    String finalMsg = msg2.replace(".", "");

                    if (Main.scenarios.contains("paranoia")) {

                        e.setDeathMessage("");
                        Paranoïa.sendBroadcast(Main.prefix + Paranoïa.prefix + "§f" + finalMsg + paranoia);

                    } else if (!Main.scenarios.contains("paranoia"))
                        e.setDeathMessage(Main.prefix + msg2);

                } else {
                    String finalMsg = msg.replace(".", "");

                    if (Main.scenarios.contains("paranoia")) {

                        e.setDeathMessage("");
                        Paranoïa.sendBroadcast(Main.prefix + Paranoïa.prefix + "§f" + finalMsg + paranoia);

                    } else if (!Main.scenarios.contains("paranoia"))
                        e.setDeathMessage(Main.prefix + "§f" + msg);
                }

                team.removePlayer(target);
                if(team.getPlayers().size() == 0) {

                    EasyCountdown eliminationTeam = new EasyCountdown(Main.getInstance(),
                            1D,
                            () -> {
                                Bukkit.broadcastMessage(Main.prefix + team.getPrefix() + team.getDisplayName() + "§f team has been eliminated!");
                                Teams.inGameTeams.remove(playerTeam.get(uuid));

                                for (Player all : Bukkit.getOnlinePlayers())
                                    all.playSound(all.getLocation(), Sound.WITHER_DEATH, 1, Integer.MAX_VALUE);
                            }
                    );
                    eliminationTeam.scheduleTimer();
                }

            } else if(Main.getMode().equalsIgnoreCase("Solo")) {

                if (killer instanceof Player && target instanceof Player) {

                    String msg2 = msg.replace(killer.getName(), killer.getName() + "§f");
                    String finalMsg = msg2.replace(".", "");

                    if (Main.scenarios.contains("paranoia")) {

                        e.setDeathMessage("");
                        Paranoïa.sendBroadcast(Main.prefix + Paranoïa.prefix + "§f" + finalMsg + paranoia);

                    } else if (!Main.scenarios.contains("paranoia"))
                        e.setDeathMessage(Main.prefix + msg2);

                } else {

                    String finalMsg = msg.replace(".", "");

                    if (Main.scenarios.contains("paranoia")) {

                        e.setDeathMessage("");
                        Paranoïa.sendBroadcast(Main.prefix + Paranoïa.prefix + "§f" + finalMsg + paranoia);

                    } else if (!Main.scenarios.contains("paranoia"))
                        e.setDeathMessage(Main.prefix + "§f" + msg);
                }
            }
        }
    }

}
