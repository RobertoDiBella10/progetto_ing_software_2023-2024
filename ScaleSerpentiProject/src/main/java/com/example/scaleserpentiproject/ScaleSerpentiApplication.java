package com.example.scaleserpentiproject;

import com.example.scaleserpentiproject.interfaccia.Home;
import javafx.application.Application;
import javafx.stage.Stage;

public class ScaleSerpentiApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Home home = new Home();
        home.draw(primaryStage);
    }
}