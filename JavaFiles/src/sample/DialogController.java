package sample;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * This class contains the help pop up box functionality
 * Previously contained within ControllerIngames.java, extracted for clarity.
 * @author Group 20
 *
 */
public class DialogController {
	private static boolean isMaori = false;
	private static boolean isPractise = false;
	
	/**
	 * Creates a pop up box that provides a help feature to the user
	 * Different messages show up depending on whether the game is in Maori mode, English mode
	 * games mode or practise mode
	 */
	public static void showHelp(){
		isMaori = Controller.getIsMaori();
		isPractise = Controller.getIsPractise();
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		Group dialogGroup = new Group();
		String messageSkip;
		String messageTiming = "   You can get bonus points based on the average \ntime taken to answer words correctly. The time only gets \ntaken into account if a word is spelt correctly. If a word is \n   spelt incorrectly or skipped that time is not recorded.";
		
		//the messages change depending on what mode the user is in
		if(!isMaori) {
			messageSkip = "If you don't know a word, press Don't Know to skip it";
		}
		else{
			messageSkip = "Ā tērā Kupu = ka kore anō";
		}

		//labels and button created and added to group
		Label helpSkip = new Label();
		helpSkip.setText(messageSkip);
		helpSkip.layoutXProperty().bind(dialog.widthProperty().subtract(helpSkip.widthProperty()).divide(2));
		helpSkip.setLayoutY(30);
		helpSkip.setTextFill(Color.BLACK);
		helpSkip.setFont(Font.font ("Verdana", FontWeight.BOLD, 20));
		helpSkip.setAlignment(Pos.CENTER);
		
		Label helpMacron = new Label();
		helpMacron.setText("To type a vowel with a macron,\npress the corresponding button");
		if(isMaori){
			helpMacron.setText("oropuare tohutō,\n  pāwhiri pātene");
		}
		helpMacron.layoutXProperty().bind(dialog.widthProperty().subtract(helpMacron.widthProperty()).divide(2));
		helpMacron.setLayoutY(60);
		helpMacron.setTextFill(Color.BLACK);
		helpMacron.setFont(Font.font ("Verdana", FontWeight.BOLD, 20));
		helpMacron.setAlignment(Pos.CENTER);
		
		//message about timing and score will not show up in practise mode
		if(isPractise) {
			messageTiming = "";
			helpSkip.setLayoutY(90);
			helpMacron.setLayoutY(120);
		}
		
		Label helpTiming = new Label();
		helpTiming.setText(messageTiming);
		helpTiming.layoutXProperty().bind(dialog.widthProperty().subtract(helpTiming.widthProperty()).divide(2));
		helpTiming.setLayoutY(120);
		helpTiming.setTextFill(Color.BLACK);
		helpTiming.setFont(Font.font ("Verdana", FontWeight.BOLD, 20));
		helpTiming.setAlignment(Pos.CENTER);

		Button dialogQuitButton = new Button();
		dialogQuitButton.setText("Ok");
		if(isMaori){
			dialogQuitButton.setText("Āe");
		}
		dialogQuitButton.layoutXProperty().bind(dialog.widthProperty().subtract(dialogQuitButton.widthProperty()).divide(2));
		dialogQuitButton.setLayoutY(220);
		dialogQuitButton.setPrefSize(200,50);
		dialogQuitButton.setFont(Font.font ("Verdana", FontWeight.BOLD, 15));

		//add component to group and show
		dialogGroup.getChildren().addAll(helpSkip, helpMacron, dialogQuitButton, helpTiming);
		Scene dialogScene = new Scene(dialogGroup, 700,300);
		dialogScene.setFill(Color.LIGHTGREY);
		dialog.setResizable(false);
		dialog.setScene(dialogScene);
		dialog.show();

		//when button is clicked it returns to main window
		dialogQuitButton.setOnAction( actionEvent -> {
			dialog.hide();
		});
	}

