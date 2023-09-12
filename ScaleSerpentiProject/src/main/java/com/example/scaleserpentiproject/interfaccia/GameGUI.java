package com.example.scaleserpentiproject.interfaccia;

import com.example.scaleserpentiproject.logica.caselle.*;
import com.example.scaleserpentiproject.logica.oggetti.ControllaCasellaCommand;
import com.example.scaleserpentiproject.logica.oggetti.Giocatore;
import com.example.scaleserpentiproject.logica.oggetti.GiocoFacade;
import com.example.scaleserpentiproject.logica.oggetti.MazzoCarte;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"FieldCanBeLocal", "EnhancedSwitchMigration", "DuplicatedCode", "SameParameterValue", "BusyWait", "IntegerMultiplicationImplicitCastToLong", "FieldMayBeFinal"})
public class GameGUI implements Decorator{

    private final GridPane griglia;
    private final FormSelectedPlayersAndRoles formSelectedPlayersAndRoles;
    private final ArrayList<Giocatore> giocatori;
    private int turno;
    private GiocoFacade game;
    private BorderPane borderPane;
    private final GridPane listPlayers = new GridPane();
    private VBox information;//serve per visualizzare la leggenda e la lista dei giocatori una sotto l'altra
    private final CustomizeTable customizeTable;
    private final ArrayList<Circle> pedine;
    private Button roll,simulate;
    private TextArea eventTextArea;
    private volatile boolean simulationRunning = true;

    public GameGUI(CustomizeTable customizeTable, FormSelectedPlayersAndRoles selectedPlayersAndRoles){
        this.customizeTable=customizeTable;
        this.griglia=customizeTable.getGridPane();
        this.formSelectedPlayersAndRoles=selectedPlayersAndRoles;
        this.giocatori=formSelectedPlayersAndRoles.getGiocatore();
        pedine = new ArrayList<>();
        this.turno=0;
    }

    @Override
    public void draw(Stage stage) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(40);
        vBox.setAlignment(Pos.CENTER);
        borderPane = new BorderPane(vBox);
        roll = new Button("Roll");
        roll.setPrefSize(100, 30);
        roll.setFont(Font.font(14));
        simulate = new Button("Simula");
        simulate.setPrefSize(100, 30);
        simulate.setFont(Font.font(14));

        roll.getStyleClass().add("button-brown");
        simulate.getStyleClass().add("button-brown");

        HBox hBox = new HBox(10,roll,simulate);
        hBox.setSpacing(30);
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        vBox.getChildren().addAll(griglia,hBox);
        borderPane.setCenter(vBox);

        this.information=new VBox();
        game = formSelectedPlayersAndRoles.getPartita();

        for(int i=0;i<formSelectedPlayersAndRoles.getNumGiocatori();i++){
            createPlayer(i);
        }
        this.information.getChildren().add(listPlayers);
        this.information.setSpacing(40);
        borderPane.setRight(this.information);

        this.information.setAlignment(Pos.CENTER_LEFT);
        Text text = new Text("Giocatori:");
        text.setStyle("-fx-font-weight: bold;");
        listPlayers.add(text,0,0);
        drawLeggend();

        eventTextArea = new TextArea();
        eventTextArea.setEditable(false);
        eventTextArea.setPrefHeight(750);
        eventTextArea.setPrefWidth(400);
        ScrollPane scrollPane = new ScrollPane(eventTextArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        borderPane.setLeft(scrollPane);

        Scene scene = new Scene(borderPane, 50 * griglia.getColumnCount()+700, 750);
        String cssFilePath = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        stage.setScene(scene);
        scene.getStylesheets().add(cssFilePath);

        roll.setOnMouseClicked(event -> {
            roll.setDisable(true);
            boolean fine = avanza();
            if(fine)
                showWinnerScreen(giocatori.get(turno), stage);
        });

        simulate.setOnMouseClicked(event -> {
            roll.setDisable(true);
            simulate.setDisable(true);
            simulate(stage);
        });

        stage.setOnCloseRequest(event -> {
            event.consume();
            FrameConfirmationDialog frameConfirmationDialog = new FrameConfirmationDialog(stage);
            frameConfirmationDialog.draw(new Stage());
        });
        stage.setTitle("Game");
        stage.show();
    }

