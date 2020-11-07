package com.belmu.uhc.Commands;

import com.belmu.uhc.Main;
import com.belmu.uhc.Events.PlayerDeath;
import com.belmu.uhc.Teams.Teams;
import com.belmu.uhc.Utils.UsefulMethods;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReviveCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("revive")) {

                if (player.isOp()) {

                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        World world = Bukkit.getWorld("world");

                        if (target != null) {

                            if (Main.spectators.contains(target.getName())) {

                                target.getInventory().clear();

                                Main.players.add(target.getName());
                                Main.spectators.remove(target.getName());

                                for (Player all : Bukkit.getOnlinePlayers())
                                    all.showPlayer(target);

                                target.showPlayer(target);
                                target.setFlying(false);
                                target.setAllowFlight(false);
                                target.setGameMode(GameMode.SURVIVAL);
                                target.setFireTicks(0);

                                target.spigot().setCollidesWithEntities(true);
                                UsefulMethods.clearEffects(target);

                                UUID uuid = target.getUniqueId();

                                if(Main.getMode().equalsIgnoreCase("Teams")) {

                                    if(PlayerDeath.playerTeam.containsKey(uuid)) {
                                        PlayerDeath.playerTeam.get(uuid).addPlayer(target);

                                        target.setDisplayName(PlayerDeath.playerTeam.get(uuid).getPrefix() + target.getName());
                                        target.setPlayerListName(PlayerDeath.playerTeam.get(uuid).getPrefix() + target.getName());

                                        if(!Teams.inGameTeams.contains(PlayerDeath.playerTeam.get(uuid)))
                                            Teams.inGameTeams.add(PlayerDeath.playerTeam.get(uuid));

                                    }
                                }

                                if (world.getBlockAt(PlayerDeath.xLoc, PlayerDeath.yLoc, PlayerDeath.zLoc).getType() == Material.LAVA || world.getBlockAt(PlayerDeath.xLoc, PlayerDeath.yLoc, PlayerDeath.zLoc).getType() == Material.STATIONARY_LAVA) {
                                    target.teleport(new Location(world, PlayerDeath.xLoc + 0.5, PlayerDeath.yLoc + 1.250, PlayerDeath.zLoc + 0.5));

                                    world.getBlockAt(PlayerDeath.xLoc, PlayerDeath.yLoc, PlayerDeath.zLoc).setType(Material.STONE);
                                    world.getBlockAt(PlayerDeath.xLoc - 1, PlayerDeath.yLoc, PlayerDeath.zLoc).setType(Material.STONE);
                                    world.getBlockAt(PlayerDeath.xLoc + 1, PlayerDeath.yLoc, PlayerDeath.zLoc).setType(Material.STONE);
                                    world.getBlockAt(PlayerDeath.xLoc, PlayerDeath.yLoc, PlayerDeath.zLoc - 1).setType(Material.STONE);
                                    world.getBlockAt(PlayerDeath.xLoc, PlayerDeath.yLoc, PlayerDeath.zLoc + 1).setType(Material.STONE);

                                } else
                                    target.teleport(new Location(world, PlayerDeath.xLoc + 0.5, PlayerDeath.yLoc + 1.250, PlayerDeath.zLoc + 0.5));

                                if (PlayerDeath.inv.containsKey(uuid)) {

                                    for (int i = 0; i < PlayerDeath.itemsInventory.size(); i++) {

                                        target.getInventory().setContents(PlayerDeath.itemsInventory.get(i));
                                        PlayerDeath.inv.remove(uuid);
                                    }
                                }

                                if (PlayerDeath.armorInv.containsKey(uuid)) {

                                    target.getInventory().setHelmet(PlayerDeath.helmet);
                                    target.getInventory().setChestplate(PlayerDeath.chestplate);
                                    target.getInventory().setLeggings(PlayerDeath.leggings);
                                    target.getInventory().setBoots(PlayerDeath.boots);

                                    PlayerDeath.armorInv.remove(uuid);
                                }
                                player.sendMessage(Main.prefix + "§aRevived §7" + target.getName() + "§a.");
                                target.sendMessage(Main.prefix + "§aYou have been revived by§7 " + player.getName() + "§a.");

                            } else if (!Main.spectators.contains(target.getName()))
                                player.sendMessage(Main.prefix + "§cThis player is alive.");

                        } else
                            player.sendMessage(Main.prefix + "§cUnknown player.");

                    } else
                        player.sendMessage(Main.prefix + "§cWrong usage. Try /revive (player).");

                } else
                    player.sendMessage(Main.prefix + "§cYou must be operator to do that.");

            }
        }

        return false;
    }

}