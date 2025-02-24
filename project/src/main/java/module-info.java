module com.stonksco.minitramways {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.stonksco.minitramways to javafx.fxml;
    exports com.stonksco.minitramways;
}