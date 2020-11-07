package com.belmu.uhc.Events;

import com.belmu.uhc.Main;
import com.belmu.uhc.Scoreboard.GameScoreboard;
import com.belmu.uhc.Teams.Teams;
import com.belmu.uhc.Utils.UsefulMethods;
import fr.minuskube.netherboard.Netherboard;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;

public class PlayerJoin implements Listener {

    private int titlechanged = 0;

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();
        FileConfiguration cfg = Main.getInstance().getConfig();

        Main.online.add(player.getUniqueId());

        Netherboard.instance().removeBoard(player);

        if(Main.startTeams) {

            ItemStack ch = new ItemStack(Material.MELON, 1);
            ItemMeta chM = ch.getItemMeta();

            chM.setDisplayName("§fChoose Team§7 (Right Click)");
            ch.setItemMeta(chM);

            player.getInventory().setItem(0, ch);
        }

        if(Main.game.contains("preparing")) {

            if(Main.getMode().equalsIgnoreCase("Teams")) {

                if (Main.getTeamPicking().equalsIgnoreCase("Normal")) {

                    ItemStack ch = new ItemStack(Material.MELON, 1);
                    ItemMeta chM = ch.getItemMeta();

                    chM.setDisplayName("§fChoose Team§7 (Right Click)");
                    ch.setItemMeta(chM);

                    if(!Teams.playersToSpread.contains(player.getUniqueId()))
                        Teams.playersToSpread.add(player.getUniqueId());

                    player.getInventory().setItem(0, ch);
                }
            }
        }

        if(Main.game.contains("running")) {

            GameScoreboard.createGameScoreboard(player);

            new BukkitRunnable() {

                @Override
                public void run() {

                    try {
                        GameScoreboard.updateGameScoreboard(player);

                    } catch (NullPointerException exc) {
                        Bukkit.broadcastMessage(Main.prefix + "§cError: Troubles occurred when loading scoreboard...");
                    }

                }
            }.runTaskTimer(Main.getInstance(), 0, 10);

        } else {
            Location loc = new Location(Bukkit.getWorld("world"), 0, Main.height + 1.500, 0);

            if(player.getLocation().getY() < Main.height)
                player.teleport(loc);

            GameScoreboard.createLobbyScoreboard(player);

            new BukkitRunnable() {

                @Override
                public void run() {

                    try {

                        if(Main.game.contains("running"))
                            this.cancel();

                        GameScoreboard.updateLobbyScoreboard(player);

                    } catch (NullPointerException exc) {
                        Bukkit.broadcastMessage(Main.prefix + "§cError: Troubles occurred when loading scoreboard...");
                    }

                }
            }.runTaskTimer(Main.getInstance(), 0, 10);

        }

        if(Main.inCooldown.contains(player.getUniqueId())) {

            if(!Main.players.contains(player.getName())) {

                Main.inCooldown.remove(player.getUniqueId());
                e.setJoinMessage(Main.prefix + player.getDisplayName() + "§7 has§a reconnected§7.");
            }

        } else if(!Main.inCooldown.contains(player.getUniqueId())) {

            String joinMsg = "§r§f joined the game";
            String playersSize = " §r§7(§c" + Bukkit.getOnlinePlayers().size() + "§7/§c" + Bukkit.getMaxPlayers() + "§7)";

            String finalJoinMsg = Main.prefix + player.getDisplayName() + joinMsg + playersSize;

            if (player.isOp()) {

                String opSpecName = "§7[S]§c[OP]§7 " + player.getName();

                if(!Main.spectators.contains(player.getName())) {

                    if(!Main.game.contains("running")) {

                        String opName = "§c[OP]§7 " + player.getName();

                        player.setDisplayName(opName);
                        player.setPlayerListName(player.getDisplayName());

                        e.setJoinMessage(finalJoinMsg);

                    } else if(Main.game.contains("running")) {

                        player.setDisplayName(opSpecName);
                        player.setPlayerListName(player.getDisplayName());

                        e.setJoinMessage(finalJoinMsg);
                    }

                } else if(Main.spectators.contains(player.getName())) {

                    player.setDisplayName(opSpecName);
                    player.setPlayerListName(player.getDisplayName());

                    e.setJoinMessage(finalJoinMsg);
                }

            } else {
                String pSpecName = "§7[S] " + player.getName();

                if(!Main.spectators.contains(player.getName())) {

                    if(!Main.game.contains("running")) {

                        String pName = "§7" + player.getName();
                        player.setDisplayName(pName);

                    } else if(Main.game.contains("running"))
                        player.setDisplayName(pSpecName);

                } else if(Main.spectators.contains(player.getName())) {

                    if(Main.game.contains("running"))
                        player.setDisplayName(pSpecName);
                }

                player.setPlayerListName(player.getDisplayName());
                e.setJoinMessage(finalJoinMsg);
            }

        }

