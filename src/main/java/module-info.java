module com.softmeth.fitnessproject {
    requires javafx.controls;
    requires javafx.fxml;
//    requires org.junit.jupiter.api;


    opens com.softmeth.fitnessproject to javafx.fxml;
    exports com.softmeth.fitnessproject;
}