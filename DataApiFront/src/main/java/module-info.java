module ru.thebestsolution.dataapi.dataapifront {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires static lombok;
    requires jersey.client;
    requires jsr311.api;
    exports ru.thebestsolution.dataapi.dataapifront.api;
    requires com.fasterxml.jackson.databind;

    opens ru.thebestsolution.dataapi.dataapifront to javafx.fxml;
    exports ru.thebestsolution.dataapi.dataapifront;
}