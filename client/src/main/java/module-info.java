module com.assembleurnational.javachatclient {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.assembleurnational.javachatclient to javafx.fxml;
    exports com.assembleurnational.javachatclient;
}
