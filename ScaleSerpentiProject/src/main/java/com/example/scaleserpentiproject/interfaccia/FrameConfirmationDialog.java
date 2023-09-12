package com.example.scaleserpentiproject.interfaccia;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FrameConfirmationDialog implements Decorator{

    private final Stage toClose;

    public FrameConfirmationDialog(Stage toClose){
        this.toClose=toClose;
    }

    @Override
    public void draw(Stage stage) {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Conferma chiusura");
        stage.setMinWidth(250);

        Button yesButton = new Button("Si");
        Button noButton = new Button("No");

        yesButton.setOnMouseClicked(e -> {
            stage.close();
            toClose.close();
        });

        noButton.setOnMouseClicked(e -> {
            stage.close();
        });


        stage.setOnCloseRequest(e -> stage.close());

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        HBox layout = new HBox(10);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(yesButton, noButton);
        vBox.getChildren().addAll(new Text("Vuoi uscire?"),layout);

        Scene scene = new Scene(vBox,200,150);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
