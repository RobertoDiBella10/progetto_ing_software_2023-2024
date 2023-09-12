package com.example.scaleserpentiproject.interfaccia;

import com.example.scaleserpentiproject.logica.caselle.Tipo;
import com.example.scaleserpentiproject.logica.caselle.TipoCasella;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FormSelectedAdd implements Decorator{

    private final CreateConfiguration createConfiguration;
    private RadioButton addSerpenteRadio;
    private RadioButton addScalaRadio;
    private RadioButton addCasellaSosta1Radio;
    private RadioButton addCasellaSosta3Radio;
    private RadioButton addCasellaPremioRadio;
    private RadioButton addCasellaPremio1Radio;
    private RadioButton addCasellaPescaCartaRadio;
    private final CustomizeTable customizeTable;

    public FormSelectedAdd(CreateConfiguration createConfiguration,CustomizeTable customizeTable){
        this.createConfiguration=createConfiguration;
        this.customizeTable=customizeTable;
    }

    @Override
    public void draw(Stage stage) {

            VBox vbox = new VBox(10);
            vbox.setPadding(new Insets(10));
            vbox.setAlignment(Pos.CENTER_LEFT);

            ToggleGroup toggleGroup = new ToggleGroup();

            addSerpenteRadio = new RadioButton("Aggiungi serpente");
            addSerpenteRadio.setToggleGroup(toggleGroup);
            addSerpenteRadio.setSelected(true);

            addScalaRadio = new RadioButton("Aggiungi scala");
            addScalaRadio.setToggleGroup(toggleGroup);

            addCasellaSosta1Radio = new RadioButton("Aggiungi Casella Sosta 'Panchina'");
            addCasellaSosta1Radio.setToggleGroup(toggleGroup);

            addCasellaSosta3Radio = new RadioButton("Aggiungi Casella Sosta 'Locanda'");
            addCasellaSosta3Radio.setToggleGroup(toggleGroup);

            addCasellaPremioRadio = new RadioButton("Aggiungi Casella Premio 'Dadi'");
            addCasellaPremioRadio.setToggleGroup(toggleGroup);

            addCasellaPremio1Radio = new RadioButton("Aggiungi Casella Premio 'Molla'");
            addCasellaPremio1Radio.setToggleGroup(toggleGroup);

            addCasellaPescaCartaRadio = new RadioButton("Aggiungi Casella Pesca Carta");
            addCasellaPescaCartaRadio.setToggleGroup(toggleGroup);

            Button annullaButton = new Button("Annulla");
            Button applicaButton = new Button("Applica");

            HBox buttonBox = new HBox(10, annullaButton, applicaButton);
            buttonBox.setAlignment(Pos.CENTER);

            vbox.getChildren().addAll(
                    addSerpenteRadio,
                    addScalaRadio,
                    addCasellaSosta1Radio,
                    addCasellaSosta3Radio,
                    addCasellaPremioRadio,
                    addCasellaPremio1Radio,
                    addCasellaPescaCartaRadio,
                    buttonBox
            );

            annullaButton.setOnMouseClicked(event -> stage.close());
            applicaButton.setOnMouseClicked(event -> {
                if(isSelectedSerpente()){
                    FormAddSerpente formAddSerpente = new FormAddSerpente(createConfiguration,customizeTable);
                    formAddSerpente.draw(new Stage());
                }
                if (isSelectedScala()) {
                    FormAddScala formAddScala = new FormAddScala(createConfiguration,customizeTable);
                    formAddScala.draw(new Stage());
                }
                if (isSelectedCasellaSostaPanchina()) {
                    FormAddCasellaSpeciale formAddCasellaSpeciale = new FormAddCasellaSpeciale(createConfiguration,customizeTable, TipoCasella.SOSTA, Tipo.PANCHINA);
                    formAddCasellaSpeciale.draw(new Stage());
                }
                if (isSelectedCasellaSostaLocanda()) {
                    FormAddCasellaSpeciale formAddCasellaSpeciale = new FormAddCasellaSpeciale(createConfiguration,customizeTable, TipoCasella.SOSTA, Tipo.LOCANDA);
                    formAddCasellaSpeciale.draw(new Stage());
                }
                if (isSelectedCasellaPescaCarta()) {
                    FormAddCasellaSpeciale formAddCasellaSpeciale = new FormAddCasellaSpeciale(createConfiguration,customizeTable, TipoCasella.PESCACARTA, Tipo.DEFAULT);
                    formAddCasellaSpeciale.draw(new Stage());
                }
                if (isSelectedCasellaPremioDadi()) {
                    FormAddCasellaSpeciale formAddCasellaSpeciale = new FormAddCasellaSpeciale(createConfiguration,customizeTable, TipoCasella.PREMIO, Tipo.DADI);
                    formAddCasellaSpeciale.draw(new Stage());
                }
                if (isSelectedCasellaPremioMolla()) {
                    FormAddCasellaSpeciale formAddCasellaSpeciale = new FormAddCasellaSpeciale(createConfiguration,customizeTable, TipoCasella.PREMIO, Tipo.MOLLA);
                    formAddCasellaSpeciale.draw(new Stage());
                }
                stage.close();
            });

            Scene scene = new Scene(vbox, 600, 450);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); // Blocca la finestra principale finché questa finestra non è chiusa
            stage.showAndWait();
    }

    public boolean isSelectedSerpente(){
        return this.addSerpenteRadio.isSelected();
    }

    public boolean isSelectedScala(){
        return this.addScalaRadio.isSelected();
    }

    public boolean isSelectedCasellaPescaCarta(){
        return this.addCasellaPescaCartaRadio.isSelected();
    }

    public boolean isSelectedCasellaSostaPanchina(){
        return this.addCasellaSosta1Radio.isSelected();
    }

    public boolean isSelectedCasellaSostaLocanda(){
        return this.addCasellaSosta3Radio.isSelected();
    }

    public boolean isSelectedCasellaPremioDadi(){
        return this.addCasellaPremioRadio.isSelected();
    }

    public boolean isSelectedCasellaPremioMolla(){
        return this.addCasellaPremio1Radio.isSelected();
    }
}
