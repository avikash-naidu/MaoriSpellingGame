package sample;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * This class is the main java controller for the statistics page.
 * It is responsible for writing and displaying statistics to the screen. 
 * @author Group 20
 * 
 */

public class Stats {
	
	public Button next;
	public Label stats;
	public Label title;
	public AnchorPane pane;
	
	private static boolean isMaori = false;
	private static boolean isPractise = false;
	public static int totalTime = 0;
	
	/**
	 * Utilizes method in CommonMethods.java for executing bash commands
	 * @param command, a String containing the command to be executed
	 */
	public static void bash(String command) {
		CommonMethods.bash(command);
	}
	
	/**
	 * Utilizes method in CommonMethods.java for extracting lines from a file
	 * @param command, specifying which line to extract
	 * @return String containing the extracted line
	 */
	
	public static String lineGet(String command) {
		return CommonMethods.lineGet(command);
	}
	
	/**
	 * @return String containing the statistics generated through the user's game
	 */
	public static String statsGet() {
		String stats = "";
		
		for(int i = 1; i < 6; i++) {
			if(!isPractise) {
				int currentTime = Integer.valueOf(lineGet("sed -n " + i + "p .timeData.txt"));
				if (currentTime == -1) {
					stats = stats + lineGet("sed -n " + i + "p .statistics.txt") +"\n";
				}else {
					stats = stats + lineGet("sed -n " + i + "p .statistics.txt") + " in " + currentTime + " seconds\n";
					//keep record of total time
					totalTime = totalTime + currentTime;
				}
			}else {
				stats = stats + lineGet("sed -n " + i + "p .statistics.txt") +"\n";
			}
		}
		return stats;
	}
	
	/**
	 * Changes the window to the reward page when "Next" is clicked. 
	 * Different scenes are loaded depending on the game mode (Practice or In Game) and language (Maori or English)
	 * @throws IOException
	 */
	
	public void next() throws IOException {
		writeTotalTimeData();
		Stage primaryStage = (Stage) next.getScene().getWindow();
		Parent pane;
		if(!isMaori) {
			if(isPractise) {
				pane = FXMLLoader.load(getClass().getResource("rewardPagePractise.fxml"));
			}else {
				pane = FXMLLoader.load(getClass().getResource("rewardPage.fxml"));
			}
			primaryStage.getScene().setRoot(pane);
		}
		else {
			if(isPractise) {
				pane = FXMLLoader.load(getClass().getResource("rewardPagePractiseMaori.fxml"));
			}else {
				pane = FXMLLoader.load(getClass().getResource("rewardPageMaori.fxml"));
			}
			primaryStage.getScene().setRoot(pane);
		}
		primaryStage.getScene().setRoot(pane);
	}
	
	
	/**
	 * Method setStats
	 */
	public void setStats() {
		String getStats = statsGet();
		stats.setText(getStats);
	}
	
	/**
	 * Saves the total time taken to a file
	 */
	
	public void writeTotalTimeData() {
		bash("touch .timeData.txt");
		bash("echo \"" + totalTime + "\" > .timeData.txt");
	}
	
	/**
	 * Sets up initial states, checking for Maori or English version and Practice or In Game version
	 * @throws IOException
	 */
	
	public void initialize() throws IOException {
		isMaori = Controller.getIsMaori();
		isPractise = Controller.getIsPractise();
		setStats();
		if(isMaori) {
			next.setText("Ā tērā");
			title.setText("Tatauranga");
			title.setLayoutX(400);
		}
    }
}
