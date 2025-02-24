package org.example.semafor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TrafficSimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("semafor.fxml"));
        AnchorPane root = loader.load(); // Load the root element (now AnchorPane)

        primaryStage.setTitle("Simulare Trecere Pietoni");
        primaryStage.setScene(new Scene(root)); // Create the scene with the loaded root
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
