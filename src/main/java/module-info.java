module org.example.semafor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.semafor to javafx.fxml;
    exports org.example.semafor;
}