package com.jaredbaboo.gameranker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class GamerankerApplication {
    private static Logger logger = Logger.getLogger(GamerankerApplication.class.getName());

    static class TeamPoints {
        private final String teamName;
        private int points;

        public TeamPoints(String teamName, int points) {
            this.teamName = teamName;
            this.points = points;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public String getTeamName() {
            return teamName;
        }

        @Override
        public String toString() {
            return teamName + ", " + points + (points != 1 ? " pts " : " pt");
        }
    }

    static class TeamScore {
        private final String teamName;
        private final int score;

        public TeamScore(String teamName, int score) {
            this.teamName = teamName;
            this.score = score;
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please supply an input file\r\n" +
                    "eg:  java -jar target/gameranker-0.0.1-SNAPSHOT.jar input.dat\n\r");
            return;
        }
        List<TeamPoints> rankTable = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            String line = reader.readLine();
            while (line != null) {
                String[] game = line.split(",");
                if (game.length != 2) {
                    logger.log(Level.WARNING, "ignoring malformed line :\r\n " + line);
                    line = reader.readLine();
                    continue;
                }
                TeamScore[] teamScores = getTeamScoresForGame(game);
                if (teamScores.length == 2) {
                    if (teamScores[0] != null && teamScores[1] != null && !teamScores[0].teamName.equalsIgnoreCase(teamScores[1].teamName)) {
                        addTeamPoints(rankTable, teamScores);
                    }
                }
                line = reader.readLine();
            }

            printLogTable(rankTable);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Fed to process ", e);
        }
    }

    private static void printLogTable(List<TeamPoints> rankTable) {
        System.out.println("*********************************************");
        int index = 1;
        for (TeamPoints teamPoints : rankTable) {
            System.out.println(index + ". " + teamPoints);
            index++;
        }

        System.out.println("*********************************************");
    }

    protected static TeamScore[] getTeamScoresForGame(String[] game) {
        TeamScore[] teamScores = new TeamScore[2];
        int pos = 0;
        for (String teamScore : game) {

            if (!teamScore.strip().contains(" ")) {
                break;
            }
            int splitpoint = teamScore.strip().lastIndexOf(" ");
            int score;
            try {
                score = Integer.parseInt(teamScore.strip().substring(splitpoint).trim());
                teamScores[pos] = new TeamScore(teamScore.strip().substring(0, splitpoint), score);
                pos++;
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING, "ignoring malformed line(Not a number) :\r\n " + game);
                teamScores = new TeamScore[0];
                break;
            }
        }
        return teamScores;
    }

    protected static void addTeamPoints(List<TeamPoints> rankTable, TeamScore[] teamScores) {
        addTeamsToRankTable(rankTable, teamScores);

        if (teamScores[0] == null || teamScores[1] == null) {
            return;
        }
        if (teamScores[0].score == teamScores[1].score) {
            List<TeamPoints> teamPoints = rankTable
                    .stream()
                    .filter(teamPoint -> teamPoint.getTeamName().equalsIgnoreCase(teamScores[0].teamName) ||
                            teamPoint.getTeamName().equalsIgnoreCase(teamScores[1].teamName))
                    .collect(Collectors.toList());
            teamPoints.forEach(teamPoints1 -> teamPoints1.setPoints(teamPoints1.getPoints() + 1));
        } else if (teamScores[0].score < teamScores[1].score) {
            List<TeamPoints> teamPoints = getSpecificTeamToAwardPoints(rankTable, teamScores, 1);
            teamPoints.forEach(teamPoints1 -> teamPoints1.setPoints(teamPoints1.getPoints() + 3));
        } else {
            List<TeamPoints> teamPoints = getSpecificTeamToAwardPoints((List<TeamPoints>) rankTable, teamScores, 0);
            teamPoints.forEach(teamPoints1 -> teamPoints1.setPoints(teamPoints1.getPoints() + 3));
        }

        rankTable.sort((t1, t2) -> {
            int result = t2.points - t1.points;
            if (result == 0) {
                result = t1.getTeamName().compareTo(t2.getTeamName());
            }
            return result;
        });
    }

    protected static List<TeamPoints> getSpecificTeamToAwardPoints(List<TeamPoints> rankTable, TeamScore[] teamScores, int i) {
        return rankTable
                .stream()
                .filter(teamPoint -> teamPoint.getTeamName().equalsIgnoreCase(teamScores[i].teamName))
                .collect(Collectors.toList());
    }

    protected static void addTeamsToRankTable(List<TeamPoints> rankTable, TeamScore[] teamScores) {
        for (TeamScore teamScore : teamScores) {
            if (teamScore == null || teamScore.teamName == null || teamScore.teamName.isEmpty()) {
                continue;
            }
            List<TeamPoints> teamPoints = rankTable
                    .stream()
                    .filter(teamPoint -> teamPoint.getTeamName().equalsIgnoreCase(teamScore.teamName))
                    .collect(Collectors.toList());
            if (teamPoints.size() == 0) {
                rankTable.add(new TeamPoints(teamScore.teamName, 0));
            }
        }
    }

}
