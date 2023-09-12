package com.example.scaleserpentiproject.interfaccia;

import com.example.scaleserpentiproject.logica.exceptions.IllegalPositionException;
import com.example.scaleserpentiproject.logica.exceptions.IllegalPositionRowException;
import com.example.scaleserpentiproject.logica.exceptions.OccupedBoxException;
import com.example.scaleserpentiproject.logica.oggetti.GiocoFacade;
import com.example.scaleserpentiproject.logica.oggetti.Scala;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

@SuppressWarnings("DuplicatedCode")
public class FormAddScala implements Decorator{

    private final CreateConfiguration createConfiguration;
    private TextField piediTextField;
    private TextField cimaTextField;
    private final CustomizeTable customizeTable;

    public FormAddScala(CreateConfiguration createConfiguration,CustomizeTable customizeTable){
        this.createConfiguration=createConfiguration;
        this.customizeTable=customizeTable;
    }

    @Override
    public void draw(Stage stage) {
        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        Label codaLabel = new Label("Numero casella piedi:");
        piediTextField = new TextField();

        Label testaLabel = new Label("Numero casella cima:");
        cimaTextField = new TextField();

        Button okButton = new Button("OK");

        gridPane.add(codaLabel, 0, 0);
        gridPane.add(piediTextField, 1, 0);
        gridPane.add(testaLabel, 0, 1);
        gridPane.add(cimaTextField, 1, 1);
        gridPane.add(okButton, 1, 2, 2, 1);

        borderPane.setCenter(gridPane);
        okButton.setOnMouseClicked(event -> {
            try {
                int piedi = Integer.parseInt(piediTextField.getText());
                int cima = Integer.parseInt(cimaTextField.getText());
                GiocoFacade gioco = createConfiguration.getGioco();
                gioco.addScala(new Scala(piedi,cima));
                customizeTable.updateGridPaneWithSnakeOrLadders(piedi, cima, false);
                stage.close();
            }catch (OccupedBoxException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText(null);
                alert.setContentText("Casella occupata da un altro elemento");
                alert.showAndWait();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText(null);
                alert.setContentText("Inserisci numeri validi per le caselle!");
                alert.showAndWait();
            } catch (IllegalPositionRowException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText(null);
                alert.setContentText("Inserire la scala su righe differenti!");
                alert.showAndWait();
            } catch (IllegalPositionException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText(null);
                alert.setContentText("Posizione scala è al di fuori del tabellone!");
                alert.showAndWait();
            }
        });

        Scene addScala = new Scene(borderPane, 350, 150);
        stage.setScene(addScala);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
