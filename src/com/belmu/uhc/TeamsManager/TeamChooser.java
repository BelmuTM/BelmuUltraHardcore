package com.belmu.uhc.TeamsManager;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Core.Options;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
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
import java.util.Arrays;
import java.util.List;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class TeamChooser implements Listener {

    public final UHC plugin;
    public TeamChooser(UHC plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
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
                                    player.sendMessage(plugin.prefix + "§cYou are already in this team!");
                                    refresh(player);
                                    return;
                                }

                                if (chosenTeam.getPlayers().size() >= Options.pPerTeam) {
                                    player.sendMessage(plugin.prefix + "§cThis team is full!");
                                    refresh(player);
                                    return;
                                }
                            }
                            chosenTeam.addPlayer(player);
                            Teams.playersToSpread.remove(player.getUniqueId());

                            player.setDisplayName(chosenTeam.getPrefix() + player.getName());
                            player.setPlayerListName(chosenTeam.getPrefix() + player.getName());

                            if (!Teams.inGameTeams.contains(chosenTeam))
                                Teams.inGameTeams.add(chosenTeam);

                            player.sendMessage(plugin.prefix + "§7Successfully added you to " + chosenTeam.getPrefix() + chosenTeam.getName());
                            player.playSound(player.getLocation(), Sound.CLICK, 1, Integer.MAX_VALUE);

                            refresh(player);
                        }
                    }
                } else if(cur.getType() == Material.BARRIER)
                    player.closeInventory();
            }
        }
    }

    public void refresh(Player player) {
        player.closeInventory();

        Inventory chooser = teamChooser(player);
        player.openInventory(chooser);
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
                    if (it.getItemMeta().getDisplayName().equalsIgnoreCase(Options.teamChooserName)) {

                        e.setCancelled(true);
                        Inventory inv = teamChooser(player);
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

            Options.pPerTeam = 1;
            f = players;
        }

        if(f % 2 != 0)
            f = f + 1;

        DecimalFormat formatter = new DecimalFormat("#");
        String a = formatter.format(f);

        int finalNumber = Integer.parseInt(a);

        List<TeamsList> teamsList = new ArrayList<>(Arrays.asList(TeamsList.values()));
        for(int i = 0; i < finalNumber; i++) {

            Team team = Teams.getTeam(teamsList.get(i).teamName);
            possibleTeams.add(team);
        }
        return possibleTeams;
    }

    @SuppressWarnings("deprecation")
    public static Inventory teamChooser(Player player) {
        ScoreboardManager m = Bukkit.getScoreboardManager();
        Scoreboard s = m.getMainScoreboard();
        Inventory inv = Bukkit.createInventory(null, 27, "Teams");

        for (int i = 0; i < getPossibleTeams().size(); i++) {
            List<TeamsList> teamsList = new ArrayList<>(Arrays.asList(TeamsList.values()));
            TeamsList team = teamsList.get(i);

            ItemStack ch = new ItemStack(Material.BANNER, 1);
            ItemMeta chM = ch.getItemMeta();

            Team scoreboardTeam = s.getTeam(team.teamName);

            BannerMeta meta = (BannerMeta) chM;
            meta.setBaseColor(team.teamDyeColor);

            List<String> lore = new ArrayList<>();

            if(scoreboardTeam.getPlayers() != null) {
                chM.setDisplayName(team.teamColor + team.teamName + "§7 (" + scoreboardTeam.getPlayers().size() + "/" + Options.pPerTeam + ")");

                for (OfflinePlayer all : scoreboardTeam.getPlayers())
                    lore.add("§7» " + team.teamColor + all.getName());
            } else {
                chM.setDisplayName(team.teamColor + team.teamName + "§7 (" + 0 + "/" + Options.pPerTeam + ")");
            }

            if(s.getPlayerTeam(player) == scoreboardTeam) {
                lore.add(" ");
                lore.add("§cYou are already in this team!");
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
