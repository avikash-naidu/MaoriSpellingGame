package sample;

import java.io.IOException;


import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class contains all the functionality for the home page in the game
 * @author Group 20
 *
 */
public class Controller {

	public Button games;
	public Button quit;
	public Button maori;
	public Button help;
	private static boolean isMaori = false;
	private static boolean isPractise = false;
	
	/**
	 * This method brings up a help box for the user
	 * gives information on what each of the buttons do
	 */
	public void showHelp() {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		Group dialogGroup = new Group();

		//labels and button created and added to group
		Label helpPrac = new Label();
		helpPrac.setText("Press Practise Module to practise a topic set");
		helpPrac.layoutXProperty().bind(dialog.widthProperty().subtract(helpPrac.widthProperty()).divide(2));
		helpPrac.setLayoutY(60);
		helpPrac.setTextFill(Color.BLACK);
		helpPrac.setFont(Font.font ("Verdana", FontWeight.BOLD, 20));
		helpPrac.setAlignment(Pos.CENTER);
		
		Label helpGames = new Label();
		helpGames.setText("Press Games Module to test your knowledge");
		helpGames.layoutXProperty().bind(dialog.widthProperty().subtract(helpGames.widthProperty()).divide(2));
		helpGames.setLayoutY(90);
		helpGames.setTextFill(Color.BLACK);
		helpGames.setFont(Font.font ("Verdana", FontWeight.BOLD, 20));
		helpGames.setAlignment(Pos.CENTER);
		
		Label helpMaori = new Label();
		helpMaori.setText("Press Māori to play Kēmu Kupu in Te Reo Māori");
		helpMaori.layoutXProperty().bind(dialog.widthProperty().subtract(helpMaori.widthProperty()).divide(2));
		helpMaori.setLayoutY(120);
		helpMaori.setTextFill(Color.BLACK);
		helpMaori.setFont(Font.font ("Verdana", FontWeight.BOLD, 20));
		helpMaori.setAlignment(Pos.CENTER);

		Button dialogQuitButton = new Button();
		dialogQuitButton.setText("Ok");
		dialogQuitButton.setLayoutX(200);
		dialogQuitButton.setLayoutY(160);
		dialogQuitButton.setPrefSize(200,50);
		dialogQuitButton.setFont(Font.font ("Verdana", FontWeight.BOLD, 15));

		//add component to group and show
		dialogGroup.getChildren().addAll(helpPrac, helpGames, dialogQuitButton, helpMaori);
		Scene dialogScene = new Scene(dialogGroup, 600,300);
		dialogScene.setFill(Color.LIGHTGREY);
		dialog.setScene(dialogScene);
		dialog.show();

		//when button is clicked it returns to main window
		dialogQuitButton.setOnAction( actionEvent -> {
			dialog.hide();
		});
	}

	/**
	 * Loads the topic selection window when "games" is clicked
	 **/
	public void startGame() throws IOException {
		isPractise = false;
		Stage primaryStage = (Stage) games.getScene().getWindow();
		Parent pane;
		
		//takes user to a different screen depending on what language its in
		if(!isMaori){
			isPractise = false;
			pane = FXMLLoader.load(getClass().getResource("topicSelection.fxml"));
		}
		else{
			isPractise = false;
			pane = FXMLLoader.load(getClass().getResource("topicSelectionMaori.fxml"));
		}
		primaryStage.getScene().setRoot(pane);

	}

	/**
	 * This method switches the game between Te Reo and English
	 * @throws IOException
	 */
	public void maoriVersion() throws IOException{
		Stage primaryStage = (Stage) games.getScene().getWindow();
		
		//takes user to a different screen depending on what language its in
		if(isMaori){
			isMaori = false;
			Parent pane = FXMLLoader.load(getClass().getResource("sample.fxml"));
			primaryStage.getScene().setRoot(pane);

		}
		else{
			isMaori = true;
			Parent pane = FXMLLoader.load(getClass().getResource("sampleMaori.fxml"));
			primaryStage.getScene().setRoot(pane);
		}
	}
	
	/**
	 * Method startPractise
	 * Loads the topic selection window when "practise" is clicked
	 **/
	public void startPractise() throws IOException {
		Stage primaryStage = (Stage) games.getScene().getWindow();
		Parent pane;
		
		//takes user to a different screen depending on what language its in
		if(!isMaori) {
			isPractise = true;
			pane = FXMLLoader.load(getClass().getResource("topicSelectionPractise.fxml"));
		}
		else {
			isPractise = true;
			pane = FXMLLoader.load(getClass().getResource("topicSelectionPractiseMaori.fxml"));
		}
		primaryStage.getScene().setRoot(pane);
	}

	/**
	 * Method quit
	 * Closes the window and ends the game when "quit" is clicked
	 */
	public void quit() {
		Stage primaryStage = (Stage) quit.getScene().getWindow();
		primaryStage.close();
		System.exit(0);
	}
	
	/**
	 * these methods allow the isPractise and isMaori variables to be accessed 
	 * in other classes
	 * @return
	 */
	public static boolean getIsPractise() {
		return isPractise;
	}

	public static boolean getIsMaori() {
		return isMaori;
	}
} 