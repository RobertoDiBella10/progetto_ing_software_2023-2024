package com.example.scaleserpentiproject.interfaccia;

import com.example.scaleserpentiproject.logica.caselle.*;
import com.example.scaleserpentiproject.logica.exceptions.IllegalPositionException;
import com.example.scaleserpentiproject.logica.exceptions.OccupedBoxException;
import com.example.scaleserpentiproject.logica.oggetti.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

//PROBLEMI:
//1) Problema con l'animazione dell'avanzamento del player quando incontra la testa del serpente o i piedi di una scala

@SuppressWarnings({"DuplicatedCode", "StringConcatenationInsideStringBufferAppend"})
public class CustomizeTable implements Decorator{

    private final int numRighe;
    private final int numCol;
    private final CreateConfiguration createConfiguration;
    private final GridPane gridPane;
    private Stage confirmationStage;
    private final Map<Integer, StackPane> posStackPane;
    private final CareTaker gestioneTabellone;
    private final LinkedList<StackPane> element; //rappresenta gli elementi(serpenti, scale, caselle speciali) aggiunti al tabellone
    private final Tabellone tabellone;


    public CustomizeTable(int numRighe,int numCol,CreateConfiguration createConfiguration){
        this.numRighe=numRighe;
        this.numCol=numCol;
        this.createConfiguration=createConfiguration;
        posStackPane = new HashMap<>();
        element = new LinkedList<>();
        gridPane = createGridPane();
        tabellone = createConfiguration.getGioco().getTabellone();
        gestioneTabellone = new CareTaker(tabellone);
        TabelloneMemento tm = new TabelloneMemento(numRighe, numCol, tabellone.getSerpenti(), tabellone.getScale() , tabellone.getCaselleSpeciali());
        gestioneTabellone.addMemento(tm);
    }

    @Override
    public void draw(Stage stage) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(40);
        BorderPane borderPane = new BorderPane(vBox);
        gridPane.setGridLinesVisible(true);

        Button backButton = new Button("Indietro");
        backButton.setPrefSize(100, 30);
        backButton.setFont(Font.font(14));
        Button add = new Button("+");
        add.setPrefSize(100, 30);
        add.setFont(Font.font(14));
        Button fine = new Button("Finish");
        fine.setPrefSize(100, 30);
        fine.setFont(Font.font(14));
        backButton.getStyleClass().add("button-brown");
        add.getStyleClass().add("button-brown");
        fine.getStyleClass().add("button-brown");
        HBox hBox = new HBox(10,backButton,add,fine);
        hBox.setSpacing(30);
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        vBox.getChildren().addAll(gridPane,hBox);

        MenuManager menuManager = new MenuManager();
        menuManager.draw(stage);
        menuManager.enableSaveItem();
        menuManager.disableOpenItem();
        borderPane.setTop(menuManager.getMenuBar());
        borderPane.setCenter(vBox);
        Scene scene = new Scene(borderPane, 750, 750);
        String cssFilePath = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        stage.setScene(scene);
        scene.getStylesheets().add(cssFilePath);

