package com.example.scaleserpentiproject.interfaccia;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.util.Objects;

@SuppressWarnings("DuplicatedCode")
public class Home implements Decorator{

    @Override
    public void draw(Stage primaryStage){
        String imagePath = "C:/Users/dibel/OneDrive/Desktop/INGEGNERIA/INGEGNERIA DEL SOFTWARE/PROGETTO/snakeandladders.png";
        Image image = new Image(imagePath);
        // Creazione ImageView per visualizzare l'immagine
        ImageView imageView = new ImageView(image);

        TextFlow textFlow = new TextFlow();
        textFlow.setTextAlignment(TextAlignment.CENTER);
        textFlow.setLineSpacing(10);

        // Creazione testo "Scale e Serpenti" con lettere verdi e marroni alternanti
        String testo = "Scale e Serpenti";
        for (int i = 0; i < testo.length(); i++) {
            Text lettera = new Text(String.valueOf(testo.charAt(i)));
            lettera.getStyleClass().add("text-alternating-letters");
            if (i % 2 == 0) {
                lettera.setFill(Color.GREEN);
            } else{
                lettera.setFill(Color.BROWN);
            }
            textFlow.getChildren().add(lettera);
        }

        Button playButton = new Button("Play");
        playButton.getStyleClass().add("button-green");
        VBox vBox = new VBox(textFlow,imageView, playButton);
        vBox.setSpacing(20); // Aggiunge uno spazio tra gli elementi
        vBox.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane(vBox);

        Scene scene = new Scene(root, 650, 500);
        String cssFilePath = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();

        primaryStage.setTitle("Scale e Serpenti");

        primaryStage.setScene(scene);
        scene.getStylesheets().add(cssFilePath);

        playButton.setOnMouseClicked(event -> {
            MenuPrincipale menu = new MenuPrincipale();
            menu.draw(primaryStage);
        });
        primaryStage.setOnCloseRequest(e -> {
                e.consume(); // Consuma l'evento di chiusura per evitare la chiusura immediata della finestra principale
                FrameConfirmationDialog frameConfirmationDialog = new FrameConfirmationDialog(primaryStage);
                frameConfirmationDialog.draw(new Stage());

        });
        primaryStage.show();
    }

}

