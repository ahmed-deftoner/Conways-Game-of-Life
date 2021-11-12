package com.example.sda_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Group group=new Group();
        GridPane grid = new GridPane();
        GridPane grid2=new GridPane();

        grid.setPadding(new Insets(0));
        grid.setHgap(0);
        grid.setVgap(0);

        grid2.setVgap(10);
        grid2.setPadding(new Insets(10));
        grid.add(new Button("Start"),12,12);
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                int number = 10 * r + c;
                Button button = new Button(String.valueOf(number));
                grid.add(button, c, r);
            }
        }

        ScrollPane scrollPane = new ScrollPane(grid);
        ScrollPane scrollPane1 = new ScrollPane(grid2);

       // stage.setScene(new Scene(scrollPane));
       // FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        group.getChildren().add(scrollPane);
        group.getChildren().add(scrollPane1);
        Scene scene = new Scene(group, 500, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}