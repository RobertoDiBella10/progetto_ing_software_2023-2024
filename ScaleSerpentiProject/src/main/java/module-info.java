module com.example.scaleserpentiproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.example.scaleserpentiproject to javafx.fxml;
    exports com.example.scaleserpentiproject;
}