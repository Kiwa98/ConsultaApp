module com.wms.consulta {
    requires javafx.controls;
    requires javafx.fxml;

    requires okhttp3;
    requires com.fasterxml.jackson.databind;

    opens com.wms.consulta to javafx.fxml;
    exports com.wms.consulta;
}
