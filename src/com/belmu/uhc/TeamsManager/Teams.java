package com.belmu.uhc.TeamsManager;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Core.Options;
import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class Teams {

    public final UHC plugin;
    public Teams(UHC plugin) {
        this.plugin = plugin;
    }

    public List<TeamsList> teams = new ArrayList<>();
    public List<Team> allTeams = new ArrayList<>();
    public List<Team> inGameTeams = new ArrayList<>();
    public List<Team> teamsAtStart = new ArrayList<>();

    public void initializeTeams() {
        for(TeamsList teamsList : TeamsList.values()) {
            if(plugin.sc.getTeam(teamsList.teamName) == null) {
                Team t = plugin.sc.registerNewTeam(teamsList.teamName);

                t.setPrefix(teamsList.prefix);
                t.setCanSeeFriendlyInvisibles(true);
                t.setAllowFriendlyFire(false);
                t.setNameTagVisibility(NameTagVisibility.ALWAYS);
                t.setDisplayName(teamsList.teamName);
            }
            if(!teams.contains(teamsList)) teams.add(teamsList);
        }
    }

    public List<UUID> playersToSpread = new ArrayList<>();

    @SuppressWarnings("deprecation")
    public void addPlayersToTeams(Player player) {

        if (plugin.getMode() == 1) {
            UUID uuid = player.getUniqueId();

            playersToSpread.add(player.getUniqueId());

            new BukkitRunnable() {

                @Override
                public void run() {
                    if (playersToSpread.contains(uuid)) {

                        if (Bukkit.getOnlinePlayers().size() > Options.pPerTeam) {
                            int divide = Bukkit.getOnlinePlayers().size() / Options.pPerTeam;

                            Random r  = new Random();
                            int upper = Math.round(divide - 1) + 1;

                            Team team = plugin.sc.getTeam(teams.get(r.nextInt(upper)).teamName);

                            if (team.getPlayers().size() < Options.pPerTeam) {
                                this.cancel();
                                team.addPlayer(player);

                                playersToSpread.remove(uuid);
                                player.setDisplayName(plugin.sc.getPlayerTeam(player).getPrefix() + player.getName());
                                player.setPlayerListName(plugin.sc.getPlayerTeam(player).getPrefix() + player.getName());

                                if (!inGameTeams.contains(plugin.sc.getPlayerTeam(player)))
                                    inGameTeams.add(plugin.sc.getPlayerTeam(player));

                                if (!teamsAtStart.contains(plugin.sc.getPlayerTeam(player)))
                                    teamsAtStart.add(plugin.sc.getPlayerTeam(player));
                            }

                        } else {
                            Random r  = new Random();
                            int upper = Bukkit.getOnlinePlayers().size() + 1;

                            Team team = plugin.sc.getTeam(teams.get(r.nextInt(upper)).teamName);

                            if (team.getPlayers().size() < Options.pPerTeam) {
                                this.cancel();

                                team.addPlayer(player);
                                playersToSpread.remove(uuid);

                                player.setDisplayName(plugin.sc.getPlayerTeam(player).getPrefix() + player.getName());
                                player.setPlayerListName(plugin.sc.getPlayerTeam(player).getPrefix() + player.getName());

                                if (!inGameTeams.contains(plugin.sc.getPlayerTeam(player)))
                                    inGameTeams.add(plugin.sc.getPlayerTeam(player));

                                if (!teamsAtStart.contains(plugin.sc.getPlayerTeam(player)))
                                    teamsAtStart.add(plugin.sc.getPlayerTeam(player));
                            }
                        }
                    }
                }

            }.runTaskTimer(plugin, 15, 5);
        }
    }

    public Team getTeam(String teamName) {
        for(Team team : allTeams) {
            if(team.getName().equalsIgnoreCase(teamName)) return team;
        }
        return null;
    }

    public List<Team> getAllTeams() {
        for (TeamsList teamsList : teams) {
            Team team = plugin.sc.getTeam(teamsList.teamName);
            if (!allTeams.contains(team)) allTeams.add(team);
        }
        return allTeams;
    }
}
