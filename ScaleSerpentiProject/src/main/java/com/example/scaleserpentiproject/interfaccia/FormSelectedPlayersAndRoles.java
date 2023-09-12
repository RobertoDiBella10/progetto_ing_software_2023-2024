package com.example.scaleserpentiproject.interfaccia;

import com.example.scaleserpentiproject.logica.oggetti.Giocatore;
import com.example.scaleserpentiproject.logica.oggetti.GiocoFacade;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings({"DuplicatedCode", "unused"})
public class FormSelectedPlayersAndRoles implements Decorator{

    private final CustomizeTable customizeTable;
    private final GiocoFacade gioco;
    private int numGiocatori;
    private int numDadi;
    private ArrayList<Giocatore> giocatore;

    public FormSelectedPlayersAndRoles(CustomizeTable customizeTable, GiocoFacade gioco){
        this.customizeTable=customizeTable;
        this.gioco=gioco;
    }

    @Override
    public void draw(Stage stage) {
        BorderPane borderPane = new BorderPane();
        // Creazione del VBox con le etichette e i ChoiceBox
        VBox vboxInsert = new VBox(10);
        Label giocatori = new Label("Inserire il numero di giocatori");
        Label dadi = new Label("Inserire il numero di dadi");
        ChoiceBox<Integer> players = new ChoiceBox<>();
        ChoiceBox<Integer> roles = new ChoiceBox<>();

        players.setItems(FXCollections.observableArrayList(2,3,4,5, 6, 7, 8, 9, 10));
        roles.setItems(FXCollections.observableArrayList(1,2));

        vboxInsert.getChildren().addAll(giocatori, players, dadi, roles);
        vboxInsert.setAlignment(Pos.CENTER);

        borderPane.setCenter(vboxInsert);

        Button backButton = new Button("Indietro");
        Button nextButton = new Button("Avanti");

        backButton.getStyleClass().add("button-brown");
        nextButton.getStyleClass().add("button-brown");

        HBox buttonBox = new HBox(10, backButton, nextButton);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        borderPane.setBottom(buttonBox);

        BooleanBinding isNextButtonDisabled = Bindings.createBooleanBinding(
                () -> players.getValue() == null || roles.getValue() == null,
                players.valueProperty(), roles.valueProperty()
        );

        nextButton.disableProperty().bind(isNextButtonDisabled);

        backButton.setOnMouseClicked(event -> customizeTable.draw(stage));

        nextButton.setOnMouseClicked(event -> {
            numGiocatori = players.getValue();
            numDadi = roles.getValue();
            gioco.creaPartita(numGiocatori, numDadi);
            giocatore = gioco.getGiocatori();
            GameGUI game = new GameGUI(customizeTable, this);
            game.draw(new Stage());
            stage.close();
        });

        Scene configuration = new Scene(borderPane, 650, 500);
        String cssFilePath = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        stage.setScene(configuration);
        configuration.getStylesheets().add(cssFilePath);
    }

    public int getNumGiocatori(){
        return this.numGiocatori;
    }

    public int getNumDadi(){
        return this.numDadi;
    }

    public GiocoFacade getPartita(){
        return this.gioco;
    }

    public ArrayList<Giocatore> getGiocatore() {
        return this.giocatore;
    }
}
