package com.belmu.uhc.Teams;

import com.belmu.uhc.Main;
import com.belmu.uhc.Utils.Options;
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

public class Teams {

    public static List<TeamsList> teams = new ArrayList<>();

    public static List<Team> allTeams = new ArrayList<>();
    public static List<Team> inGameTeams = new ArrayList<>();
    public static List<Team> teamsAtStart = new ArrayList<>();

    public static void initializeTeams() {

        ScoreboardManager m = Bukkit.getScoreboardManager();
        Scoreboard s = m.getMainScoreboard();

        for(TeamsList teamsList : TeamsList.values()) {

            if(s.getTeam(teamsList.teamName) == null) {

                Team t = s.registerNewTeam(teamsList.teamName);

                t.setPrefix(teamsList.prefix);
                t.setCanSeeFriendlyInvisibles(false);
                t.setAllowFriendlyFire(false);
                t.setNameTagVisibility(NameTagVisibility.ALWAYS);
                t.setDisplayName(teamsList.teamName);
            }

            if(!teams.contains(teamsList))
                teams.add(teamsList);
        }
    }

    public static List<UUID> playersToSpread = new ArrayList<>();

    @SuppressWarnings("deprecation")
    public static void addPlayersToTeams(Player player) {

        if (Main.getMode().equalsIgnoreCase("Teams")) {

            ScoreboardManager m = Bukkit.getScoreboardManager();
            Scoreboard s = m.getMainScoreboard();

            UUID uuid = player.getUniqueId();

            playersToSpread.add(player.getUniqueId());

            new BukkitRunnable() {

                @Override
                public void run() {

                    if (playersToSpread.contains(uuid)) {

                        if (Main.online.size() > Options.pPerTeam) {
                            int divide = Main.online.size() / Options.pPerTeam;

                            int max = Math.round(divide - 1);
                            int min = 0;

                            Random r = new Random();
                            int upper = ((max - min) + 1) + min;

                            Team team = s.getTeam(teams.get(r.nextInt(upper)).teamName);

                            if (team.getPlayers().size() < Options.pPerTeam) {

                                this.cancel();
                                team.addPlayer(player);

                                playersToSpread.remove(uuid);
                                player.setDisplayName(s.getPlayerTeam(player).getPrefix() + player.getName());
                                player.setPlayerListName(s.getPlayerTeam(player).getPrefix() + player.getName());

                                if (!inGameTeams.contains(s.getPlayerTeam(player)))
                                    inGameTeams.add(s.getPlayerTeam(player));

                                if (!teamsAtStart.contains(s.getPlayerTeam(player)))
                                    teamsAtStart.add(s.getPlayerTeam(player));

                            }

                        } else {

                            int max = Main.online.size();
                            int min = 0;

                            Random r = new Random();
                            int upper = ((max - min) + 1) + min; //((max - min) + 1) + min;

                            Team team = s.getTeam(teams.get(r.nextInt(upper)).teamName);

                            if (team.getPlayers().size() < Options.pPerTeam) {
                                this.cancel();

                                team.addPlayer(player);
                                playersToSpread.remove(uuid);

                                player.setDisplayName(s.getPlayerTeam(player).getPrefix() + player.getName());
                                player.setPlayerListName(s.getPlayerTeam(player).getPrefix() + player.getName());

                                if (!inGameTeams.contains(s.getPlayerTeam(player)))
                                    inGameTeams.add(s.getPlayerTeam(player));

                                if (!teamsAtStart.contains(s.getPlayerTeam(player)))
                                    teamsAtStart.add(s.getPlayerTeam(player));

                            }
                        }
                    }
                }

            }.runTaskTimer(Main.getInstance(), 15, 5); // Valeur de la boucle (Elle démarre avec 15 ticks (3/4 de seconde) et effectue ce qu'il y a dedans tous les 5 ticks (1/4 de seconde)).

        }
    }

    public static Team getTeam(String teamName) {

        for(Team team : allTeams) {

            if(team.getName().equalsIgnoreCase(teamName))
                return team;
        }
        return null;
    }

    public static List<Team> getAllTeams() {

        ScoreboardManager m = Bukkit.getScoreboardManager();
        Scoreboard s = m.getMainScoreboard();

        for (TeamsList teamsList : teams) {
            Team team = s.getTeam(teamsList.teamName);

            if (!allTeams.contains(team))
                allTeams.add(team);
        }
        return allTeams;
    }

}