    private void simulate(Stage stage) {
        CompletableFuture.runAsync(() -> {
            while (simulationRunning) {
                boolean fine = avanza();
                Platform.runLater(() -> roll.setDisable(true));
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                if (fine) {
                    Platform.runLater(() -> {
                        roll.setDisable(false);
                        simulate.setDisable(false);
                        showWinnerScreen(giocatori.get(turno), stage);
                    });
                    break;
                }

                try {
                    Thread.sleep((giocatori.get(turno).getPosizione() * 100) + 2000); // Serve per la pausa
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private boolean avanza(){
        boolean primoLancio;
        int startPosition = giocatori.get(turno).getPosizione();
        if(startPosition==0)
            startPosition=1;
        if(turno>0){
            Circle cerchioPrec = (Circle) listPlayers.getChildren().get(((turno-1)*listPlayers.getColumnCount())+1);
            cerchioPrec.setVisible(false);
        }
        if(turno==0){
            Circle cerchioPrec = (Circle) listPlayers.getChildren().get(((formSelectedPlayersAndRoles.getNumGiocatori()-1)*listPlayers.getColumnCount())+1);
            cerchioPrec.setVisible(false);
        }
        Circle cerchio = (Circle) listPlayers.getChildren().get(((turno)*listPlayers.getColumnCount())+1);
        cerchio.setVisible(true);
        primoLancio = giocatori.get(turno).getPosizione() == 0;

        String message = "-------------------------TURNO "+giocatori.get(turno).getNome()+" --------------------------------";
        eventTextArea.appendText(message + "\n");
        if(giocatori.get(turno).getTurniStop()==0) {
            game.avanza(giocatori.get(turno));
            message = "Giocatore " + giocatori.get(turno).getNome() + ": al lancio del dado è uscito " + giocatori.get(turno).getUltimoLancio();
            eventTextArea.appendText(message + "\n");
            message = "Giocatore " + giocatori.get(turno).getNome() + ": avanza alla casella in pos. " + giocatori.get(turno).getPosizione();
            eventTextArea.appendText(message + "\n");
            int avanzamento = giocatori.get(turno).getUltimoLancio();
            animatePlayerMovement(startPosition, avanzamento, turno, primoLancio);
            if (giocatori.get(turno).getPosizione() == game.getNumCaselle()) {
                return true;
            }

            Casella casellaCur = game.casellaCorrente(giocatori.get(turno));
            boolean continua = true;
            while(continua){
                continua = controllaCasella(casellaCur, false);
                casellaCur = game.casellaCorrente(giocatori.get(turno));
            }

        }else{
            message = "Giocatore " + giocatori.get(turno).getNome() + ": è fermo per altri "+giocatori.get(turno).getTurniStop()+" turni";
            eventTextArea.appendText(message + "\n");
            giocatori.get(turno).setTurniStop(giocatori.get(turno).getTurniStop()-1);
            roll.setDisable(false);
        }
        turno=(turno+1) % formSelectedPlayersAndRoles.getNumGiocatori();
        return false;
    }

    private void createPlayer(int id) {
        StackPane start = customizeTable.getStackPaneAt(1);

        if (start != null) {
            Color color = getRandomColor();
            Circle circle = new Circle(10);
            circle.setFill(color);
            circle.setId(String.valueOf(id));
            StackPane.setAlignment(circle,Pos.CENTER);
            pedine.add(id,circle);
            start.getChildren().add(circle);
            Text text1 = new Text("Giocatore "+(id+1));
            text1.setStyle("-fx-font-weight: bold;");
            text1.setFill(color);
            Circle cerchio = new Circle(5);
            cerchio.setFill(Paint.valueOf("#1fff3a"));
            cerchio.setVisible(false);
            listPlayers.addRow(id+1,text1,cerchio);
        }
    }

    private boolean controllaCasella(Casella casellaCur, boolean primoLancio){

        MazzoCarte mazzo = game.creaMazzo();
        ControllaCasellaCommand controlCommand = new ControllaCasellaCommand(
                giocatori.get(turno), casellaCur,
                mazzo, game.getPartita()
        );
        controlCommand.execute();
        String message;

        if (casellaCur.containsTestaSerpente()) {
            message = "Giocatore " + giocatori.get(turno).getNome() + ": è sulla testa di un serpente, scende in pos. " + giocatori.get(turno).getPosizione();
            eventTextArea.appendText(message + "\n");
            StackPane posCurPlayer = customizeTable.getStackPaneAt(casellaCur.getNum());
            StackPane posNextPlayer = customizeTable.getStackPaneAt(giocatori.get(turno).getPosizione());
            removeCircle(posCurPlayer, turno);
            posNextPlayer.getChildren().add(pedine.get(turno));
            return false;
        } else if (casellaCur.containsPiediScala()) {
            message = "Giocatore " + giocatori.get(turno).getNome() + ": è sui piedi di una scala, salta in pos. " + giocatori.get(turno).getPosizione();
            eventTextArea.appendText(message + "\n");
            StackPane posCurPlayer = customizeTable.getStackPaneAt(casellaCur.getNum());
            StackPane posNextPlayer = customizeTable.getStackPaneAt(giocatori.get(turno).getPosizione());
            removeCircle(posCurPlayer, turno);
            posNextPlayer.getChildren().add(pedine.get(turno));
            return false;
        } else {
            switch (casellaCur.getTipo()) {
                case PESCACARTA:
                    Tipo carta = controlCommand.getCarta();
                    message = "Giocatore " + giocatori.get(turno).getNome() + ": ha pescato la carta " + carta.name();
                    eventTextArea.appendText(message + "\n");
                    switch (carta) {
                        case PANCHINA, LOCANDA:
                            if (!giocatori.get(turno).haCartaDivietoSosta()) {
                                message = "Giocatore " + giocatori.get(turno).getNome() + ": è fermo per " + giocatori.get(turno).getTurniStop() + " turni";
                                eventTextArea.appendText(message + "\n");
                            } else {
                                message = "Giocatore " + giocatori.get(turno).getNome() + ": dispone della carta divieto di sosta";
                                eventTextArea.appendText(message + "\n");
                            }
                            return false;
                        case DADI:
                            message = "Giocatore " + giocatori.get(turno).getNome() + ": al lancio del dado è uscito " + giocatori.get(turno).getUltimoLancio();
                            eventTextArea.appendText(message + "\n");
                            message = "Giocatore " + giocatori.get(turno).getNome() + ": avanza alla casella in pos. " + giocatori.get(turno).getPosizione();
                            eventTextArea.appendText(message + "\n");
                            animatePlayerMovement(casellaCur.getNum(), giocatori.get(turno).getUltimoLancio(), turno, primoLancio);
                            return true;
                        case MOLLA:
                            message = "Giocatore " + giocatori.get(turno).getNome() + ": avanza alla casella in pos. " + giocatori.get(turno).getPosizione();
                            eventTextArea.appendText(message + "\n");
                            animatePlayerMovement(casellaCur.getNum(), giocatori.get(turno).getUltimoLancio(), turno, primoLancio);
                            return true;
                        case DIVIETOSOSTA:
                            message = "Giocatore " + giocatori.get(turno).getNome() + ": conserva la carta";
                            eventTextArea.appendText(message + "\n");
                            return false;
                    }
                    break;
                case PREMIO:
                    CasellaPremio cp=(CasellaPremio) casellaCur;
                    message = "Giocatore " + giocatori.get(turno).getNome() + ": è sulla casella premio "+cp.getTipoCasella().name();
                    eventTextArea.appendText(message + "\n");
                    if(cp.getTipoCasella()==Tipo.DADI){
                        message = "Giocatore " + giocatori.get(turno).getNome() + ": al lancio del dado è uscito " + giocatori.get(turno).getUltimoLancio();
                        eventTextArea.appendText(message + "\n");
                        message = "Giocatore " + giocatori.get(turno).getNome() + ": avanza alla casella in pos. " + giocatori.get(turno).getPosizione();
                        eventTextArea.appendText(message + "\n");
                    }

                    if(cp.getTipoCasella()==Tipo.MOLLA){
                        message = "Giocatore " + giocatori.get(turno).getNome() + ": avanza alla casella in pos. " + giocatori.get(turno).getPosizione();
                        eventTextArea.appendText(message + "\n");
                    }
                    animatePlayerMovement(cp.getNum(), giocatori.get(turno).getUltimoLancio(), turno, primoLancio);
                    return true;
                case SOSTA:
                    if (!giocatori.get(turno).haCartaDivietoSosta()) {
                        message = "Giocatore " + giocatori.get(turno).getNome() + ": è fermo per " + giocatori.get(turno).getTurniStop() + " turni";
                        eventTextArea.appendText(message + "\n");
                    } else {
                        message = "Giocatore " + giocatori.get(turno).getNome() + ": dispone della carta divieto di sosta";
                        eventTextArea.appendText(message + "\n");
                    }
                    return false;
            }
        }
        return false;
    }

    private Color getRandomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private void drawLeggend(){
        GridPane leggenda = new GridPane();
        HBox hBox1 = new HBox();
        Text title = new Text("Leggenda");
        title.setStyle("-fx-font-weight: bold;-fx-alignment: center;");
        Rectangle boxSosta = new Rectangle(18,18);
        boxSosta.setFill(Paint.valueOf("red"));
        Text text1 = new Text(" = Casella Sosta");
        hBox1.setAlignment(Pos.CENTER_LEFT);
        hBox1.getChildren().addAll(boxSosta,text1);
        HBox hBox2 = new HBox();
        Rectangle boxPescaCarta = new Rectangle(18,18);
        boxPescaCarta.setFill(Paint.valueOf("#0ba0e0"));
        Text text2 = new Text(" = Casella Pesca Carta");
        hBox2.setAlignment(Pos.CENTER_LEFT);
        hBox2.getChildren().addAll(boxPescaCarta,text2);
        HBox hBox3 = new HBox();
        Rectangle boxPremio = new Rectangle(18,18);
        boxPremio.setFill(Paint.valueOf("#ff5802"));
        Text text3 = new Text(" = Casella Premio");
        hBox3.setAlignment(Pos.CENTER_LEFT);
        hBox3.getChildren().addAll(boxPremio,text3);
        leggenda.add(title,0,0);
        leggenda.add(hBox1,0,1);
        leggenda.add(hBox2,0,2);
        leggenda.add(hBox3,0,3);
        leggenda.setHgap(10);
        leggenda.setAlignment(Pos.CENTER_LEFT);
        leggenda.setPrefWidth(200);
        leggenda.setVgap(10);
        this.information.getChildren().add(leggenda);
    }

    private void removeCircle(StackPane stackPane, int playerIndex){
       ObservableList<Node> children = stackPane.getChildren(); // Ottieni la lista dei figli
        Node circleToRemove = null;
        for (Node child : children) {
            if (child instanceof Circle && child.getId().equals(String.valueOf(playerIndex))) {
                circleToRemove = child;
                break; // Esci dal ciclo una volta trovato il cerchio da rimuovere
            }
        }
        if (circleToRemove != null) {
            stackPane.getChildren().remove(circleToRemove);
        }
    }

    private void animatePlayerMovement(int startPosition, int avanzamento, int playerIndex, boolean isPrimoLancio) {
        Timeline timeline1 = new Timeline();
        Timeline timeline2 = new Timeline();
        int endPosition;
        int scarto;
        if(isPrimoLancio) {
            scarto = 0;
            endPosition = avanzamento;
        } else {
            endPosition = avanzamento + startPosition;
            if(endPosition> game.getNumCaselle())
                scarto = endPosition-game.getNumCaselle();
            else {
                scarto = 0;
            }
        }
        for (int i = startPosition; i < (endPosition-scarto); i++) {
            StackPane posCurPlayer = customizeTable.getStackPaneAt(i);
            StackPane posNextPlayer = customizeTable.getStackPaneAt(i + 1);

            KeyFrame avanti = new KeyFrame(Duration.millis(i*100), event -> {
                removeCircle(posCurPlayer,playerIndex);
                posNextPlayer.getChildren().add(pedine.get(playerIndex));
            });
            timeline1.getKeyFrames().add(avanti);
        }
        timeline1.setOnFinished(event -> {
            if (scarto > 0) {
                for (int i = 0; i < scarto; i++) {
                    StackPane posCurPlayer = customizeTable.getStackPaneAt(game.getNumCaselle() - i);
                    StackPane posNextPlayer = customizeTable.getStackPaneAt(game.getNumCaselle() - i - 1);

                    KeyFrame indietro = new KeyFrame(Duration.millis(i * 100), event1 -> {
                        removeCircle(posCurPlayer, playerIndex);
                        posNextPlayer.getChildren().add(pedine.get(playerIndex));
                    });
                    timeline2.getKeyFrames().add(indietro);
                }

                timeline2.setOnFinished(indietroEvent -> roll.setDisable(false));

                timeline2.play();
            } else {
                roll.setDisable(false);
            }
        });

        timeline1.play();
    }

    private void showWinnerScreen(Giocatore vincitore,Stage stage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Vittoria!");
        alert.setHeaderText("Il giocatore " + vincitore.getNome() + " ha vinto!");
        alert.setContentText("Cosa vuoi fare?");

        ButtonType newGameButton = new ButtonType("Nuova Partita");
        ButtonType exitButton = new ButtonType("Esci");

        alert.getButtonTypes().setAll(newGameButton, exitButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == newGameButton) {
                CreateConfiguration createConfiguration = new CreateConfiguration();
                createConfiguration.draw(stage);
            } else if (result.get() == exitButton) {
                stage.close();
            }
        }
    }
}
