package com.example.scaleserpentiproject.interfaccia;

import com.example.scaleserpentiproject.logica.caselle.*;
import com.example.scaleserpentiproject.logica.exceptions.IllegalPositionException;
import com.example.scaleserpentiproject.logica.exceptions.IllegalPositionRowException;
import com.example.scaleserpentiproject.logica.exceptions.OccupedBoxException;
import com.example.scaleserpentiproject.logica.oggetti.GiocoFacade;
import com.example.scaleserpentiproject.logica.oggetti.Scala;
import com.example.scaleserpentiproject.logica.oggetti.Serpente;
import com.example.scaleserpentiproject.logica.oggetti.Tabellone;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

@SuppressWarnings("DuplicatedCode")
public class CreateConfiguration implements Decorator {

    private GiocoFacade gioco;

    @Override
    public void draw(Stage stage) {
        BorderPane borderPane = new BorderPane();

        MenuManager menuManager = new MenuManager();
        menuManager.draw(stage);
        menuManager.enableOpenItem();
        menuManager.disableSaveItem();
        borderPane.setTop(menuManager.getMenuBar());
        // Creazione del VBox con le etichette e i ChoiceBox
        VBox vboxInsert = new VBox(10);
        Label righe = new Label("Inserire il numero di righe");
        Label colonne = new Label("Inserire il numero di colonne");
        ChoiceBox<Integer> rig = new ChoiceBox<>();
        ChoiceBox<Integer> col = new ChoiceBox<>();


        rig.setItems(FXCollections.observableArrayList(5, 6, 7, 8, 9, 10));
        col.setItems(FXCollections.observableArrayList(5, 6, 7, 8, 9, 10));

        vboxInsert.getChildren().addAll(righe, rig, colonne, col);
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
                () -> rig.getValue() == null || col.getValue() == null, // Controlla se uno dei ChoiceBox non è selezionato
                rig.valueProperty(), col.valueProperty() // Aggiungo i Property che si vuole monitorare per il binding
        );

        nextButton.disableProperty().bind(isNextButtonDisabled);

        backButton.setOnMouseClicked(event -> {
            MenuPrincipale menuPrincipale = new MenuPrincipale();
            menuPrincipale.draw(stage);
        });

        menuManager.getOpenItem().setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Apri File di Configurazione Tabellone");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("File di Testo", "*.txt")
            );

            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                loadTabelloneFromFile(selectedFile,stage);
            }
        });

        nextButton.setOnMouseClicked(event -> {
            gioco= new GiocoFacade(rig.getValue(),col.getValue());
            CustomizeTable customizeTable = new CustomizeTable(rig.getValue(),col.getValue(),this);
            customizeTable.draw(stage);
        });

        Scene configuration = new Scene(borderPane, 650, 500);
        String cssFilePath = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        stage.setScene(configuration);
        configuration.getStylesheets().add(cssFilePath);
    }

    private void loadTabelloneFromFile(File file, Stage stage) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String[] dimensions = line.split(",");
            int numRighe = Integer.parseInt(dimensions[0]);
            int numCol = Integer.parseInt(dimensions[1]);
            Tabellone tabellone = Tabellone.getInstance();
            tabellone.creaTabellone(numRighe,numCol);
            if(gioco==null)
                gioco = new GiocoFacade(numRighe,numCol);
            CustomizeTable customizeTable = new CustomizeTable(numRighe,numCol,this);
            customizeTable.draw(stage);
            int numSpecialBox = Integer.parseInt(reader.readLine());
            for(int i=0;i<numSpecialBox;i++){
                line = reader.readLine();
                String[] boxData = line.split("-");
                int numBox = Integer.parseInt(boxData[0]);
                String tipoBox = boxData[1];
                if(tipoBox.equals("cpd")) {
                    CasellaPremio cpd = new CasellaPremio(numBox,Tipo.DADI);
                    tabellone.addCasellaSpeciale(numBox, cpd);
                    customizeTable.updateGridPaneWithSpecialBox(numBox,TipoCasella.PREMIO,Tipo.DADI);
                }
                if(tipoBox.equals("cpm")) {
                    CasellaPremio cpm = new CasellaPremio(numBox,Tipo.MOLLA);
                    tabellone.addCasellaSpeciale(numBox, cpm);
                    customizeTable.updateGridPaneWithSpecialBox(numBox,TipoCasella.PREMIO,Tipo.MOLLA);
                }
                if(tipoBox.equals("cs1")) {
                    CasellaSosta cs1 = new CasellaSosta(numBox, Tipo.PANCHINA);
                    tabellone.addCasellaSpeciale(numBox, cs1);
                    customizeTable.updateGridPaneWithSpecialBox(numBox,TipoCasella.SOSTA,Tipo.PANCHINA);
                }
                if(tipoBox.equals("cs3")) {
                    CasellaSosta cs3 = new CasellaSosta(numBox, Tipo.LOCANDA);
                    tabellone.addCasellaSpeciale(numBox, cs3);
                    customizeTable.updateGridPaneWithSpecialBox(numBox,TipoCasella.SOSTA,Tipo.LOCANDA);
                }
                if(tipoBox.equals("cpc")) {
                    CasellaPescaCarta cpc = new CasellaPescaCarta(numBox);
                    tabellone.addCasellaSpeciale(numBox, cpc);
                    customizeTable.updateGridPaneWithSpecialBox(numBox,TipoCasella.PESCACARTA,Tipo.DEFAULT);
                }
            }
            int numSnake = Integer.parseInt(reader.readLine());
            for (int i = 0; i < numSnake; i++) {
                line = reader.readLine();
                String[] snakeData = line.split("-");
                int coda = Integer.parseInt(snakeData[0]);
                int testa = Integer.parseInt(snakeData[1]);
                tabellone.addSerpente(new Serpente(testa,coda));
                customizeTable.updateGridPaneWithSnakeOrLadders(coda,testa,true);
            }

            int numLadders = Integer.parseInt(reader.readLine());
            for (int i = 0; i < numLadders; i++) {
                line = reader.readLine();
                String[] ladderData = line.split("-");
                int piedi = Integer.parseInt(ladderData[0]);
                int cima = Integer.parseInt(ladderData[1]);
                tabellone.addScala(new Scala(piedi,cima));
                customizeTable.updateGridPaneWithSnakeOrLadders(piedi,cima,false);
            }

        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore!");
            alert.setHeaderText(null);
            alert.setContentText("File non trovato!");
            alert.showAndWait();
        } catch (IOException | OccupedBoxException | IllegalPositionException | IllegalPositionRowException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore!");
            alert.setHeaderText(null);
            alert.setContentText("Errore durante il caricamento");
            alert.showAndWait();
        }
    }

    public GiocoFacade getGioco(){
        return this.gioco;
    }
}
