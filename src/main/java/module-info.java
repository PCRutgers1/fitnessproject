module com.softmeth.fitnessproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.softmeth.fitnessproject to javafx.fxml;
    exports com.softmeth.fitnessproject;
}