package com.example.sda_project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class HelloApplication extends Application {
    private double x;
    private double y;
    private int[][] gridarr=new int[500][500];

    @Override
    public void start(Stage stage) throws IOException {
       /* String styles =
                "-fx-background-radius: 22px;"+
                "-fx-border-radius: 22px;" +
                        "-fx-background-color: #e0dce5;"+
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.2, 0.5, 0.2);";*/

        for(int i=0;i<100;++i)
            for(int j=0;j<100;++j)
                gridarr[i][j]=0;
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
                      if(gridarr[roundx][roundy]==0) {
                          graphics.setFill(Color.BLACK);
                          gridarr[roundx][roundy]=1;
                      }
                      else{
                          graphics.setFill(Color.LAVENDER);
                          gridarr[roundx][roundy]=0;
                      }
                      graphics.fillRect(roundx+1,roundy+1,18,18);
                }
        );
        Button start=new Button("Start");
        start.setSkin(new MyButtonSkin(start));
        start.setMaxSize(100,100);
        Button stop=new Button("Stop");
        stop.setSkin(new MyButtonSkin(stop));
        stop.setMaxSize(100,100);
        Button reset=new Button("Reset");
        reset.setSkin(new MyButtonSkin(reset));
        reset.setMaxSize(100,100);
        Label l1=new Label("Speed");
        l1.setPadding(new Insets(0,50,0,0));
        Slider speed=new Slider();
        Label l2=new Label("Zoom");
        l2.setPadding(new Insets(0,50,0,0));
        Slider zoom=new Slider();
        Button save=new Button("Save");
        save.setSkin(new MyButtonSkin(save));
        save.setMaxSize(100,100);
        Button history=new Button("History");
        history.setSkin(new MyButtonSkin(history));
        history.setMaxSize(100,100);
        //history.setStyle(styles);
       // root.getStylesheets().add("neu.css");
        VBox vBox=new VBox(20,start,stop,reset,l1,speed,l2,zoom,save,history);
        vBox.getStylesheets().add("neu.css");
        vBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.setPadding(new Insets(10,30,10,10));
        root.getChildren().addAll(canvas,vBox);
        Scene scene = new Scene(root, 700, 500);
       // scene.getStylesheets().add("neu.css");
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