        if (!Main.players.contains(player.getName())) {

            if (!Main.spectators.contains(player.getName())) {

                if (Main.game.contains("running")) {
                    setSpectator(player);

                } else {

                    UsefulMethods.clearEffects(player);
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    player.getInventory().clear();

                    for(ItemStack i : player.getInventory().getArmorContents())
                        i.setType(Material.AIR);
                }

            } else if (Main.spectators.contains(player.getName())) {

                setSpectator(player);
            }

        } else if (Main.game.contains(player.getName())) {

            org.bukkit.scoreboard.ScoreboardManager m = Bukkit.getScoreboardManager();
            Scoreboard s = m.getMainScoreboard();

            if(Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                for (Team t : s.getTeams()) {

                    for (OfflinePlayer p : t.getPlayers()) {

                        p.getPlayer().setDisplayName(t.getPrefix() + p.getPlayer().getName());
                        p.getPlayer().setPlayerListName(t.getPrefix() + p.getPlayer().getName());
                    }
                }
            }
        }

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        new BukkitRunnable() {

            @Override
            public void run() {

                try {
                    Field a = packet.getClass().getDeclaredField("a");
                    a.setAccessible(true);
                    Field b = packet.getClass().getDeclaredField("b");
                    b.setAccessible(true);

                    Object header1 = new ChatComponentText("§c§lUHC\n§7  Good Luck!");
                    Object header2 = new ChatComponentText("§c§lUHC\n§f  Good Luck!");
                    Object header3 = new ChatComponentText("§4§lUHC\n§7  Good Luck!");
                    Object header4 = new ChatComponentText("§4§lUHC\n§f  Good Luck!");
                    Object header5 = new ChatComponentText("§c§lU§4§lHC\n§7  Good Luck!");
                    Object header6 = new ChatComponentText("§4§lU§c§lH§4§lC\n§f  Good Luck!");
                    Object header7 = new ChatComponentText("§4§lUH§c§lC\n§7  Good Luck!");

                    Object footer = new ChatComponentText("§a" + ((CraftPlayer) player).getHandle().ping + "ms §7| §b@Belmu_");
                    if (titlechanged == 0) {
                        a.set(packet, header1);
                    } else if (titlechanged == 1) {
                        a.set(packet, header2);
                    } else if (titlechanged == 2) {
                        a.set(packet, header3);
                    } else if (titlechanged == 3) {
                        a.set(packet, header4);
                    } else if (titlechanged == 4) {
                        a.set(packet, header5);
                    } else if (titlechanged == 5) {
                        a.set(packet, header6);
                    } else if (titlechanged == 6) {
                        a.set(packet, header7);
                    }
                    titlechanged += 1;
                    if(titlechanged == 7) titlechanged = 0;

                    b.set(packet, footer);

                    if (Bukkit.getOnlinePlayers().size() == 0) return;
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }.runTaskTimer(Main.getInstance(), 0, 15);

        if(!(cfg.contains(player.getName()))) {

            cfg.set("Players", player.getName());
            Main.getInstance().saveConfig();
        }

        if(!Main.game.contains("running")) {
            World world = Bukkit.getWorld("world");

            player.teleport(new Location(world, 0, world.getHighestBlockYAt(0, 0) + 2.5, 0));
            player.setGameMode(GameMode.ADVENTURE);

            for(ItemStack i : player.getInventory().getArmorContents())
                i.setType(Material.AIR);
        }
    }

    public void setSpectator(Player player) {

        if(!Main.spectators.contains(player.getName()))
            Main.spectators.add(player.getName());

        player.getInventory().clear();
        player.setHealth(20);
        player.setGameMode(GameMode.ADVENTURE);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2), true);

        player.setAllowFlight(true);
        player.setFlying(true);

        player.hidePlayer(player);

        for(Player all : Bukkit.getOnlinePlayers())
            all.hidePlayer(player);

        ItemStack spec = new ItemStack(Material.COMPASS, 1);
        ItemMeta specM = spec.getItemMeta();

        specM.setDisplayName("§fSpectate§7 (Right Click)");
        spec.setItemMeta(specM);

        player.getInventory().setItem(0, spec);
        player.spigot().setCollidesWithEntities(false);
    }

}