	/**
	 * Creates a pop up box that gives the user feedback after submitting an attempt
	 * The feedback will vary depending on what language the user is in and also what mode (practice or game)
	 * @param wordInPlay
	 * @param correct
	 * @param seconds
	 */
	public static void dialogBoxInGame(String wordInPlay, boolean correct, int seconds){
		String message = "Good Try, Hope you get it correct next time!";
		String correctSpelling = "";
		String correctWord = "";
		isMaori = Controller.getIsMaori();
		isPractise = Controller.getIsPractise();
		
		//These set of if statements decide the messages within the pop up box
		//What message comes up depends on current game language and mode settings
		if(isMaori){
			if(correct){
				ControllerIngame.writeStats("tika");
				//timing will only be shown if in games mode
				if(!isPractise) {
					message = "Ka Pai! Koia te hāngaitanga. " +
						"I roto i te "+seconds+" hēkona!";
				}else {
					message = "Ka Pai! Koia te hāngaitanga.";
				}
			}
			else{
				ControllerIngame.writeStats("pape");
				message = "Papai whakamātau";
				String cmd = "echo \"" + -1 + "\" >> .timeData.txt";
				ControllerIngame.bash(cmd);
				if(isPractise){
					//Correct spelling of the word only comes up in practise mode
					correctSpelling = "Whakatika :" ;
					correctWord = wordInPlay;
				}
			}
		}
		else {
			if (correct) {
				ControllerIngame.writeStats("correct");
				//Timing will only be shown if in games mode
				if(!isPractise) {
					message = "Well Done! You spelt the word correctly in " + seconds + " seconds!";
				}else {
					message = "Well Done! You spelt the word correctly!";
				}
			} else {
				ControllerIngame.writeStats("incorrect");
				String cmd = "echo \"" + -1 + "\" >> .timeData.txt";
				ControllerIngame.bash(cmd);
				//Correct spelling of the word only comes up in practise mode
				if (isPractise) {
					correctWord = wordInPlay;
					correctSpelling = "The correct spelling was: ";
				}
			}
		}
		//New stage created for dialogbox to output result of conditional statements needed
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initStyle(StageStyle.UNDECORATED);
		Group dialogGroup = new Group();


		//All components of window are styled below

		//Labels and buttons created and added to group
		Label dialogMessage = new Label();
		dialogMessage.setText(message);
		dialogMessage.setLayoutY(60);
		dialogMessage.layoutXProperty().bind(dialog.widthProperty().subtract(dialogMessage.widthProperty()).divide(2));
		dialogMessage.setTextFill(Color.WHITE);
		dialogMessage.setFont(Font.font ("Verdana", FontWeight.BOLD, 20));
		dialogMessage.setAlignment(Pos.CENTER);

		Label spellingMessage = new Label();
		spellingMessage.setText(correctSpelling);
		spellingMessage.layoutXProperty().bind(dialog.widthProperty().subtract(spellingMessage.widthProperty()).divide(2));
		spellingMessage.setLayoutY(90);
		spellingMessage.setTextFill(Color.WHITE);
		spellingMessage.setFont(Font.font ("Verdana", FontWeight.BOLD, 20));
		spellingMessage.setAlignment(Pos.CENTER);

		Label word = new Label();
		word.setText(correctWord);
		word.layoutXProperty().bind(dialog.widthProperty().subtract(word.widthProperty()).divide(2));
		word.setLayoutY(120);
		word.setTextFill(Color.WHITE);
		word.setFont(Font.font ("Verdana", FontWeight.BOLD, 20));
		word.setAlignment(Pos.CENTER);


		Button dialogQuitButton = new Button();
		dialogQuitButton.setText("Return to Quiz!");
		if(isMaori){
			dialogQuitButton.setText("Auraki");
		}
		dialogQuitButton.layoutXProperty().bind(dialog.widthProperty().subtract(dialogQuitButton.widthProperty()).divide(2));
		dialogQuitButton.setLayoutY(150);
		dialogQuitButton.setPrefSize(200,50);
		dialogQuitButton.setFont(Font.font ("Verdana", FontWeight.BOLD, 15));

		//When button is clicked it returns to main window and checks whether it goes to next word or ends game
		dialogQuitButton.setOnAction( actionEvent -> {
			dialog.close();
		});

		//Add components to group and show

		dialogGroup.getChildren().addAll(dialogMessage, dialogQuitButton, spellingMessage, word);
		Scene dialogScene = new Scene(dialogGroup, 600,300);
		dialogScene.setFill(Color.GREEN);
		dialog.setScene(dialogScene);
		dialog.showAndWait();
	}
}