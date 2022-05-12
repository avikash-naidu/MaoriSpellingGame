package sample;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.EventHandler;

/**
 * This class contains controller functionality for in-game play. 
 * Applies to practice and game modules in both English in Maori settings
 * @author Group 20
 *
 */
public class ControllerIngame {

	public javafx.scene.control.TextField guess;
	public Label hint;
	public Label scoreNum;
	public Label speed;
	public Label wordNum;
	public Label numLetters;
	public Label timerNum;
	public Label numLettersInWords;
	private static int score = 0;
	private static int numAttempts;
	private static String attempt;
	private static Boolean correct = false;
	private static int indexPos;
	private static String wordInPlay;
	public Date startTime;
	public Date endTime;
	public Button repeat;
	public Button faster;
	public Button slower;
	public Button dontKnow;
	
	private int oldCaretPosition;
	private String wordLetters = "";
	double minSpeedStretch = 0.25;
	double maxSpeedStretch = 2.0;
	double currentSpeed = 1.0;
	double speedIncrement = 0.25;
	double newSpeed;
	int seconds = 0;

	private static boolean isMaori = false;
	private static boolean isPractise = false;
	String maoriHint = "Tīwhiri\nRua reta: ";
	String maoriWordNum = "Kupu: ";

	/**
	 * Retrieves method from DialogController.java that displays a pop up "help" box
	 */
	public void showHelp() {
		DialogController.showHelp();
		guess.requestFocus();
	}

	/**
	 * Initializes a timer to record time taken to submit a word
	 */
	public void startTimer() {
		startTime = new Date();
	}

