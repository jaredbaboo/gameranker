package com.jaredbaboo.gameranker;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;


public class GamerankerApplicationTest extends TestCase {

    public void testGetTeamScoresForGame() {

        GamerankerApplication.TeamScore[] teamScores = GamerankerApplication.getTeamScoresForGame(new String[0]);
        assertEquals(2, teamScores.length);
        assertEquals(null, teamScores[0]);
        assertEquals(null, teamScores[1]);
        String line = " a 1, b 2";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        assertTrue(teamScores != null);
        assertTrue(teamScores.length == 2);
        assertNotNull(teamScores[0]);
        assertNotNull(teamScores[1]);
        line = " a 1, b b";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        assertTrue(teamScores.length == 0);
        line = " a 1, b ";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        assertTrue(teamScores != null);
        assertNotNull(teamScores[0]);
        assertNull(teamScores[1]);
        line = " a c, b 3";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        assertTrue(teamScores != null);
        assertTrue(teamScores.length == 0);
        line = " a c,";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        assertTrue(teamScores != null);
        assertTrue(teamScores.length == 0);
        line = "b 3";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        assertTrue(teamScores != null);
        assertTrue(teamScores.length == 2);
        line = " ";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        assertTrue(teamScores != null);
        assertTrue(teamScores.length == 2);

    }


    public void testAddTeamPoints() {
        List<GamerankerApplication.TeamPoints> rankTable = new ArrayList<>();
        String line = " a 1, b 2";
        GamerankerApplication.TeamScore[] teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        GamerankerApplication.addTeamPoints(rankTable, teamScores);
        assertTrue(rankTable.size() == 2);
        assertTrue(rankTable.get(0).getTeamName().equalsIgnoreCase("b"));
        assertTrue(rankTable.get(0).getPoints() == 3);
        line = " c 2, b 2";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        GamerankerApplication.addTeamPoints(rankTable, teamScores);
        assertTrue(rankTable.size() == 3);
        assertTrue(rankTable.get(0).getTeamName().equalsIgnoreCase("b"));
        assertTrue(rankTable.get(0).getPoints() == 4);
        line = " d 12, b 2";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        GamerankerApplication.addTeamPoints(rankTable, teamScores);
        assertTrue(rankTable.size() == 4);
        assertTrue(rankTable.get(0).getTeamName().equalsIgnoreCase("b"));
        assertTrue(rankTable.get(0).getPoints() == 4);
        line = " d 3, a 2";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        GamerankerApplication.addTeamPoints(rankTable, teamScores);
        assertTrue(rankTable.size() == 4);
        assertTrue(rankTable.get(0).getTeamName().equalsIgnoreCase("d"));
        assertTrue(rankTable.get(0).getPoints() == 6);
        assertTrue(rankTable.get(1).getTeamName().equalsIgnoreCase("b"));
        assertTrue(rankTable.get(1).getPoints() == 4);
        line = " c 2, a 2";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        GamerankerApplication.addTeamPoints(rankTable, teamScores);
        assertTrue(rankTable.size() == 4);
        assertTrue(rankTable.get(0).getTeamName().equalsIgnoreCase("d"));
        assertTrue(rankTable.get(0).getPoints() == 6);
        line = " b 2, a 2";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        GamerankerApplication.addTeamPoints(rankTable, teamScores);
        assertTrue(rankTable.size() == 4);
        assertTrue(rankTable.get(0).getTeamName().equalsIgnoreCase("d"));
        assertTrue(rankTable.get(0).getPoints() == 6);
        assertTrue(rankTable.get(1).getTeamName().equalsIgnoreCase("b"));
        assertTrue(rankTable.get(1).getPoints() == 5);
        assertTrue(rankTable.get(2).getTeamName().equalsIgnoreCase("a"));
        assertTrue(rankTable.get(2).getPoints() == 2);
        assertTrue(rankTable.get(3).getTeamName().equalsIgnoreCase("c"));
        assertTrue(rankTable.get(3).getPoints() == 2);


    }



    public void testAddTeamsToRankTable() {
        List<GamerankerApplication.TeamPoints> rankTable = new ArrayList<>();
        String line = " b 2, a 2";
        GamerankerApplication.TeamScore[] teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        GamerankerApplication.addTeamsToRankTable(rankTable,teamScores);

        assertTrue(rankTable.size() == 2);

        line = "a 1, b 3";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        GamerankerApplication.addTeamsToRankTable(rankTable,teamScores);

        assertTrue(rankTable.size() == 2);

        line = "c 1, b 3";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        GamerankerApplication.addTeamsToRankTable(rankTable,teamScores);

        assertTrue(rankTable.size() == 3);

        line = "c 0, d 1";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        GamerankerApplication.addTeamsToRankTable(rankTable,teamScores);

        assertTrue(rankTable.size() == 4);
    }

    public void testGetSpecificTeamToAwardPoints() {
        List<GamerankerApplication.TeamPoints> rankTable = new ArrayList<>();
        String line = " b 2, a 2";
        GamerankerApplication.TeamScore[] teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        GamerankerApplication.addTeamsToRankTable(rankTable,teamScores);
        line = " b 2, c 0";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        GamerankerApplication.addTeamsToRankTable(rankTable,teamScores);
        line = " d 2, c 4";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        GamerankerApplication.addTeamsToRankTable(rankTable,teamScores);
        List<GamerankerApplication.TeamPoints> teamPoints = GamerankerApplication.getSpecificTeamToAwardPoints(rankTable,teamScores,1);
        assertTrue(teamPoints.size() == 1);
        assertEquals("c",teamPoints.get(0).getTeamName());
        teamPoints = GamerankerApplication.getSpecificTeamToAwardPoints(rankTable,teamScores,0);
        assertTrue(teamPoints.size() == 1);
        assertEquals("d",teamPoints.get(0).getTeamName());
        line = " a 2, b 4";
        teamScores = GamerankerApplication.getTeamScoresForGame(line.split(","));
        teamPoints = GamerankerApplication.getSpecificTeamToAwardPoints(rankTable,teamScores,0);
        assertTrue(teamPoints.size() == 1);
        assertEquals("a",teamPoints.get(0).getTeamName());
        teamPoints = GamerankerApplication.getSpecificTeamToAwardPoints(rankTable,teamScores,1);
        assertTrue(teamPoints.size() == 1);
        assertEquals("b",teamPoints.get(0).getTeamName());
    }
}