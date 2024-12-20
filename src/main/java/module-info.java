module com.example.project3_algo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.project3_algo to javafx.fxml;
    exports com.example.project3_algo;
}