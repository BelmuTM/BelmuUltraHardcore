package com.belmu.uhc.Teams;

import com.belmu.uhc.Main;
import com.belmu.uhc.Utils.Options;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TeamChooser implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Inventory inv = e.getClickedInventory();
        ItemStack cur = e.getCurrentItem();
        HumanEntity p = e.getWhoClicked();

        if (inv == null) return;
        if (cur == null) return;

        if(p instanceof Player) {

            Player player = (Player) p;

            if (e.getInventory().getName().equals("Teams")) {

                if (!cur.hasItemMeta()) return;
                e.setCancelled(true);

                if (cur.getType() == Material.BANNER) {

                    BannerMeta banner = (BannerMeta) cur.getItemMeta();

                    for (TeamsList team : TeamsList.values()) {

                        if (banner.getBaseColor() == team.teamDyeColor) {

                            ScoreboardManager m = Bukkit.getScoreboardManager();
                            Scoreboard s = m.getMainScoreboard();

                            Team chosenTeam = s.getTeam(team.teamName);

                            if(chosenTeam.getPlayers() != null) {

                                if (chosenTeam.getPlayers().contains(player)) {

                                    player.sendMessage(Main.prefix + "§cYou are already in this team!");
                                    player.closeInventory();

                                    Inventory inven = teamChooser();
                                    player.openInventory(inven);

                                    return;
                                }

                                if (chosenTeam.getPlayers().size() >= Options.pPerTeam) {

                                    player.sendMessage(Main.prefix + "§cThis team is full!");
                                    player.closeInventory();

                                    Inventory inven = teamChooser();
                                    player.openInventory(inven);

                                    return;
                                }

                            }

                            chosenTeam.addPlayer(player);


                            if(Teams.playersToSpread.contains(player.getUniqueId())) {

                                Teams.playersToSpread.remove(player.getUniqueId());

                            }

                            player.setDisplayName(chosenTeam.getPrefix() + player.getName());
                            player.setPlayerListName(chosenTeam.getPrefix() + player.getName());

                            if (!Teams.inGameTeams.contains(chosenTeam)) {

                                Teams.inGameTeams.add(chosenTeam);

                            }

                            player.sendMessage(Main.prefix + "§7Successfully added you to " + chosenTeam.getPrefix() + chosenTeam.getName() + "§7.");
                            player.closeInventory();

                            Inventory inven = teamChooser();
                            player.openInventory(inven);

                        }

                    }

                } else if (cur.getType() == Material.BARRIER) {

                    player.closeInventory();

                }

            }

        }

    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {

        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack it = e.getItem();

        if (it == null) return;

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {

            if (it.hasItemMeta()) {

                if (it.getItemMeta().hasDisplayName()) {

                    if (it.getItemMeta().getDisplayName().equalsIgnoreCase("§fChoose Team§7 (Right Click)")) {

                        e.setCancelled(true);

                        Inventory inv = teamChooser();
                        player.openInventory(inv);

                    }

                }

            }

        }

    }

    public static List<Team> getPossibleTeams() {

        List<Team> possibleTeams = new ArrayList<>();

        int ppt = Options.pPerTeam;
        int players = Bukkit.getOnlinePlayers().size();
        int f = 0;

        if(players >= ppt) {

            f = players / ppt;

        } else if(players < ppt) {

            Options.pPerTeam = players;
            f = players;

        }

        DecimalFormat formatter = new DecimalFormat("#");
        String a = formatter.format(f);

        int finalNumber = Integer.parseInt(a);

        List<TeamsList> teamsList = new ArrayList<>();
        for(TeamsList teams : TeamsList.values()) {

            teamsList.add(teams);

        }

        for(int i = 0; i < finalNumber; i++) {

            Team team = Teams.getTeam(teamsList.get(i).teamName);

            possibleTeams.add(team);

        }

        return possibleTeams;

    }

    public static Inventory teamChooser() {

        Inventory inv = Bukkit.createInventory(null, 27, "Teams");

        for (int i = 0; i < getPossibleTeams().size(); i++) {

            List<TeamsList> teamsList = new ArrayList<>();
            for (TeamsList teams : TeamsList.values()) {

                teamsList.add(teams);

            }

            ScoreboardManager m = Bukkit.getScoreboardManager();
            Scoreboard s = m.getMainScoreboard();

            TeamsList team = teamsList.get(i);

            ItemStack ch = new ItemStack(Material.BANNER, 1);
            ItemMeta chM = ch.getItemMeta();

            Team scoreboardTeam = s.getTeam(team.teamName);

            BannerMeta meta = (BannerMeta) chM;
            meta.setBaseColor(team.teamDyeColor);

            try {

                chM.setDisplayName(team.teamColor + team.teamName + "§7 (" + scoreboardTeam.getPlayers().size() + "/" + Options.pPerTeam + ")");

            } catch (NullPointerException npe) {

                chM.setDisplayName(team.teamColor + team.teamName + "§7 (" + 0 + "/" + Options.pPerTeam + ")");

            }

            List<String> lore = new ArrayList<>();

            if(scoreboardTeam.getPlayers() != null) {

                for (OfflinePlayer p : scoreboardTeam.getPlayers()) {

                    lore.add("§7» " + team.teamColor + p.getName() + "\n");

                }

            }

            chM.setLore(lore);

            ch.setItemMeta(chM);

            inv.setItem(i, ch);

        }

        ItemStack b = new ItemStack(Material.BARRIER, 1);
        ItemMeta bM = b.getItemMeta();

        bM.setDisplayName("§cBack");
        b.setItemMeta(bM);

        inv.setItem(22, b);

        return inv;

    }

}