        menuManager.getSaveItem().setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Tabellone Configuration");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );

            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                saveToFile(file);
            }
        });

        add.setOnMouseClicked(event -> {
            FormSelectedAdd formSelectedAdd = new FormSelectedAdd(createConfiguration,this);
            formSelectedAdd.draw(new Stage());
            Tabellone tabelloneModified = createConfiguration.getGioco().getTabellone();
            TabelloneMemento tmModified = new TabelloneMemento(numRighe, numCol, tabelloneModified.getSerpenti(), tabelloneModified.getScale(), tabelloneModified.getCaselleSpeciali() );
            gestioneTabellone.addMemento(tmModified);
        });

        backButton.setOnMouseClicked(event -> {
            event.consume();
            if(gestioneTabellone.getDimensione()<=1)
                showConfirmationDialog(stage);
            else {
                gestioneTabellone.undo();
                StackPane elementToRemove = element.getFirst();
                rimuoviOggetto(elementToRemove);
                element.removeFirst();
                try {
                    tabellone.removeElement(Integer.parseInt(elementToRemove.getId()));
                } catch (IllegalPositionException | OccupedBoxException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        fine.setOnMouseClicked(event -> {
            FormSelectedPlayersAndRoles formSelectedPlayersAndRoles = new FormSelectedPlayersAndRoles(this,  createConfiguration.getGioco());
            formSelectedPlayersAndRoles.draw(stage);
        });


    }

    private void rimuoviOggetto(StackPane stackPane){
        for(Node n : stackPane.getChildren()) {
            if (n instanceof Line){
                stackPane.getChildren().remove(n);
                return;
            }
            if(n instanceof TextFlow || n instanceof Text){
                stackPane.getChildren().remove(n);
                stackPane.setStyle("-fx-background-color: white;");
                return;
            }
        }
    }

    private void saveToFile(File file) {
        try {
            StringBuilder sb = new StringBuilder();
            FileWriter writer = new FileWriter(file);
            TabelloneMemento memento = gestioneTabellone.ultimoMemento();
            sb.append(memento.getNumRighe() + "," + memento.getNumCol() + "\n");
            Map<Integer, Casella> caselleSpeciali = memento.getCaselleSpeciali();
            int numSpecialBox = caselleSpeciali.size();
            sb.append(numSpecialBox+"\n");
            for(Casella c: caselleSpeciali.values()) {
                if(c.getTipo() == TipoCasella.PREMIO) {
                    CasellaPremio cp = (CasellaPremio) c;
                    if(cp.getTipoCasella() == Tipo.DADI)
                        sb.append(c.getNum() + "-cpd\n");
                    if(cp.getTipoCasella() == Tipo.MOLLA)
                        sb.append(c.getNum() + "-cpm\n");
                }
                if(c.getTipo() == TipoCasella.SOSTA) {
                    CasellaSosta cp = (CasellaSosta) c;
                    if(cp.getTipoCasella() == Tipo.PANCHINA)
                        sb.append(c.getNum() + "-cs1\n");
                    if(cp.getTipoCasella() == Tipo.LOCANDA)
                        sb.append(c.getNum() + "-cs3\n");
                }
                if(c.getTipo() == TipoCasella.PESCACARTA) {
                    sb.append(c.getNum() + "-cpc\n");
                }
            }
            Map<Integer,Serpente> serpenti = memento.getSerpenti();
            int numSnake = serpenti.size();
            sb.append(numSnake+"\n");
            for(Serpente s:serpenti.values())
                sb.append(s.getCoda()+"-"+s.getTesta()+"\n");
            Map<Integer,Scala> scale = memento.getScale();
            int numLadders = scale.size();
            sb.append(numLadders+"\n");
            for(Scala s:scale.values())
                sb.append(s.getPiedi()+"-"+s.getCima()+"\n");
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore!");
            alert.setHeaderText(null);
            alert.setContentText("Errore durante il salvataggio");
            alert.showAndWait();
        }
    }

    private void showConfirmationDialog(Stage stage) {
        // Creazione della finestra di conferma
        confirmationStage = new Stage();
        confirmationStage.initModality(Modality.APPLICATION_MODAL);
        confirmationStage.initStyle(StageStyle.UTILITY);
        confirmationStage.setTitle("Conferma chiusura");
        confirmationStage.setMinWidth(250);

        // Creazione dei pulsanti per "Si" e "No"
        Button yesButton = new Button("Si");
        Button noButton = new Button("No");

        yesButton.setOnMouseClicked(e -> {
            confirmationStage.close();
            createConfiguration.draw(stage);
        });

        noButton.setOnMouseClicked(e -> confirmationStage.close());

        confirmationStage.setOnCloseRequest(e -> confirmationStage.close());

        // Creazione del layout della finestra di conferma
        VBox vBox = new VBox(10);
        HBox layout = new HBox(10);
        vBox.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(yesButton, noButton);
        vBox.getChildren().addAll(new Text("Sei sicuro di voler uscire?"),new Text("I dati non salvati andranno persi"),layout);

        Scene scene = new Scene(vBox,200,150);
        confirmationStage.setScene(scene);
        confirmationStage.showAndWait();
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0));
        gridPane.setPrefSize(50 * numCol,50 * numRighe);
        gridPane.setMaxSize(50 * numCol,50 * numRighe);
        gridPane.setMinSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);

        int cellNumber = 1;
        for (int row = numRighe - 1; row >= 0; row--) {
            if (row % 2 == 0) {
                for (int col = 0; col < numCol; col++) {
                    cellNumber = getCellNumber(gridPane, cellNumber, row, col);
                }
            } else {
                for (int col = numCol - 1; col >= 0; col--) {
                    cellNumber = getCellNumber(gridPane, cellNumber, row, col);
                }
            }
        }
        // Impostazioni di layout per il GridPane
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #FFFFFF;");
        return gridPane;
    }

    private int getCellNumber(GridPane gridPane, int cellNumber, int row, int col) {
        Label label = new Label(String.valueOf(cellNumber));
        label.setStyle("-fx-font-weight: bold;");
        StackPane cellPane = new StackPane();
        cellPane.setId(String.valueOf(cellNumber));
        posStackPane.put(cellNumber, cellPane);
        cellPane.setPrefSize(50,50);
        cellPane.setMinSize(50,50);
        cellPane.setMaxSize(50,50);
        cellPane.getChildren().add(label);
        StackPane.setAlignment(label, Pos.TOP_LEFT);
        gridPane.add(cellPane, col, row);
        cellNumber++;
        return cellNumber;
    }

    public void updateGridPaneWithSnakeOrLadders(int idStart,int idEnd, boolean isSnakeAdded) {
        Line line = new Line();
        StackPane start = getStackPaneAt(idStart);
        StackPane end = getStackPaneAt(idEnd);

        if(isSnakeAdded) {
            line.getStyleClass().add("snake");
        }else{
            line.getStyleClass().add("ladder");
        }

        double startX = Objects.requireNonNull(start).getLayoutX();
        double startY = start.getLayoutY();
        double endX = Objects.requireNonNull(end).getLayoutX();
        double endY = end.getLayoutY();

        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        start.getChildren().add(line);

        if(GridPane.getColumnIndex(start)<=GridPane.getColumnIndex(end)) {
            StackPane.setMargin(line, new Insets(0, 0, 25,25 ));
            GridPane.setConstraints(line, GridPane.getColumnIndex(start), GridPane.getRowIndex(start));
            start.setAlignment(Pos.BOTTOM_LEFT);
        }else{
            StackPane.setMargin(line, new Insets(0, 25, 25, 0));
            GridPane.setConstraints(line, GridPane.getColumnIndex(end), GridPane.getRowIndex(start));
            start.setAlignment(Pos.BOTTOM_RIGHT);
        }
        element.addFirst(start);
    }

    public StackPane getStackPaneAt(int id) {
        return posStackPane.get(id);
    }

    public void updateGridPaneWithSpecialBox(int id, TipoCasella tipoCasella, Tipo tipo) {
        StackPane box = getStackPaneAt(id);
        TextFlow textFlow = new TextFlow();
        textFlow.setTextAlignment(TextAlignment.CENTER);
        switch (tipoCasella) {
            case SOSTA -> {
                Objects.requireNonNull(box).setStyle("-fx-background-color: red;");
                Text numeroText = new Text();
                numeroText.setStyle("-fx-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");
                Text turnoText;
                if (tipo == Tipo.PANCHINA) {
                    numeroText.setText("1");
                    turnoText = new Text("\nTURNO");
                } else {
                    numeroText.setText("3");
                    turnoText = new Text("\nTURNI");
                }
                turnoText.setStyle("-fx-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");

                textFlow.getChildren().addAll(numeroText, turnoText);
                box.getChildren().add(textFlow);
                StackPane.setMargin(textFlow, new Insets(10, 0, 0,0 ));
            }
            case PREMIO -> {
                Objects.requireNonNull(box).setStyle("-fx-background-color: #ff5802;");
                Text text;
                if (tipo == Tipo.DADI) {
                    text = new Text("DADI");
                } else {
                    text = new Text("MOLLA");
                }
                text.setStyle("-fx-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");
                textFlow.getChildren().addAll(text);
                box.getChildren().add(textFlow);
                StackPane.setMargin(textFlow, new Insets(15, 0, 0,0 ));
            }
            case PESCACARTA -> {
                Objects.requireNonNull(box).setStyle("-fx-background-color: #0ba0e0;");
                Text text = new Text("PESCA");
                text.setStyle("-fx-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");
                textFlow.getChildren().addAll(text);
                box.getChildren().add(textFlow);
                StackPane.setMargin(textFlow, new Insets(15, 0, 0,0 ));
            }
        }
        element.addFirst(box);
    }

    public GridPane getGridPane(){
        return this.gridPane;
    }

}

