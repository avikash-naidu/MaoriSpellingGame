package sample;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This class is the main java controller for the Topic Selection page. 
 * It is responsible for selecting relevant words from a word list based on a selected topic,
 * and displaying relevant content on screen.
 * @author Group 20
 *
 */

public class TopicSelection {

	public Button engineering;
	public Button weekdays;
	public Button months;
	public Button babies;
	public Button uni;
	public Button weather;
	public Button work;
	public Button colours;
	public Button software;
	public Button feelings;
	public Button compass;
	public Button back;


	private boolean goHome = false;

	/**
	 * Changes the scene to the Game screen when a topic is chosen by loading the relevant fxml file
	 * @throws IOException
	 */
	public void changeScene() throws IOException {
		boolean isMaori = Controller.getIsMaori();
		boolean isPractise = Controller.getIsPractise();
		Stage primaryStage = (Stage) uni.getScene().getWindow();
		if(!goHome){
			Parent pane;
			if(!isMaori && !isPractise) {
				pane = FXMLLoader.load(getClass().getResource("ingame.fxml"));
			}
			else if(isMaori && !isPractise){
				pane = FXMLLoader.load(getClass().getResource("ingameMaori.fxml"));
			}
			else if(!isMaori){
				pane = FXMLLoader.load(getClass().getResource("inpractise.fxml"));
			}else {
				pane = FXMLLoader.load(getClass().getResource("inpractiseMaori.fxml"));
			}
			primaryStage.getScene().setRoot(pane);
		}
		else{
			goHome = false;
			Parent pane;
			if(!isMaori) {
				pane = FXMLLoader.load(getClass().getResource("sample.fxml"));
			}
			else{
				pane = FXMLLoader.load(getClass().getResource("sampleMaori.fxml"));
			}
			primaryStage.getScene().setRoot(pane);
		}

	}

	/**
	 * Utilizes method in CommonMethods.java for executing bash commands
	 * @param command - String containing the command to be executed
	 */
	public static void bash(String command) {
		CommonMethods.bash(command);
	}

	/**
	 * Utilizes method in CommonMethods.java for extracting lines from a file to get a word from a word list
	 * @param command - specifying line to extract from
	 * @return String containing the extracted word
	 */
	
	public static String wordGet(String command) {
		return CommonMethods.lineGet(command);
	}

	/**
	 * Retrieves 5 random words from a word list and stores them in an array
	 * @param topic - Topic selected by user
	 * @return Array containing 5 string words
	 */
	public String[] getWords(String topic) {
		String[] words = new String[5];

		for(int i = 0; i < 5; i++) {
			words[i] = wordGet("(shuf -n 1 words/" + topic +".txt)");
		}

		return words;
	}

	/**
	 * Stores the 5 words in a local file .selectedWords
	 * @param words - List of 5 words chosen from the relevant word list
	 */
	public static void inputWords(String[] words) {
		bash("touch words/.selectedWords");
		bash("echo " + words[0] +" > words/.selectedWords");
		for(int i = 1; i < 5; i++) {
			bash("echo " + words[i] +" >> words/.selectedWords");
		}
	}
	
	/**
	 * This method tests to see if a specific element in an array is duplicated
	 *  (Helper method for compare method if statement)
	 * @param words - Current array of selected words
	 * @param num - Index of element to check
	 * @return true if duplicated, false if unique
	 */
	public static boolean replaceWhichWord(String[] words, int num) {
		for (int i = num + 1; i < words.length; i++) {
			if(words[num].equals(words[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method tests to see if an array contains any duplicate elements
	 * @param words - Current array of selected words
	 * @return false in no duplicates, true if duplicates
	 */
	public static boolean duplicateInArray(String[] words) {
		for (int i = 0; i<words.length-1; i++) {
			for(int j = i+1; j < words.length; j ++ ) {
				if(words[i].equals(words[j])) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Takes an array and ensures all the words are unique 
	 * It then places them in a text file for use later
	 * @param words - Current array of selected words
	 * @param topic - Currently selected topic
	 */
	public static void compare(String[] words, String topic) {
		String getWordBash = "(shuf -n 1 words/" + topic + ".txt)";
		//checks each word in the array against each other - if it finds a duplicate then it replaces that word
		//Continues until all words are unique
		while (duplicateInArray(words)) {
			if(replaceWhichWord(words, 0)) {
				words[0] = wordGet(getWordBash);
			}else if(replaceWhichWord(words, 1)) {
				words[1] = wordGet(getWordBash);
			}else if(replaceWhichWord(words, 2)) {
				words[2] = wordGet(getWordBash);
			}else if(replaceWhichWord(words, 3)) {
				words[3] = wordGet(getWordBash);
			}
		}

		//Inputs 5 selected words into a file to be used in the quiz
		inputWords(words);

		//This while loop ensure that all 5 words get copied into the file
		while(Integer.parseInt(wordGet("cat words/.selectedWords | wc -l")) != 5) {
			inputWords(words);
		}
	}
	
	/**
	 * When a topic is chosen, this method generates 5 random words from the topic and changes the scene
	 * @param topic - Chosen topic by user
	 * @throws IOException
	 */
	public void topicChosen(String topic) throws IOException {
		String[] words = getWords(topic);
		compare(words, topic);
		changeScene();
	}

	// The following methods are called when the relevant topic button is clicked.
	// They allow to change the scene and generate word lists from the relevant topic.

	public void quizEngineering() throws IOException {
		topicChosen("engineering");
	}

	public void quizWeekdays() throws IOException {
		topicChosen("weekdays");
	}

	public void quizWeekdaysLoan() throws IOException {
		topicChosen("weekdaysLoan");
	}

	public void quizMonths() throws IOException {
		topicChosen("months");
	}

	public void quizMonthsLoan() throws IOException {
		topicChosen("monthsLoan");
	}

	public void quizColours() throws IOException {
		topicChosen("colours");
	}

	public void quizFeelings() throws IOException {
		topicChosen("feelings");
	}

	public void quizCompass() throws IOException {
		topicChosen("compass");
	}

	public void quizUni() throws IOException {
		topicChosen("university");
	}

	public void quizSoftware() throws IOException {
		topicChosen("software");
	}

	public void quizBabies() throws IOException {
		topicChosen("babies");
	}

	public void quizWork() throws IOException {
		topicChosen("work");
	}

	public void quizWeather() throws IOException {
		topicChosen("weather");
	}

	/**
	 * Changes scene back to the home page when the "Back" button is clicked
	 * @throws IOException
	 */
	public void returnHome() throws IOException {
		goHome = true;
		changeScene();
	}

}