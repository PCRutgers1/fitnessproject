package com.softmeth.fitnessproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The entry point into the program and where the GUI can be started
 *
 * @author Peter Chen, Jonathon Lopez
 */
public class GymManagerMain extends Application {

    /**
     * Starts the GUI and initializes it's size and title attributes
     * @param stage the stage we are setting to
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GymManagerMain.class.getResource("GymManagerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 854, 564);
        stage.setTitle("Gym Manager App");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The entry point to start the program
     * @param args command line args
     */
    public static void main(String[] args) {
        launch();
    }
}
