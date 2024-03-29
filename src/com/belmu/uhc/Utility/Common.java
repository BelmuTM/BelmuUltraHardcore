package com.belmu.uhc.Utility;

import com.belmu.uhc.Core.Options;
import com.belmu.uhc.UHC;
import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class Common {

    public final UHC plugin;
    public Common(UHC plugin) {
        this.plugin = plugin;
    }

    public void clearEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
    }

    public boolean isOutsideOfBorder(Entity p) {
        WorldBorder border = p.getWorld().getWorldBorder();
        Location loc       = p.getLocation();
        Location center    = border.getCenter();

        double size = border.getSize() * 0.5;
        double x    = loc.getX() - center.getX(), z = loc.getZ() - center.getZ();

        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    }

    public void startUpdate(final Furnace tile, final int increase) {

        new BukkitRunnable() {
            public void run() {
                if(tile.getCookTime() > 0 || tile.getBurnTime() > 0) {
                    tile.setCookTime((short) (tile.getCookTime() + increase));
                    tile.update();

                } else this.cancel();

            }
        }.runTaskTimer(plugin, 1, 1);
    }

    public boolean consumeItem(Player player, int count, ItemStack mat) {
        Map<Integer, ? extends ItemStack> i = player.getInventory().all(mat);

        int found = 0;
        for(ItemStack stack : i.values()) found += stack.getAmount();
        if(count > found) return false;

        for(Integer index : i.keySet()) {
            ItemStack stack = i.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
                player.getInventory().setItem(index, null);
            else
                stack.setAmount(stack.getAmount() - removed);

            if (count <= 0) break;
        }
        player.updateInventory();
        return true;
    }

    @SuppressWarnings("deprecation")
    public ItemStack getSkull(String name) {

        ItemStack skull = new ItemStack(397, 1, (short) 3);
        SkullMeta skullmeta = (SkullMeta) skull.getItemMeta();

        skullmeta.setDisplayName(name);
        skullmeta.setOwner(name);
        skull.setItemMeta(skullmeta);

        return skull;
    }

    @SuppressWarnings("deprecation")
    public void setBlocksRegion(Location loc1, Location loc2, Material material) {

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

    public void setBlocksRegionAsBorder(Location center, int size, Material material) {
        int x = center.getBlockX();
        int y = center.getBlockY();
        int z = center.getBlockZ();

        Location loc3 = new Location(plugin.world, x + size, y, z - size);
        Location loc4 = new Location(plugin.world, x + size, y, z + size);
        setBlocksRegion(loc3, loc4, material);

        Location loc5 = new Location(plugin.world, x - size, y, z - size);
        Location loc6 = new Location(plugin.world, x - size, y, z + size);
        setBlocksRegion(loc5, loc6, material);

        Location loc7 = new Location(plugin.world, x - size, y, z - size);
        Location loc8 = new Location(plugin.world, x + size, y, z - size);
        setBlocksRegion(loc7, loc8, material);

        Location loc9 = new Location(plugin.world, x + size, y, z + size);
        Location loc10 = new Location(plugin.world, x - size, y, z + size);
        setBlocksRegion(loc9, loc10, material);
    }

    public void setBlock(Location loc, Material material) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        Block block = plugin.world.getBlockAt(x, y, z);
        block.setType(material);
    }

    public void drop(Location loc, ItemStack item) {
        plugin.world.dropItemNaturally(new Location(plugin.world, loc.getX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5), item);
    }

    public void dropApple(Player player, Block block) {

        if (player.getItemInHand().getType() != Material.SHEARS) {
            int upper = 7;
            Random random = new Random();

            if (random.nextInt(upper) == 1) {
                Location loc    = block.getLocation();
                ItemStack apple = new ItemStack(Material.APPLE, 1);

                drop(loc, apple);
            }
        }
    }

    public void dropTreeApple(Block block) {

        int upper = 16;
        Random random = new Random();
        if (random.nextInt(upper) == 1) {
            Location loc = block.getLocation();
            ItemStack apple = new ItemStack(Material.APPLE, 1);

            drop(loc, apple);
        }
    }

    public final Map<UUID, Location> tpLocation = new HashMap<>();
    public final Map<Team, Location> tpLocationTeams = new HashMap<>();

    public void prepareTp() {
        if(plugin.getMode() == 0) {

            for (int i = 0, total = plugin.players.size(); i < total; i++) {
                Player p = Bukkit.getPlayer(plugin.players.get(i));

                double angle = (2 * Math.PI * i) / total;
                int distance = Options.borderScale / 3;

                double x = Math.cos(angle) * distance;
                double z = Math.sin(angle) * distance;
                double y = plugin.height;

                Location blockLoc_1 = new Location(plugin.world, x - 1, y, z + 1);
                Location blockLoc_2 = new Location(plugin.world, x + 1, y, z - 1);

                setBlocksRegion(blockLoc_1, blockLoc_2, Material.STAINED_GLASS);

                Location teleportLocation = new Location(plugin.world, x + 0.5, y + 1.0, z + 0.5);

                Vector diff = new Location(plugin.world, 0, teleportLocation.getY(), 0).toVector().subtract(teleportLocation.toVector());
                diff.normalize();
                teleportLocation.setDirection(diff);

                tpLocation.put(p.getUniqueId(), teleportLocation);
            }

        } else if(plugin.getMode() == 1) {
            for (int i = 0, total = plugin.teams.inGameTeams.size(); i < total; i++) {
                Team team = plugin.teams.inGameTeams.get(i);

                double angle = (2 * Math.PI * i) / total;
                int distance = Options.borderScale / 3;

                double x = Math.cos(angle) * distance;
                double z = Math.sin(angle) * distance;
                double y = plugin.height;

                Location blockLoc_1 = new Location(plugin.world, x - 1, y, z + 1);
                Location blockLoc_2 = new Location(plugin.world, x + 1, y, z - 1);

                setBlocksRegion(blockLoc_1, blockLoc_2, Material.STAINED_GLASS);

                Location teleportLocation = new Location(plugin.world, x + 0.5, y + 1.0, z + 0.5);

                Vector diff = new Location(plugin.world, 0, teleportLocation.getY(), 0).toVector().subtract(teleportLocation.toVector());
                diff.normalize();
                teleportLocation.setDirection(diff);

                tpLocationTeams.put(team, teleportLocation);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void tp() {
        Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();

        for (Player all : Bukkit.getOnlinePlayers()) {
            UUID uuid = all.getUniqueId();

            all.setHealth(20.0D);
            all.setFoodLevel(20);
            all.setExp(0.0F);
            all.setTotalExperience(0);
            all.setLevel(0);

            if(plugin.getMode() == 0) {

                if (tpLocation.containsKey(uuid)) {
                    all.teleport(tpLocation.get(uuid));
                    plugin.game.teleported = true;
                }

            } else if(plugin.getMode() == 1) {
                Team team = s.getPlayerTeam(all);

                if (tpLocationTeams.containsKey(team)) {
                    all.teleport(tpLocationTeams.get(team));
                    plugin.game.teleported = true;
                }
            }
        }
    }

    public void breakPlatforms(Location loc1, Location loc2) {
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = plugin.world.getBlockAt(x, y, z);
                    block.breakNaturally();
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void start(double timer) {
        int size = 6;
        for (Player all : Bukkit.getOnlinePlayers()) {

            if(plugin.getMode() == 0) {
                UUID uuid = all.getUniqueId();

                int x = tpLocation.get(uuid).getBlockX();
                int z = tpLocation.get(uuid).getBlockZ();
                int y = plugin.height;

                Location loc1 = new Location(plugin.world, x + size, y + size, z - size);
                Location loc2 = new Location(plugin.world, x - size, y - size, z + size);

                EasyCountdown stop = new EasyCountdown(plugin,
                        plugin.timer - 1,
                        () -> {
                            if(all.getLocation() != tpLocation.get(uuid))
                                all.teleport(tpLocation.get(uuid));
                        }
                );
                stop.scheduleTimer();
                yeet(loc1, loc2, timer);

            } else if(plugin.getMode() == 1) {

                Team team = plugin.sc.getPlayerTeam(all);

                if (tpLocationTeams.containsKey(team)) {

                    int x = tpLocationTeams.get(team).getBlockX();
                    int z = tpLocationTeams.get(team).getBlockZ();
                    int y = plugin.height;

                    Location loc1 = new Location(plugin.world, x + size, y + size, z - size);
                    Location loc2 = new Location(plugin.world, x - size, y - size, z + size);

                    EasyCountdown stop = new EasyCountdown(plugin,
                            plugin.timer - 1,
                            () -> {
                                if (all.getLocation() != tpLocationTeams.get(team))
                                    all.teleport(tpLocationTeams.get(team));

                            }
                    );
                    stop.scheduleTimer();
                    yeet(loc1, loc2, timer);
                }
            }
        }
    }

    public String calculateRelativeDirectionFromPlayerTo(Player player, Location loc) {
        Location pLoc = player.getLocation();
        Vector vector = pLoc.toVector().subtract(loc.toVector());
        vector.normalize();

        double x = vector.getX();
        double z = vector.getZ();

        if(x != 0 && z != 0) {
            double theta = Math.atan2(-x, z);
            double TAU   = Math.PI * 2;

            float yaw      = (float) Math.toDegrees((theta + TAU) % TAU);
            float rotation = (pLoc.getYaw() - yaw) % 360;

            if (rotation < 0) rotation += 360.0;

            if (0 <= rotation && rotation < 22.5)
                return "⬇"; //N
            else if (22.5 <= rotation && rotation < 67.5)
                return "⬊"; //NE
            else if (67.5 <= rotation && rotation < 112.5)
                return "➡"; //E
            else if (112.5 <= rotation && rotation < 157.5)
                return "⬈"; //NE
            else if (157.5 <= rotation && rotation < 202.5)
                return "⬆"; //S
            else if (202.5 <= rotation && rotation < 247.5)
                return "⬉"; //SW
            else if (247.5 <= rotation && rotation < 292.5)
                return "⬅"; //W
            else if (292.5 <= rotation && rotation < 337.5)
                return "⬋"; //NW
            else if (337.5 <= rotation && rotation < 360.0)
                return "⬇"; //N
            else
                return "N/A";
        }
        return "N/A";
    }

    public void sendPacket(Player p, String text) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }

    public void deleteWorlds() {
        Bukkit.unloadWorld("world", false);
        Bukkit.unloadWorld("world_nether", false);

        try {
            FileUtils.deleteDirectory(new File(plugin.world.getWorldFolder().getPath()));
            FileUtils.deleteDirectory(new File(plugin.nether.getWorldFolder().getPath()));

        } catch (IOException exc) { exc.printStackTrace(); }
        Bukkit.getServer().shutdown();
    }

    public void kickAll(String message) {
        for (Player all : Bukkit.getOnlinePlayers())
            all.kickPlayer(message);
    }

    public void yeet(Location loc1, Location loc2, double timer) {

        EasyCountdown yeet = new EasyCountdown(plugin,
                timer,
                () -> {
                    breakPlatforms(loc1, loc2);

                    plugin.game.teleported = false;
                    plugin.game.fell       = true;

                    EasyCountdown fell = new EasyCountdown(plugin,
                            10,
                            () -> plugin.game.fell = false
                    );
                    fell.scheduleTimer();
                }
        );
        yeet.scheduleTimer();
    }

    public void addKills(Player player, int amount) {

        if(plugin.kills.get(player.getUniqueId()) != null)
            plugin.kills.put(player.getUniqueId(), plugin.kills.get(player.getUniqueId()) + amount);
        else plugin.kills.put(player.getUniqueId(), amount);
    }

    public int getKills(Player player) {
        if(plugin.kills.get(player.getUniqueId()) != null) return plugin.kills.get(player.getUniqueId());
        else return 0;
    }

    public LinkedHashMap<UUID, Integer> sortedKills(boolean natural) {
        LinkedHashMap<UUID, Integer> sorted = new LinkedHashMap<>();

        if(natural) {
            plugin.kills.entrySet()
                    .stream()
                    .sorted(java.util.Map.Entry.comparingByValue(Comparator.naturalOrder()))
                    .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
        } else {
            plugin.kills.entrySet()
                    .stream()
                    .sorted(java.util.Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
        }
        return sorted;
    }

    public String ordinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];

        }
    }

    public void sendWinMessage(String winner) {
        List players = new ArrayList(sortedKills(false).keySet());

        StringBuilder win = new StringBuilder();
        String na = "§7N/A";
        if(!players.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                String format;

                int n = i + 1;
                String ordinal = ordinal(n);
                String place = "";

                switch(n) {
                    case 1:
                        place = "§e§l" + ordinal + " Killer";
                        break;
                    case 2:
                        place = "§6§l" + ordinal + " Killer";
                        break;
                    case 3:
                        place = "§c§l" + ordinal + " Killer";
                        break;
                }

                if(i < players.size()) {
                    UUID uuid = UUID.fromString(players.get(i).toString());
                    OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
                    String pName = "§7§m" + p.getName() + "§r";

                    if(p.isOnline()) pName = "§7" + p.getName();

                    int kills = sortedKills(false).get(p.getUniqueId());
                    format = place + " " + pName + " §e- §a" + kills;
                } else {
                    format = place + " " + na + " §e- " + na;
                }
                win.append("§r      ").append(format).append("\n");
            }
            String name;

            switch(plugin.getMode()) {
                case 0:
                    name = "§c§lUHC Solo";
                    break;
                case 1:
                    name = "§c§lUHC Teams";
                    break;
                default:
                    name = "§c§lUHC";
                    break;
            }

            String line = "§7§m                                                   ";
            Bukkit.broadcastMessage(line + "\n" + "§r                 " + name + "§r" + "\n§r               §bWinner(s): " + winner + "\n§r \n" + win.toString().trim() + "\n§r \n" + line);

            for(Player all : Bukkit.getOnlinePlayers())
                all.playSound(all.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
        }
    }

    public void setSpectator(Player player) {
        plugin.players.remove(player.getUniqueId());
        player.getInventory().clear();

        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2), true);

        player.spigot().setCollidesWithEntities(false);
        player.setAllowFlight(true);
        player.setFlying(true);

        String specName;

        if(player.isOp()) specName = "§7[S]§c[OP]§7 " + player.getName();
        else specName = "§7[S] " + player.getName();

        player.setDisplayName(specName);
        player.setPlayerListName(specName);

        PacketPlayOutPlayerInfo tablistInfo = new PacketPlayOutPlayerInfo
                (PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle());

        player.hidePlayer(player);
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.hidePlayer(player);
            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(tablistInfo);
        }
    }

    public void giveCompass(Player player) {
        ItemStack spec = new ItemStack(Material.COMPASS, 1);
        ItemMeta specM = spec.getItemMeta();

        specM.setDisplayName(Options.compassName);
        spec.setItemMeta(specM);

        player.getInventory().setItem(0, spec);
    }

    public void giveTeamChooser(Player player) {
        ItemStack ch = new ItemStack(Material.EMERALD, 1);
        ItemMeta chM = ch.getItemMeta();

        chM.setDisplayName(Options.teamChooserName);
        ch.setItemMeta(chM);

        player.getInventory().setItem(0, ch);
    }

    public void gameChecks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!plugin.game.running) this.cancel();

                for(Player all : Bukkit.getOnlinePlayers()) {

                    if(!plugin.players.contains(all.getUniqueId()))
                        if (!all.getInventory().contains(Material.COMPASS)) giveCompass(all);
                }
            }
        }.runTaskTimer(plugin, 12, 12);
    }
}
