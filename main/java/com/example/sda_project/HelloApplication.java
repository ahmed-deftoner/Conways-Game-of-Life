package com.example.sda_project;

import javafx.application.Application;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private double x;
    private double y;

    @Override
    public void start(Stage stage) throws IOException {
        String styles =
                "-fx-background-radius: 22px;"+
                "-fx-border-radius: 22px;" +
                        "-fx-background-color: #e0dce5;"+
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.2, 0.5, 0.2);";
        HBox root=new HBox(10);
        Canvas canvas=new Canvas(500,500);
        GraphicsContext graphics=canvas.getGraphicsContext2D();
        draw(graphics);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                      x=mouseEvent.getX();
                      y=mouseEvent.getY();
                      int roundx= (int) x/20;
                      int roundy=(int) y/20;
                      roundx=roundx*20;
                      roundy=roundy*20;
                      graphics.setFill(Color.BLACK);
                      graphics.fillRect(roundx,roundy,20,20);
                }
        );
        Button start=new Button("Start");
        start.setStyle(styles);
        start.setMaxSize(100,100);
        Button stop=new Button("Stop");
        stop.setStyle(styles);
        stop.setMaxSize(100,100);
        Button reset=new Button("Reset");
        reset.setStyle(styles);
        reset.setMaxSize(100,100);
        Label l1=new Label("Speed");
        l1.setPadding(new Insets(0,50,0,0));
        Slider speed=new Slider();
        Label l2=new Label("Zoom");
        l2.setPadding(new Insets(0,50,0,0));
        Slider zoom=new Slider();
        Button save=new Button("Save");
        save.setStyle(styles);
        save.setMaxSize(100,100);
        Button history=new Button("History");
        history.setMaxSize(100,100);
        history.setStyle(styles);
       // root.getStylesheets().add("neu.css");
        VBox vBox=new VBox(20,start,stop,reset,l1,speed,l2,zoom,save,history);
        vBox.getStylesheets().add("neu.css");
        vBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.setPadding(new Insets(10,30,10,10));
        root.getChildren().addAll(canvas,vBox);
        Scene scene = new Scene(root, 700, 500);
        //scene.getStylesheets().add("neu.css");
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
                    graphics.fillRect(i * 20, j * 20, 20, 20);
                    graphics.setFill(Color.LAVENDER);
                    graphics.fillRect((i * 20) + 1, (j * 20) + 1, 20 - 2, 20 - 2);
                //}
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}