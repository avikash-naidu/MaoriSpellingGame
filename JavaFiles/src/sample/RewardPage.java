package sample;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This class contains the functionality for the reward page
 * @author Group 20
 *
 */

public class RewardPage {

	public Button playAgain;
	public Label score;
	public Label messageScore7_10;
	public Label messagePractise;
	public Label messageScore3_6;
	public Label messageScore0_2;
	public Label endGameLabel;
	public ImageView celebration;
	public Label messageMaori;
	private boolean isMaori = Controller.getIsMaori();
	private boolean isPractise = Controller.getIsPractise();
	public int initialScore = 0;
	public int bonusPoints = 0;
	public int totalPoints = 0;
	public float averageTime = 0;

	/*
	 * Reads input from a file using a bash command argument
	 */
	public static String scoreGet(String command) {
		return CommonMethods.lineGet(command);
	}

	/**
	 * Changes the window to the topic selection page when "Play Again" is clicked
	 * @throws IOException
	 */
	public void playAgain() throws IOException {
		Stage primaryStage = (Stage) playAgain.getScene().getWindow();
		Parent pane;
		// What scene the user is taken to depends on what mode and language the game is in
		if(!isMaori) {
			if(isPractise) {
				pane = FXMLLoader.load(getClass().getResource("topicSelectionPractise.fxml"));
			}else {
				pane = FXMLLoader.load(getClass().getResource("topicSelection.fxml"));
			}
		}
		else{
			if(isPractise) {
				pane = FXMLLoader.load(getClass().getResource("topicSelectionPractiseMaori.fxml"));
			}else {
				pane = FXMLLoader.load(getClass().getResource("topicSelectionMaori.fxml"));
			}

		}
		primaryStage.getScene().setRoot(pane);
	}

	/**
	 * Closes the window and ends the game
	 */
	public void quit() {
		Stage primaryStage = (Stage) playAgain.getScene().getWindow();
		primaryStage.close();
		System.exit(0);
	}

	/**
	 * Returns the user to the home page
	 * @throws IOException
	 */
	public void returnMenu() throws IOException {
		Stage primaryStage = (Stage) playAgain.getScene().getWindow();
		Parent pane;
		if(!isMaori) {
			pane = FXMLLoader.load(getClass().getResource("sample.fxml"));
		}
		else{
			pane = FXMLLoader.load(getClass().getResource("sampleMaori.fxml"));
		}
		primaryStage.getScene().setRoot(pane);
	}

	/**
	 * Calculates final score based on the average time to answer correctly
	 */
	public void calculateFinalPoints() {
		initialScore = Integer.valueOf(scoreGet("(head -n 1 .score.txt)"));
		int totalTime = Integer.valueOf(scoreGet("(head -n 1 .timeData.txt)"));
		if (initialScore > 0) {
			averageTime = totalTime/initialScore;
			
			//Allocation of bonus points depends in average time
			if (averageTime <= 3) {
				bonusPoints = 5;
			}
			else if (averageTime <= 6) {
				bonusPoints = 4;
			}
			else if (averageTime <=10) {
				bonusPoints = 3;
			}
			else if (averageTime <= 15) {
				bonusPoints = 2;
			}
			else if (averageTime <= 20) {
				bonusPoints = 1;
			}
		}
		totalPoints = initialScore + bonusPoints;
		setScore();
	}
	
	/**
	 * Displays the score to the player
	 */
	public void setScore() {
		endGameLabel.setText("The end! Correct Words: " + initialScore + "  Average Time Per Word: " + averageTime + " seconds");
		score.setText("Bonus Points Earned: " + bonusPoints + "  Final Score: " + totalPoints);
		if(isMaori){
			score.setText("Tatau: " + totalPoints);
		}
	}

	/**
	 * Sets the display message to be displayed to the player
	 */
	public void setMessage() {
		String getScore = scoreGet("(head -n 1 .score.txt)");
		if(Integer.parseInt(getScore) > 3) {
        	messageScore7_10.setVisible(true);
        } else if (Integer.parseInt(getScore) > 1) {
        	messageScore3_6.setVisible(true);
        } else {
        	messageScore0_2.setVisible(true);
        }
	}
	
	/**
	 * Contains code that is implemented immediately when user enters the scene
	 */
	public void initialize() {
		//what messages are shown depends on what mode the user is in
		//score is only calculated if in the games module
		if(!isPractise) {
			calculateFinalPoints();
			if(!isMaori) {
				messageScore7_10.setVisible(false);
				messageScore3_6.setVisible(false);
				messageScore0_2.setVisible(false);
				setMessage();
			}else {
				messageMaori.setVisible(false);
				messageMaori.setText("Ka Pai!");
				messageMaori.setVisible(true);
			}
		}else {
			if(isMaori) {
				messageMaori.setVisible(true);
			}else {
				messagePractise.setVisible(true);
				messageMaori.setVisible(false);
			}
		}
	}
}