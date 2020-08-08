package com.belmu.uhc.Utils;

import com.belmu.uhc.Main;
import com.belmu.uhc.Teams.Teams;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class UsefulMethods {

    public static void clearEffects(Player player) {

        for (PotionEffect effect : player.getActivePotionEffects()) {

            player.removePotionEffect(effect.getType());

        }

    }

    public static boolean isOutsideOfBorder(Entity p) {

        Location loc = p.getLocation();
        WorldBorder border = p.getWorld().getWorldBorder();
        double size = border.getSize() / 2;
        Location center = border.getCenter();
        double x = loc.getX() - center.getX(), z = loc.getZ() - center.getZ();

        return ((x > size || (-x) > size) || (z > size || (-z) > size));

    }

    public static void startUpdate(final Furnace tile, final int increase) {

        new BukkitRunnable() {
            public void run() {

                if (tile.getCookTime() > 0 || tile.getBurnTime() > 0) {

                    tile.setCookTime((short) (tile.getCookTime() + increase));
                    tile.update();

                } else {

                    this.cancel();

                }

            }
        }.runTaskTimer(Main.getInstance(), 1L, 1L);

    }

    public static boolean consumeItem(Player player, int count, ItemStack mat) {
        Map<Integer, ? extends ItemStack> i = player.getInventory().all(mat);

        int found = 0;
        for (ItemStack stack : i.values())
            found += stack.getAmount();
        if (count > found)
            return false;

        for (Integer index : i.keySet()) {
            ItemStack stack = i.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
                player.getInventory().setItem(index, null);
            else
                stack.setAmount(stack.getAmount() - removed);

            if (count <= 0)
                break;
        }

        player.updateInventory();
        return true;

    }

    @SuppressWarnings("deprecation")
    public static ItemStack getSkull(String name) {

        ItemStack skull = new ItemStack(397, 1, (short) 3);
        SkullMeta skullmeta = (SkullMeta) skull.getItemMeta();

        skullmeta.setDisplayName(name);
        skullmeta.setOwner(name);
        skull.setItemMeta(skullmeta);

        return skull;

    }

    public static void addKill(Player p, int count) {

        FileConfiguration cfg = Main.getInstance().getConfig();

        int i = cfg.getInt("Players" + "." + p.getName() + ".kills");
        cfg.set("Players" + "." + p.getName() + "." + "kills", count + i);
        Main.getInstance().saveConfig();

    }

    public static ItemStack createItem(Material material, int count, String name, List<String> lore) {

        ItemStack item = new ItemStack(material, count);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;

    }

    public static ItemStack createItemWithData(Material material, int count, int data, String name, List<String> lore) {

        ItemStack item = new ItemStack(material, count, (byte) data);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;

    }

    @SuppressWarnings("deprecation")
    public static void setBlocksRegion(Location loc1, Location loc2, Material material) {

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {

            for (int y = minY; y <= maxY; y++) {

                for (int z = minZ; z <= maxZ; z++) {

                    Block block = Bukkit.getWorld("world").getBlockAt(x, y, z);
                    block.setType(material);

                    if(material == Material.CARPET || material == Material.STAINED_GLASS) {

                        block.setData(DyeColor.RED.getData());

                    }

                }

            }

        }

    }

    public static void setBlocksRegionAsBorder(Location center, int size, Material material) {

        World world = Bukkit.getWorld("world");

        int x = center.getBlockX();
        int y = center.getBlockY();
        int z = center.getBlockZ();

        Location loc3 = new Location(world, x + size, y, z - size);
        Location loc4 = new Location(world, x + size, y, z + size);

        UsefulMethods.setBlocksRegion(loc3, loc4, material);

        Location loc5 = new Location(world, x - size, y, z - size);
        Location loc6 = new Location(world, x - size, y, z + size);

        UsefulMethods.setBlocksRegion(loc5, loc6, material);

        Location loc7 = new Location(world, x - size, y, z - size);
        Location loc8 = new Location(world, x + size, y, z - size);

        UsefulMethods.setBlocksRegion(loc7, loc8, material);

        Location loc9 = new Location(world, x + size, y, z + size);
        Location loc10 = new Location(world, x - size, y, z + size);

        UsefulMethods.setBlocksRegion(loc9, loc10, material);

    }

    public static void setBlock(Location loc, Material material) {

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        Block block = Bukkit.getWorld("world").getBlockAt(x, y, z);
        block.setType(material);

    }

    public static void drop(Location loc, ItemStack item) {

        Bukkit.getWorld("world").dropItem(new Location(Bukkit.getWorld("world"), loc.getX() + 0.5000, loc.getBlockY() + 0.500, loc.getBlockZ() + 0.500), item);

    }

    public static Item dropCutClean(Location loc, ItemStack item) {

        return Bukkit.getWorld("world").dropItemNaturally(new Location(Bukkit.getWorld("world"), loc.getX() + 0.5000, loc.getBlockY() + 1, loc.getBlockZ() + 0.500), item);

    }

    public static void dropApple(Player player, Block block) {

        if (player.getItemInHand().getType() != Material.SHEARS) {

            int upper = 7;

            Random random = new Random();

            if (random.nextInt(upper) == 1) {

                Location loc = block.getLocation();
                ItemStack apple = new ItemStack(Material.APPLE, 1);

                UsefulMethods.drop(loc, apple);

            }
        }
    }

    public static void dropTreeApple(Block block) {

        int upper = 16;

        Random random = new Random();

        if (random.nextInt(upper) == 1) {

            Location loc = block.getLocation();
            ItemStack apple = new ItemStack(Material.APPLE, 1);

            UsefulMethods.drop(loc, apple);

        }

    }

    public static final Map<UUID, Location> tpLocation = new HashMap<>();
    public static final Map<Team, Location> tpLocationTeams = new HashMap<>();

    public static void prepareTp() {

        if(!Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

            for (int i = 0, total = Main.online.size(); i < total; i++) {

                Player p = Bukkit.getPlayer(Main.online.get(i));
                World world = Bukkit.getWorld("world");

                double angle = (2 * Math.PI * i) / total;
                int distance = Options.borderScale / 3;

                double x = Math.cos(angle) * distance;
                double z = Math.sin(angle) * distance;
                double y = Main.height;

                Location blockLoc_1 = new Location(world, x - 1, y, z + 1);
                Location blockLoc_2 = new Location(world, x + 1, y, z - 1);

                setBlocksRegion(blockLoc_1, blockLoc_2, Material.STAINED_GLASS);

                Location teleportLocation = new Location(world, x + 0.500D, y + 1.00D, z + 0.500D);

                tpLocation.put(p.getUniqueId(), teleportLocation);

                int xL = (int) teleportLocation.getX();
                int zL = (int) teleportLocation.getZ();

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if(Main.preparation.contains(true) && Main.justTeleported.contains(true)) {

                            world.getChunkAt(xL, zL).load();

                        } else if(!Main.preparation.contains(true) && !Main.justTeleported.contains(true)) {

                            this.cancel();

                        }

                    }
                }.runTaskTimer(Main.getInstance(), 0, 20);

            }

        } else if(Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

            ScoreboardManager m = Bukkit.getScoreboardManager();
            Scoreboard s = m.getMainScoreboard();

            for (int i = 0, total = Teams.inGameTeams.size(); i < total; i++) {

                Team team = s.getTeam(Teams.teams.get(i).teamName);

                World world = Bukkit.getWorld("world");

                double angle = (2 * Math.PI * i) / total;
                int distance = Options.borderScale / 3;

                double x = Math.cos(angle) * distance;
                double z = Math.sin(angle) * distance;
                double y = Main.height;

                Location blockLoc_1 = new Location(world, x - 1, y, z + 1);
                Location blockLoc_2 = new Location(world, x + 1, y, z - 1);

                setBlocksRegion(blockLoc_1, blockLoc_2, Material.STAINED_GLASS);

                Location teleportLocation = new Location(world, x + 0.500D, y + 1.00D, z + 0.500D);

                tpLocationTeams.put(team, teleportLocation);

                int xL = (int) teleportLocation.getX();
                int zL = (int) teleportLocation.getZ();

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if(Main.preparation.contains(true) && Main.justTeleported.contains(true)) {

                            world.getChunkAt(xL, zL).load();

                        } else if(!Main.preparation.contains(true) && !Main.justTeleported.contains(true)) {

                            this.cancel();

                        }

                    }
                }.runTaskTimer(Main.getInstance(), 0, 20);

            }

        }

    }

    public static void tp() {

        for (Player p : Bukkit.getOnlinePlayers()) {

            World world = Bukkit.getWorld("world");

            UUID uuid = p.getUniqueId();

            p.setHealth(20.0D);
            p.setFoodLevel(20);
            p.setExp(0.0F);
            p.setTotalExperience(0);
            p.setLevel(0);

            if(!Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                if (tpLocation.containsKey(uuid)) {

                    int x = tpLocation.get(uuid).getBlockX();
                    int z = tpLocation.get(uuid).getBlockZ();

                    if (world.getChunkAt(x, z).isLoaded()) {

                        p.teleport(tpLocation.get(uuid));

                        EasyCountdown interval = new EasyCountdown(Main.getInstance(),
                                0.35D,
                                () -> Main.justTeleported.add(true)

                        );
                        interval.scheduleTimer();

                    }

                }

            } else if(Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                ScoreboardManager m = Bukkit.getScoreboardManager();
                Scoreboard s = m.getMainScoreboard();

                Team team = s.getPlayerTeam(p);

                if (tpLocationTeams.containsKey(team)) {

                    int x = tpLocationTeams.get(team).getBlockX();
                    int z = tpLocationTeams.get(team).getBlockZ();

                    if (world.getChunkAt(x, z).isLoaded()) {

                        p.teleport(tpLocationTeams.get(team));

                        EasyCountdown interval = new EasyCountdown(Main.getInstance(),
                                0.35D,
                                () -> Main.justTeleported.add(true)

                        );
                        interval.scheduleTimer();

                    }

                }

            }

        }

    }

    public static void breakPlatforms(Location loc1, Location loc2) {

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {

            for (int y = minY; y <= maxY; y++) {

                for (int z = minZ; z <= maxZ; z++) {

                    Block block = Bukkit.getWorld("world").getBlockAt(x, y, z);
                    block.breakNaturally();

                }
            }
        }
    }

    public static void start() {

        for (Player p : Bukkit.getOnlinePlayers()) {

            World world = Bukkit.getWorld("world");

            if(!Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                UUID uuid = p.getUniqueId();

                int x = tpLocation.get(uuid).getBlockX();
                int z = tpLocation.get(uuid).getBlockZ();
                int y = Main.height;

                Location loc1 = new Location(world, x + 6, y + 6, z - 6);
                Location loc2 = new Location(world, x - 6, y - 6, z + 6);

                EasyCountdown stop = new EasyCountdown(Main.getInstance(),
                        Main.timer - 1,
                        () -> {

                            if(p.getLocation() != tpLocation.get(uuid)) {

                                p.teleport(tpLocation.get(uuid));

                            }

                        }
                );
                stop.scheduleTimer();

                EasyCountdown yeet = new EasyCountdown(Main.getInstance(),
                        Main.timer,
                        () -> {
                            breakPlatforms(loc1, loc2);

                            Main.justTeleported.remove(true);
                            Main.justTeleported.add(false);

                            Main.fell.add(true);
                            EasyCountdown fell = new EasyCountdown(Main.getInstance(),
                                    10,
                                    () -> {

                                        Main.fell.remove(true);
                                        Main.fell.add(false);

                                    }
                            );
                            fell.scheduleTimer();
                        }
                );
                yeet.scheduleTimer();

            } else if(Main.getInstance().getConfig().get("UHC" + "." + "Mode").equals("Teams")) {

                ScoreboardManager m = Bukkit.getScoreboardManager();
                Scoreboard s = m.getMainScoreboard();

                Team team = s.getPlayerTeam(p);

                int x = tpLocationTeams.get(team).getBlockX();
                int z = tpLocationTeams.get(team).getBlockZ();
                int y = Main.height;

                Location loc1 = new Location(world, x + 6, y + 6, z - 6);
                Location loc2 = new Location(world, x - 6, y - 6, z + 6);

                EasyCountdown stop = new EasyCountdown(Main.getInstance(),
                        Main.timer - 1,
                        () -> {

                            if(p.getLocation() != tpLocationTeams.get(team)) {

                                p.teleport(tpLocationTeams.get(team));

                            }

                        }
                );
                stop.scheduleTimer();

                EasyCountdown yeet = new EasyCountdown(Main.getInstance(),
                        Main.timer,
                        () -> {
                            breakPlatforms(loc1, loc2);

                            Main.justTeleported.remove(true);
                            Main.justTeleported.add(false);

                            Main.fell.add(true);
                            EasyCountdown fell = new EasyCountdown(Main.getInstance(),
                                    10,
                                    () -> {

                                        Main.fell.remove(true);
                                        Main.fell.add(false);

                                    }
                            );
                            fell.scheduleTimer();
                        }
                );
                yeet.scheduleTimer();

            }

        }

    }

    public static void sendToServer(JavaPlugin plugin, Player player, String servername){

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(byteOutput);

        try {

            output.writeUTF("Connect");
            output.writeUTF(servername);

            player.sendPluginMessage(plugin, "BungeeCord", byteOutput.toByteArray());

            output.close();

            player.sendMessage("\n \n \n \n ");

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public static String calculateRelativeDirectionFromPlayerTo(Player player, Location loc) {

        Location pLoc = player.getLocation();

        Vector vector = pLoc.toVector().subtract(loc.toVector());
        vector.normalize();

        double x = vector.getX();
        double z = vector.getZ();

        if(x != 0 && z != 0) {

            double theta = Math.atan2(-x, z);
            double dPi = Math.PI * 2;

            float yaw = (float) Math.toDegrees((theta + dPi) % dPi);
            float rotation = (pLoc.getYaw() - yaw) % 360;

            if (rotation < 0) {

                rotation += 360.0;

            }

            if (0 <= rotation && rotation < 22.5) {

                return "⬇"; //N

            } else if (22.5 <= rotation && rotation < 67.5) {

                return "⬊"; //NE

            } else if (67.5 <= rotation && rotation < 112.5) {

                return "➡"; //E

            } else if (112.5 <= rotation && rotation < 157.5) {

                return "⬈"; //NE

            } else if (157.5 <= rotation && rotation < 202.5) {

                return "⬆"; //S

            } else if (202.5 <= rotation && rotation < 247.5) {

                return "⬉"; //SW

            } else if (247.5 <= rotation && rotation < 292.5) {

                return "⬅"; //W

            } else if (292.5 <= rotation && rotation < 337.5) {

                return "⬋"; //NW

            } else if (337.5 <= rotation && rotation < 360.0) {

                return "⬇"; //N

            } else {

                return null;

            }

        }

        return "None";

    }

    public static int getKills(Player player) {

        FileConfiguration cfg = Main.getInstance().getConfig();

        if(cfg.get("Players" + "." + player.getName() + ".kills") != null) {

            return cfg.getInt("Players" + "." + player.getName() + ".kills");

        } else if(cfg.get("Players" + "." + player.getName() + ".kills") == null) {

            cfg.set("Players" + "." + player.getName() + ".kills", 0);
            return cfg.getInt("Players" + "." + player.getName() + ".kills");

        }

        return 0;
    }

    public static void sendPacket(Player p, String text) {















































        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);

    }

}
