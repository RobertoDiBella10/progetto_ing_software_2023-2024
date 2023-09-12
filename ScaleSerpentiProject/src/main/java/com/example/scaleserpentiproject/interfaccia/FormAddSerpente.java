package com.example.scaleserpentiproject.interfaccia;

import com.example.scaleserpentiproject.logica.exceptions.IllegalPositionException;
import com.example.scaleserpentiproject.logica.exceptions.IllegalPositionRowException;
import com.example.scaleserpentiproject.logica.exceptions.OccupedBoxException;
import com.example.scaleserpentiproject.logica.oggetti.GiocoFacade;
import com.example.scaleserpentiproject.logica.oggetti.Serpente;
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
public class FormAddSerpente implements Decorator{

    private final CreateConfiguration createConfiguration;
    private TextField codaTextField;
    private TextField testaTextField;
    private final CustomizeTable customizeTable;

    public FormAddSerpente(CreateConfiguration createConfiguration,CustomizeTable customizeTable){
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

        Label codaLabel = new Label("Numero casella della coda:");
        codaTextField = new TextField();

        Label testaLabel = new Label("Numero casella della testa:");
        testaTextField = new TextField();

        Button okButton = new Button("OK");

        gridPane.add(codaLabel, 0, 0);
        gridPane.add(codaTextField, 1, 0);
        gridPane.add(testaLabel, 0, 1);
        gridPane.add(testaTextField, 1, 1);
        gridPane.add(okButton, 1, 2, 2, 1);

        borderPane.setCenter(gridPane);
        okButton.setOnMouseClicked(event -> {
            try {
                int coda = Integer.parseInt(codaTextField.getText());
                int testa = Integer.parseInt(testaTextField.getText());
                GiocoFacade gioco = createConfiguration.getGioco();
                gioco.addSerpente(new Serpente(testa, coda));
                customizeTable.updateGridPaneWithSnakeOrLadders(coda, testa, true);
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
                alert.setContentText("Inserire il serpente su righe differenti!");
                alert.showAndWait();
            } catch (IllegalPositionException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText(null);
                alert.setContentText("Posizione serpente è al di fuori del tabellone!");
                alert.showAndWait();
            }
        });

        Scene addSerpente = new Scene(borderPane, 350, 150);
        stage.setScene(addSerpente);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}

