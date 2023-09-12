package com.example.scaleserpentiproject.interfaccia;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class MenuPrincipale implements Decorator{

    @Override
    public void draw(Stage stage) {

        Button createConfigButton = new Button("Crea configurazione");
        Button homeButton = new Button("Torna alla home");

        createConfigButton.getStyleClass().add("button-brown");
        homeButton.getStyleClass().add("button-brown");

        double buttonWidth = 200;

        createConfigButton.setPrefWidth(buttonWidth);
        homeButton.setPrefWidth(buttonWidth);

        createConfigButton.setOnMouseClicked(e -> {
            CreateConfiguration createConfiguration = new CreateConfiguration();
            createConfiguration.draw(stage);
        });

        homeButton.setOnMouseClicked(e -> {
            Home home = new Home();
            home.draw(stage);
        });

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(createConfigButton, homeButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        Scene secondScene = new Scene(vbox, 650, 500);
        String cssFilePath = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        stage.setScene(secondScene);
        secondScene.getStylesheets().add(cssFilePath);
    }
}
