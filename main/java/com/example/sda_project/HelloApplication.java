package com.example.sda_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        HBox root=new HBox(10);
        Canvas canvas=new Canvas(500,500);
        GraphicsContext graphics=canvas.getGraphicsContext2D();
        draw(graphics);
        Button start=new Button("Start");
        Button stop=new Button("Stop");
        Button reset=new Button("Reset");
        Label l1=new Label("Speed");
        Slider speed=new Slider();
        Label l2=new Label("Zoom");
        Slider zoom=new Slider();
        Button save=new Button("Save");
        Button history=new Button("History");
        VBox vBox=new VBox(20,start,stop,reset,l1,speed,l2,zoom,save,history);
        vBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.setPadding(new Insets(10,30,10,10));
        root.getChildren().addAll(canvas,vBox);
        Scene scene = new Scene(root, 700, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private void draw(GraphicsContext graphics) {
        // clear graphics
        graphics.setFill(Color.LAVENDER);
        graphics.fillRect(0, 0, 500, 500);

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
               /* if (grid[i][j] == 1) {
                    // first rect will end up becoming the border
                    graphics.setFill(Color.gray(0.5, 0.5));
                    graphics.fillRect(i * 10, j * 10, 10, 10);
                    graphics.setFill(Color.PURPLE);
                    graphics.fillRect((i * 10) + 1, (j * 10) + 1, 10 - 2, 10 - 2);
                }else {*/
                    graphics.setFill(Color.gray(0.5, 0.5));
                    graphics.fillRect(i * 10, j * 10, 10, 10);
                    graphics.setFill(Color.LAVENDER);
                    graphics.fillRect((i * 10) + 1, (j * 10) + 1, 10 - 2, 10 - 2);
                //}
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}