	/**
	 * Initializes game variables and start state depending if the game is practice or game mode
	 * @throws IOException
	 */
	public void initialize() throws IOException {
		indexPos = 0;
		isMaori = Controller.getIsMaori();
		isPractise = Controller.getIsPractise();

		//Clear previous data in files
		bash("touch .statistics.txt");
		bash("echo -n \"\" > .statistics.txt");
		bash("touch .timeData.txt");
		bash("echo -n \"\" > .timeData.txt");

		setWordFestival();
		wordLength();
		setMacron("");
		isMaori = Controller.getIsMaori();
		isPractise = Controller.getIsPractise();
		startTimer();
		if(!isPractise) {
			//Updates the time displayed every 1 second on a new thread
			Timer timerUpdate = new Timer();
			timerUpdate.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							endTime = new Date();
							seconds = (int)((endTime.getTime() - startTime.getTime()) / 1000);
							timerNum.setText(Integer.toString(seconds));
						}
					});
				}
			}, 0, 1000);
		}

	}

	/**
	 * Retrieves method from CommonMethods.java to execute bash commands
	 * @param command - command to be executed in bash
	 */
	public static void bash(String command) {
		CommonMethods.bash(command);
	}

	/**
	 * Reads and stores words from a file
	 * @return String List of 5 selected words
	 * @throws IOException
	 */
	public List<String> getSelectedWords() throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader("words/.selectedWords"));
		List<String> words = new ArrayList<>();
		String currentWord = reader.readLine();

		while(currentWord != null){
			words.add(currentWord);
			currentWord = reader.readLine();
		}
		reader.close();
		return words;
	}

	/**
	 *  Records score from previous word and sets the next word
	 */
	public void goNextWordIndex() {
		hideHint();
		if(correct){
			score += 1;
			numAttempts = 0;
			correct = false;
		}else {
			numAttempts = 0;
		}
		indexPos++;
		setWordNum();
		if(!isPractise) {
			setScoreLabel();
			startTimer();
		}
	}

	/**
	 * Displays the progress through the 5 words eg. Word 2 of 5
	 */
	public void setWordNum() {
		if(indexPos < 5) {
			if (!isMaori) {
				wordNum.setText("Word " + (indexPos + 1) + " of 5");
			}else{
				wordNum.setText(maoriWordNum + (indexPos + 1) + "/5");
			}
		}
	}

	/**
	 * Responsible for setting and executing Festival speech synthesis commands for each word
	 * @throws IOException
	 */
	public void setWordFestival() throws IOException {
		wordInPlay = getSelectedWords().get(indexPos);
		bash("touch .festivalCommands.scm");
		bash("echo \"(voice_akl_mi_pk06_cg)\" > .festivalCommands.scm");
		bash("echo \"(Parameter.set 'Duration_Stretch \"" + currentSpeed + "\")\" >> .festivalCommands.scm");
		bash("echo \'(SayText \"" + wordInPlay + "\") \' >> .festivalCommands.scm");
		bash("festival -b .festivalCommands.scm");
	}

	/**
	 * Check if the word was spelt correctly
	 * @param currentWord - User input for the current word
	 * @return true if correct, false if incorrect
	 */
	public Boolean checkCorrect(String currentWord){
		numAttempts++;
		setAttempt();
		return currentWord.equals(attempt.strip().toLowerCase());
	}

	/**
	 *  Takes the user input and sets it in variable attempt
	 */
	public void setAttempt(){
		attempt = guess.getText();
	}

	/**
	 * Allows user to hit enter key instead of submit button by checking for enter key event
	 */
	public void enterKeyCheck(){
		guess.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyPressed) {
				if (keyPressed.getCode() == KeyCode.ENTER) {
					try {
						submitClicked();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}


	/**
	 * Executed when a word is submitted
	 * @throws IOException
	 */
	public void submitClicked() throws IOException {
		if(guess.getText().length()>0) {
			correct = checkCorrect(wordInPlay);
			if(!correct && numAttempts < 2) {
				showHint();
			}else if(correct){
				//Saves the time taken to get the word correct in a file timeData.txt
				//Time is only recorded for a correct question for scoring purposes
				if(!isPractise) {
					endTime = new Date();
					seconds = (int)((endTime.getTime() - startTime.getTime()) / 1000);
					String cmd = "echo \"" + seconds + "\" >> .timeData.txt";
					bash(cmd);
				}
				dialogBoxMessage();
			}else if(numAttempts == 2) {
				dialogBoxMessage();
			}
		} else {
			guess.requestFocus();
		}
	}

	/**
	 * Shows hint if user gets the word incorrect after one attempt
	 */
	public void showHint() {
		String hintLetter;
		int letterIndex = 1;
		hintLetter = wordInPlay.substring(letterIndex,(letterIndex +1));
		if(hintLetter.equals(" ")) {
			letterIndex++;
			hintLetter = wordInPlay.substring(letterIndex, (letterIndex+ 1));
		}
		String hintText;
		if(!isMaori) {
			hintText = "Try Again! Hint:\nSecond Letter is: " + hintLetter;
		}else{
			hintText = maoriHint + hintLetter;
		}

		String newWordLetters = wordLetters.substring(0,1) + wordInPlay.charAt(1) + wordLetters.substring(2, wordLetters.length()-1) + '-';
		wordLetters = newWordLetters;
		numLetters.setText(newWordLetters);
		hint.setText(hintText);
	}

	public void hideHint(){
		hint.setText(null);
	}

	/**
	 * Disables or enables all buttons when there is a process running (e.g. Festival)
	 * @param command, true to enable, false to disable
	 */
	public void buttonsDisable(Boolean command) {
		repeat.setDisable(command);
		faster.setDisable(command);
		slower.setDisable(command);
		dontKnow.setDisable(command);
	}

	/**
	 * Repeats word in speech synthesis when repeat button clicked
	 */
	public void repeatWord() {
		buttonsDisable(true);
		RepeatWordTask repeatTask = new RepeatWordTask();
		repeatTask.setOnSucceeded( event -> {
			buttonsDisable(false);
			guess.requestFocus();
		});
		new Thread(repeatTask).start();
	}

	//Speed Synthesis Controls
	
	/**
	 * Slows down the speech when slower button clicked
	 */
	public void slowDown() {
		newSpeed = currentSpeed + speedIncrement;
		if (newSpeed <= maxSpeedStretch){
			currentSpeed = newSpeed;
			String line = "(Parameter.set 'Duration_Stretch " + currentSpeed + ")";
			bash("sed -i \"2s/.*/" + line + "/\" .festivalCommands.scm");
			setSpeedText();
		}
		guess.requestFocus();
	}

	/**
	 * Speeds up the speech when faster button clicked
	 */
	public void speedUp() {
		newSpeed = currentSpeed - speedIncrement;
		if (newSpeed >= minSpeedStretch) {
			currentSpeed = newSpeed;
			String line = "(Parameter.set 'Duration_Stretch " + currentSpeed + ")";
			bash("sed -i \"2s/.*/" + line + "/\" .festivalCommands.scm");
			setSpeedText();
		}
		guess.requestFocus();
	}

	/**
	 * Set speed output to display current speed of synthesis to user
	 * */
	public void setSpeedText() {
		String speedString = "Speed: ";
		String normal = "Normal";
		String fastest = "Fastest";
		String slowest = "Slowest";
		//Maori translations for the words
		if(isMaori){
			speedString = "Tere: ";
			normal = "Māori";
			fastest = "Teretere";
			slowest = "Akitō";
		}
		//Displayed speeds are reversed to imitate traditional speed settings
		if (currentSpeed == 1.0) {
			speed.setText(speedString + normal);
		} else if (currentSpeed == 0.75) {
			speed.setText(speedString + "1.25");
		} else if (currentSpeed == 0.5) {
			speed.setText(speedString + "1.50");
		} else if (currentSpeed == 0.25) {
			speed.setText(speedString + fastest);
		} else if (currentSpeed == 1.25) {
			speed.setText(speedString +"0.75");
		} else if (currentSpeed == 1.5) {
			speed.setText(speedString +"0.5");
		} else if (currentSpeed == 1.75) {
			speed.setText(speedString +"0.25");
		} else if (currentSpeed == 2.0) {
			speed.setText(speedString + slowest);
		}
	}

	/**
	 * Allows input of macron letter through buttons
	 * @param macron - Macron letter to be inputted
	 */
	public void setMacron(String macron) {
		//code from https://stackoverflow.com/questions/36054363/is-there-more-than-one-caret-in-javafx-textfield
		//stores caret position when text field loses focus
		guess.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				oldCaretPosition = guess.getCaretPosition();
			}
		});
		guess.insertText(oldCaretPosition, macron);
		guess.requestFocus();
		guess.positionCaret(oldCaretPosition + 1);
	}

	//The following macron methods allow text entry of macrons when correlating button is clicked
	
	public void macronA() {
		setMacron("ā");
	}

	public void macronE() {
		setMacron("ē");
	}

	public void macronI() {
		setMacron("ī");
	}

	public void macronO() {
		setMacron("ō");
	}

	public void macronU() {
		setMacron("ū");
	}

	/**
	 * Called after all words are completed to end the game
	 * @throws IOException
	 */
	public void gameEnd() throws IOException {
		if(!isPractise) {
			writeResults();
		}
		reset();
		changeScene();
	}

	/**
	 * Resets static variables for next game
	 * @throws IOException
	 */
	public void reset() {
		score = 0;
		numAttempts = 0;
		wordInPlay = null;
		indexPos = 0;
		correct = false;
		attempt = null;
	}

	/**
	 * Writes the score to a file to read in rewards page
	 */
	public void writeResults() {
		bash("touch .score.txt");
		String scoreStr = String.valueOf(score);
		String cmd = "echo \"" + scoreStr + "\" > .score.txt";
		bash(cmd);
	}

	/**
	 * Writes the statistics for each word to read in stats page
	 * @param result, string containing results
	 */
	public static void writeStats(String result) {
		String cmd = "echo \"" + wordInPlay + ": " + result + "\" >> .statistics.txt";
		bash(cmd);
	}


	/**
	 * Loads stats page
	 * @throws IOException
	 */
	public void changeScene() throws IOException {
		Stage primaryStage = (Stage) guess.getScene().getWindow();
		Parent pane = FXMLLoader.load(getClass().getResource("stats.fxml"));
		primaryStage.getScene().setRoot(pane);
	}


	/**
	 * Loads topic selection page
	 * @throws IOException
	 */
	public void goBack() throws IOException {
		isMaori = Controller.getIsMaori();
		isPractise = Controller.getIsPractise();
		Stage primaryStage = (Stage) guess.getScene().getWindow();
		Parent pane;
		if(!isMaori && !isPractise) {
			pane = FXMLLoader.load(getClass().getResource("topicSelection.fxml"));
		}
		else if(isMaori && !isPractise){
			pane = FXMLLoader.load(getClass().getResource("topicSelectionMaori.fxml"));
		}
		else if(!isMaori){
			pane = FXMLLoader.load(getClass().getResource("topicSelectionPractise.fxml"));
		}
		else{
			pane = FXMLLoader.load(getClass().getResource("topicSelectionPractiseMaori.fxml"));
		}
		primaryStage.getScene().setRoot(pane);
	}

	/**
	 * Updates the current score as displayed to the user
	 */
	public void setScoreLabel() {
		String labelText = String.valueOf(score);
		scoreNum.setText(labelText);
	}

	/**
	 * Checks the word length to display to the player
	 */
	public void wordLength() {
		wordLetters = wordInPlay.replaceAll("[^' ']", "-");
		numLetters.setText(wordLetters);
		if(isMaori){
			numLettersInWords.setText("Reta: " + wordInPlay.length());
		}else {
			numLettersInWords.setText("There are " + wordInPlay.length() + " letters");
		}
	}

	/**
	 * Displays a pop up window containing relevant feedback after submitting or skipping a word
	 **/
	public void dialogBoxMessage() {
		//Create a message to send to user and a new window to output
		DialogController.dialogBoxInGame(wordInPlay, correct, seconds);

		//Sets the next input and changes scenes if it is at 5 words already
		goNextWordIndex();
		guess.clear();

		if(indexPos == 5) {
			try {
				gameEnd();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				guess.requestFocus();
				setWordFestival();
				wordLetters = "";
				wordLength();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}