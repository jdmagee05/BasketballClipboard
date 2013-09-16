/**
 * 
 */
package com.jeremy.basketballclipboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author J
 * 
 */
public class StatisticsCruncher {

	// the lists of players and teams
	public ArrayList<String> playerList = new ArrayList<String>();
	public ArrayList<String> teamList = new ArrayList<String>();

	// the array that will contain the players' stats
	double[] playerStats = new double[5];

	public void populateLists(String dir) throws IOException {
		File[] files = new File(dir).listFiles();
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i].getName();
			// set up the buffered reader to read info from the files
			BufferedReader br = new BufferedReader(new FileReader(dir
					+ fileName));
			String strLine = null;
			strLine = br.readLine();
			// get and add the team name to the teanList
			String teamName = strLine;
			if (!(teamName).equals("") && !(teamList.contains(teamName))) {
				teamList.add(teamName);
			}
			// go through the remainder of the file reading the player names and
			// adding them to the playerList
			while (strLine != null) {
				strLine = br.readLine();
				if (strLine == null) {
					break;
				}
				int index = strLine.indexOf("$");
				String playerName = strLine.substring(0, index);
				if (!(playerName).equals("")
						&& !(playerList.contains(playerName))) {
					playerList.add(playerName);
				}
			}
			br.close();
		}
	}

	public ArrayList<String> getPlayerList() {
		return playerList;
	}

	public ArrayList<String> getTeamList() {
		return teamList;
	}

	public double[] calculatePlayerAverages(String playerName, String dir)
			throws IOException {
		File[] files = new File(dir).listFiles();
		int playerOccurrence = 0;
		int totalPoints = 0;
		int totalAssists = 0;
		int totalRebs = 0;
		int totalSteals = 0;

		for (int i = 0; i < files.length; i++) {
			String fileName = files[i].getName();
			BufferedReader br = new BufferedReader(new FileReader(dir
					+ fileName));
			String strLine = br.readLine();
			String name = "";
			while (!(name.equals(playerName))) {
				strLine = br.readLine();
				if (strLine != null) {
					int nameIdx = strLine.indexOf("$");
					name = strLine.substring(0, nameIdx);
				} else {
					break;
				}
			}
			if (name.equals(playerName)) {
				playerOccurrence++;
				int index = strLine.indexOf("$");
				strLine = strLine.substring(index + 1);
				index = strLine.indexOf("$");
				int points = Integer.parseInt(strLine.substring(0, index));
				strLine = strLine.substring(index + 1);
				index = strLine.indexOf("$");
				int assists = Integer.parseInt(strLine.substring(0, index));
				strLine = strLine.substring(index + 1);
				index = strLine.indexOf("$");
				int rebs = Integer.parseInt(strLine.substring(0, index));
				strLine = strLine.substring(index + 1);
				index = strLine.indexOf("$");
				int steals = Integer.parseInt(strLine.substring(0, index));
				strLine = strLine.substring(index + 1);
				// add the points, assists, rebounds and steals to the total
				// points
				totalPoints = totalPoints + points;
				totalAssists = totalAssists + assists;
				totalRebs = totalRebs + rebs;
				totalSteals = totalSteals + steals;
			}
			br.close();
		}
		if (playerOccurrence > 0) {
			// calculate the player stat averages
			double averagePoints = totalPoints / playerOccurrence;
			double averageAssists = totalAssists / playerOccurrence;
			double averageRebs = totalRebs / playerOccurrence;
			double averageSteals = totalSteals / playerOccurrence;
			// put the player stat averages in the playerStats array
			playerStats[0] = playerOccurrence;
			playerStats[1] = averagePoints;
			playerStats[2] = averageAssists;
			playerStats[3] = averageRebs;
			playerStats[4] = averageSteals;

		}
		return playerStats;

	}
}
