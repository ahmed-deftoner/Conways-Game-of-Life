package com.example.sda_project;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class HelloController {
    @FXML
    private Label welcomeText;
    private ListCell<Button> list;
    private static final int h = 10;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}