package com.example.scaleserpentiproject.interfaccia;

import com.example.scaleserpentiproject.logica.caselle.*;
import com.example.scaleserpentiproject.logica.exceptions.IllegalPositionException;
import com.example.scaleserpentiproject.logica.exceptions.OccupedBoxException;
import com.example.scaleserpentiproject.logica.oggetti.GiocoFacade;
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
public class FormAddCasellaSpeciale implements Decorator{

    private final CreateConfiguration createConfiguration;
    private TextField positionTextField;
    private final CustomizeTable customizeTable;
    private final TipoCasella tipologiaCasella;
    private final Tipo tipo;

    public FormAddCasellaSpeciale(CreateConfiguration createConfiguration, CustomizeTable customizeTable, TipoCasella tipologiaCasella,Tipo tipo){
        this.createConfiguration=createConfiguration;
        this.customizeTable=customizeTable;
        this.tipologiaCasella=tipologiaCasella;
        this.tipo=tipo;
    }

    @Override
    public void draw(Stage stage) {
        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        Label position = new Label("Numero casella :");
        positionTextField = new TextField();

        Button okButton = new Button("OK");

        gridPane.add(position, 0, 0);
        gridPane.add(positionTextField, 1, 0);
        gridPane.add(okButton, 1, 2, 2, 1);

        borderPane.setCenter(gridPane);
        okButton.setOnMouseClicked(event -> {
            try {
                int pos = Integer.parseInt(positionTextField.getText());
                GiocoFacade gioco = createConfiguration.getGioco();
                Casella added = null;
                switch (tipologiaCasella) {
                    case SOSTA -> {
                        if (tipo == Tipo.PANCHINA) {
                            added = new CasellaSosta(pos, Tipo.PANCHINA);
                        } else {
                            added = new CasellaSosta(pos, Tipo.LOCANDA);
                        }
                    }
                    case PREMIO -> {
                        if (tipo == Tipo.MOLLA) {
                            added = new CasellaPremio(pos, Tipo.MOLLA);
                        } else {
                            added = new CasellaPremio(pos, Tipo.DADI);
                        }
                    }
                    case PESCACARTA -> added = new CasellaPescaCarta(pos);
                    default -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Errore");
                        alert.setHeaderText(null);
                        alert.setContentText("Unexpected value: " + tipologiaCasella);
                        alert.showAndWait();
                    }
                }
                gioco.addCasellaSpeciale(pos,added);
                customizeTable.updateGridPaneWithSpecialBox(pos,tipologiaCasella, tipo);
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
            } catch (IllegalPositionException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText(null);
                alert.setContentText("Posizione casella speciale al di fuori del tabellone!");
                alert.showAndWait();
            }
        });

        Scene addSerpente = new Scene(borderPane, 350, 150);
        stage.setScene(addSerpente);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
