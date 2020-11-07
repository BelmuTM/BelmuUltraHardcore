package com.belmu.uhc.Utils;

import com.belmu.uhc.Main;
import com.belmu.uhc.Teams.Teams;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.apache.commons.io.FileUtils;
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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UsefulMethods {

    public static void clearEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
    }

    public static boolean isOutsideOfBorder(Entity p) {

        WorldBorder border = p.getWorld().getWorldBorder();
        Location loc = p.getLocation();
        Location center = border.getCenter();

        double size = border.getSize() / 2;
        double x = loc.getX() - center.getX(), z = loc.getZ() - center.getZ();

        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    }

    public static void startUpdate(final Furnace tile, final int increase) {

        new BukkitRunnable() {
            public void run() {

                if (tile.getCookTime() > 0 || tile.getBurnTime() > 0) {
                    tile.setCookTime((short) (tile.getCookTime() + increase));
                    tile.update();

                } else this.cancel();

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

                    if(material == Material.CARPET || material == Material.STAINED_GLASS)
                        block.setData(DyeColor.RED.getData());
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

        World world = Bukkit.getWorld("world");
        world.dropItemNaturally(new Location(world, loc.getX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5), item);
    }

    public static Item dropCutClean(Location loc, ItemStack item) {

        World world = Bukkit.getWorld("world");
        return world.dropItemNaturally(new Location(world, loc.getX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5), item);
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

        ScoreboardManager m = Bukkit.getScoreboardManager();
        Scoreboard s = m.getMainScoreboard();

        World world = Bukkit.getWorld("world");

        if(Main.getMode().equalsIgnoreCase("Solo")) {

            for (int i = 0, total = Main.online.size(); i < total; i++) {

                Player p = Bukkit.getPlayer(Main.online.get(i));

                double angle = (2 * Math.PI * i) / total;
                int distance = Options.borderScale / 3;

                double x = Math.cos(angle) * distance;
                double z = Math.sin(angle) * distance;
                double y = Main.height;

                Location blockLoc_1 = new Location(world, x - 1, y, z + 1);
                Location blockLoc_2 = new Location(world, x + 1, y, z - 1);

                setBlocksRegion(blockLoc_1, blockLoc_2, Material.STAINED_GLASS);

                Location teleportLocation = new Location(world, x + 0.500D, y + 1.00D, z + 0.500D);
                Location chunkLocation =  new Location(world, x, y, z);

                tpLocation.put(p.getUniqueId(), teleportLocation);

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if(Main.game.contains("preparing")) {

                            Chunk chunk = world.getChunkAt(chunkLocation);
                            chunk.load();

                            if(chunk.isLoaded()) this.cancel();

                        } else this.cancel();
                    }
                }.runTaskTimer(Main.getInstance(), 20, 50);

            }

        } else if(Main.getMode().equalsIgnoreCase("Teams")) {

            for (int i = 0, total = Teams.inGameTeams.size(); i < total; i++) {

                Team team = s.getTeam(Teams.teams.get(i).teamName);

                double angle = (2 * Math.PI * i) / total;
                int distance = Options.borderScale / 3;

                double x = Math.cos(angle) * distance;
                double z = Math.sin(angle) * distance;
                double y = Main.height;

                Location blockLoc_1 = new Location(world, x - 1, y, z + 1);
                Location blockLoc_2 = new Location(world, x + 1, y, z - 1);

                setBlocksRegion(blockLoc_1, blockLoc_2, Material.STAINED_GLASS);

                Location teleportLocation = new Location(world, x + 0.500D, y + 1.00D, z + 0.500D);
                Location chunkLocation =  new Location(world, x, y, z);

                tpLocationTeams.put(team, teleportLocation);

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if(Main.game.contains("preparing")) {

                            Chunk chunk = world.getChunkAt(chunkLocation);
                            chunk.load();

                            if(chunk.isLoaded()) this.cancel();

                        } else this.cancel();

                    }
                }.runTaskTimer(Main.getInstance(), 20, 50);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void tp() {

        ScoreboardManager m = Bukkit.getScoreboardManager();
        Scoreboard s = m.getMainScoreboard();

        World world = Bukkit.getWorld("world");
        String errorChunk = "§cError: Couldn't load chunk at ";

        for (Player all : Bukkit.getOnlinePlayers()) {
            UUID uuid = all.getUniqueId();

            all.setHealth(20.0D);
            all.setFoodLevel(20);
            all.setExp(0.0F);
            all.setTotalExperience(0);
            all.setLevel(0);

            if(Main.getMode().equalsIgnoreCase("Solo")) {

                if (tpLocation.containsKey(uuid)) {

                    int x = tpLocation.get(uuid).getBlockX();
                    int z = tpLocation.get(uuid).getBlockZ();
                    Chunk chunk = world.getChunkAt(x, z);

                    if (chunk.isLoaded()) {
                        all.teleport(tpLocation.get(uuid));

                        EasyCountdown interval = new EasyCountdown(Main.getInstance(),
                                0.35D,
                                () -> Main.justTeleported = true
                        );
                        interval.scheduleTimer();

                    } else
                        Bukkit.broadcastMessage(Main.prefix + errorChunk + "§4X:" + chunk.getX() + " Z:" + chunk.getZ() + "§c.");
                }

            } else if(Main.getMode().equalsIgnoreCase("Teams")) {

                Team team = s.getPlayerTeam(all);

                if (tpLocationTeams.containsKey(team)) {

                    int x = tpLocationTeams.get(team).getBlockX();
                    int z = tpLocationTeams.get(team).getBlockZ();
                    Chunk chunk = world.getChunkAt(x, z);

                    if (chunk.isLoaded()) {
                        all.teleport(tpLocationTeams.get(team));

                        EasyCountdown interval = new EasyCountdown(Main.getInstance(),
                                0.35D,
                                () -> Main.justTeleported = true
                        );
                        interval.scheduleTimer();

                    } else
                        Bukkit.broadcastMessage(Main.prefix + errorChunk + "§4X:" + chunk.getX() + " Z:" + chunk.getZ() + "§c.");
                }
            }
        }
    }

    public static void breakPlatforms(Location loc1, Location loc2) {

        World world = Bukkit.getWorld("world");
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {

                    Block block = world.getBlockAt(x, y, z);
                    block.breakNaturally();
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void start() {
        World world = Bukkit.getWorld("world");

        for (Player all : Bukkit.getOnlinePlayers()) {

            if(Main.getMode().equalsIgnoreCase("Solo")) {
                UUID uuid = all.getUniqueId();

                int x = tpLocation.get(uuid).getBlockX();
                int z = tpLocation.get(uuid).getBlockZ();
                int y = Main.height;

                Location loc1 = new Location(world, x + 6, y + 6, z - 6);
                Location loc2 = new Location(world, x - 6, y - 6, z + 6);

                EasyCountdown stop = new EasyCountdown(Main.getInstance(),
                        Main.timer - 1,
                        () -> {
                            if(all.getLocation() != tpLocation.get(uuid))
                                all.teleport(tpLocation.get(uuid));
                        }
                );
                stop.scheduleTimer();
                yeet(loc1, loc2);

            } else if(Main.getMode().equalsIgnoreCase("Teams")) {
                ScoreboardManager m = Bukkit.getScoreboardManager();
                Scoreboard s = m.getMainScoreboard();

                Team team = s.getPlayerTeam(all);

                int x = tpLocationTeams.get(team).getBlockX();
                int z = tpLocationTeams.get(team).getBlockZ();
                int y = Main.height;

                Location loc1 = new Location(world, x + 6, y + 6, z - 6);
                Location loc2 = new Location(world, x - 6, y - 6, z + 6);

                EasyCountdown stop = new EasyCountdown(Main.getInstance(),
                        Main.timer - 1,
                        () -> {
                            if(all.getLocation() != tpLocationTeams.get(team))
                                all.teleport(tpLocationTeams.get(team));

                        }
                );
                stop.scheduleTimer();
                yeet(loc1, loc2);
            }
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

            if (rotation < 0)
                rotation += 360.0;

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

            } else
                return null;

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
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }

    public static Map<Player, Integer> sortedKills() {

        Map<Player, Integer> unsortedKills = new HashMap<>();

        for(String names : Main.players) {

            Player p = Bukkit.getPlayer(names);
            unsortedKills.put(p, Main.getInstance().getConfig().getInt("Players" + "." + p.getName() + ".kills"));
        }

        Map<Player, Integer> sortedKills = new HashMap<>();

        unsortedKills.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedKills.put(x.getKey(), x.getValue()));

        return sortedKills;
    }

   public static void deleteWorlds() {

        World world = Bukkit.getWorld("world");
        World nether = Bukkit.getWorld("world_nether");

        Bukkit.unloadWorld("world", false);
        Bukkit.unloadWorld("world_nether", false);

        try {
            FileUtils.deleteDirectory(new File(world.getWorldFolder().getPath()));
            FileUtils.deleteDirectory(new File(nether.getWorldFolder().getPath()));

        } catch (IOException exc) { exc.printStackTrace(); }

        Bukkit.getServer().shutdown();
    }

    public static void kickAll(String message) {
        for (Player all : Bukkit.getOnlinePlayers())
            all.kickPlayer(message);
    }

    public static void yeet(Location loc1, Location loc2) {

        EasyCountdown yeet = new EasyCountdown(Main.getInstance(),
                Main.timer,
                () -> {
                    breakPlatforms(loc1, loc2);

                    Main.justTeleported = false;
                    Main.fell = true;

                    EasyCountdown fell = new EasyCountdown(Main.getInstance(),
                            10,
                            () -> Main.fell = false
                    );
                    fell.scheduleTimer();
                }
        );
        yeet.scheduleTimer();
    }

}
