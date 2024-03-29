package com.belmu.uhc.Commands;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Events.PlayerDeath;
import com.belmu.uhc.Utility.Common;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class ReviveCommand implements CommandExecutor {

    public final UHC plugin;
    public ReviveCommand(UHC plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("revive")) {
                if (player.isOp()) {

                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);

                        if(target != null) {
                            if(!plugin.players.contains(target.getUniqueId()) && plugin.game.running) {
                                target.getInventory().clear();
                                plugin.players.add(target.getUniqueId());

                                for (Player all : Bukkit.getOnlinePlayers()) all.showPlayer(target);

                                target.showPlayer(target);
                                target.setFlying(false);
                                target.setAllowFlight(false);
                                target.setGameMode(GameMode.SURVIVAL);
                                target.setHealth(20);
                                target.setFoodLevel(20);
                                target.setFireTicks(0);

                                target.spigot().setCollidesWithEntities(true);
                                plugin.common.clearEffects(target);

                                UUID uuid = target.getUniqueId();
                                String name;

                                if(target.isOp()) name = "§c[OP]§7 " + target.getName();
                                else name = "§7" + target.getName();

                                if(plugin.getMode() == 1) {

                                    if(PlayerDeath.playerTeam.containsKey(uuid)) {
                                        PlayerDeath.playerTeam.get(uuid).addPlayer(target);

                                        name = PlayerDeath.playerTeam.get(uuid).getPrefix() + target.getName();

                                        if(!plugin.teams.inGameTeams.contains(PlayerDeath.playerTeam.get(uuid)))
                                            plugin.teams.inGameTeams.add(PlayerDeath.playerTeam.get(uuid));

                                        PlayerDeath.playerTeam.remove(uuid);
                                    }
                                }
                                target.setDisplayName(name);
                                target.setPlayerListName(name);

                                Location loc = PlayerDeath.deadLocations.get(uuid);
                                int x = (int) loc.getX();
                                int y = (int) loc.getY();
                                int z = (int) loc.getZ();

                                if (plugin.world.getBlockAt(x, y, z).getType() == Material.LAVA || plugin.world.getBlockAt(x, y, z).getType() == Material.STATIONARY_LAVA) {

                                    plugin.world.getBlockAt(x, y, z).setType(Material.GLASS);
                                    plugin.common.setBlocksRegionAsBorder(loc, 1, Material.GLASS);
                                }

                                if(plugin.world.getBlockAt(x, y + 1, z).getType() != Material.AIR)
                                    target.teleport(new Location(plugin.world, x + 0.5, plugin.world.getHighestBlockYAt(x, z), z + 0.5));
                                else
                                    target.teleport(new Location(plugin.world, x + 0.5, y + 1.25, z + 0.5));

                                if (PlayerDeath.inv.containsKey(uuid)) {

                                    target.getInventory().setContents(PlayerDeath.inv.get(uuid));
                                    PlayerDeath.inv.remove(uuid);
                                }

                                if (PlayerDeath.armorInv.containsKey(uuid)) {

                                    target.getInventory().setArmorContents(PlayerDeath.armorInv.get(uuid));
                                    PlayerDeath.armorInv.remove(uuid);
                                }
                                player.sendMessage(plugin.prefix + "§aRevived §7" + target.getName());
                                target.sendMessage(plugin.prefix + "§aYou have been revived by§7 " + player.getName());

                            } else {
                                player.sendMessage(plugin.prefix + "§cThis player is alive.");
                            }
                        } else {
                            player.sendMessage(plugin.prefix + "§cUnknown player.");
                        }
                    } else {
                        player.sendMessage(plugin.prefix + "§cWrong usage. Try /revive (player)");
                    }
                } else {
                    player.sendMessage(plugin.prefix + "§cYou must be operator to do that.");
                }
            }
        }
        return false;
    }
}
