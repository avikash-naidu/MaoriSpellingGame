package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class Main
 * @author Group 20
 *
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Kēmu Kupu");
        primaryStage.setScene(new Scene(root, 1000, 560));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
