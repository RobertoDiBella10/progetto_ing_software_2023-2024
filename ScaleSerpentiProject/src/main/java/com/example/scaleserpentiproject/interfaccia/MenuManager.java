package com.example.scaleserpentiproject.interfaccia;


import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MenuManager implements Decorator {

    private MenuItem saveItem;
    private MenuItem openItem;
    private MenuBar menuBar;

    @Override
    public void draw(Stage stage) {
        VBox vbox = new VBox(10);
        menuBar = new MenuBar();
        // Creazione dei menu
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");

        openItem = new MenuItem("Apri");
        saveItem = new MenuItem("Salva");
        MenuItem aboutItem = new MenuItem("About");

        fileMenu.getItems().addAll(saveItem, openItem);
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);

        vbox.getChildren().addAll(menuBar);
    }

    public MenuBar getMenuBar(){
        return this.menuBar;
    }

    public MenuItem getSaveItem(){
        return this.saveItem;
    }

    public MenuItem getOpenItem(){
        return this.openItem;
    }

    public void disableSaveItem() {
        saveItem.setDisable(true);
    }

    public void enableOpenItem() {
        saveItem.setDisable(false);
    }

    public void disableOpenItem() {
        openItem.setDisable(true);
    }

    public void enableSaveItem() {
        openItem.setDisable(false);
    }
}
