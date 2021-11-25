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
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class HelloApplication extends Application {
    private double x;
    private double y;
    private int[][] gridarr=new int[100][100];
    private int gridsize=20;

    @Override
    public void start(Stage stage) throws IOException {

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
                      int roundx= (int) x/gridsize;
                      int roundy=(int) y/gridsize;
                      if(gridarr[roundx][roundy]==0) {
                          graphics.setFill(Color.PURPLE);
                          gridarr[roundx][roundy]=1;
                      }
                      else{
                          graphics.setFill(Color.LAVENDER);
                          gridarr[roundx][roundy]=0;
                      }
                     roundx=roundx*gridsize;
                     roundy=roundy*gridsize;
                   //  draw(graphics);
                     graphics.fillRect(roundx+1,roundy+1,gridsize-2,gridsize-2);
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
        reset.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                    for(int i=0;i<100;++i)
                        for(int j=0;j<100;++j)
                            gridarr[i][j]=0;
                    draw(graphics);
                }
        );
        reset.setMaxSize(100,100);
        Label l1=new Label("Speed");
        l1.setPadding(new Insets(0,50,0,0));
        Slider speed=new Slider();
        Label l2=new Label("Zoom");
        l2.setPadding(new Insets(0,50,0,0));
        Button zoomIn=new Button("+");
        zoomIn.setSkin(new MyButtonSkin(zoomIn));
        zoomIn.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                    gridsize+=5;
                    draw(graphics);
                }
        );
        Button zoomOut=new Button("-");
        zoomOut.setSkin(new MyButtonSkin(zoomOut));
        zoomOut.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                    gridsize-=5;
                    draw(graphics);
                }
        );
        Button save=new Button("Save");
        save.setSkin(new MyButtonSkin(save));
        save.setMaxSize(100,100);
        save.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                    TextInputDialog dialog=new TextInputDialog("hehe");
                    dialog.setHeaderText(null);
                    dialog.setTitle("Save Design");
                    dialog.setContentText("Please enter design name");

                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent() && result.get()=="") {
                        //Creating a JSONObject object
                        JSONObject jsonObject = new JSONObject();
                        //Inserting key-value pairs into the json object
                        String name=result.get();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        jsonObject.put("Name", name);
                        jsonObject.put("Time", dtf.format(now));
                        try {
                            FileWriter file = new FileWriter("D:/C#/SDA_project/data.json",true);
                            file.write(jsonObject.toJSONString());
                            file.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        System.out.println("Your name: " + result.get());
                    }
                }
        );
        Button history=new Button("History");
        history.setSkin(new MyButtonSkin(history));
        history.setMaxSize(100,100);
        history.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                    Stage stage1=new Stage();
                    stage1.setTitle("History");
                  //  Scene scene1=new Scene(300,300);
                    stage1.initModality(Modality.APPLICATION_MODAL);
                    stage1.showAndWait();
                }
        );
        //history.setStyle(styles);
       // root.getStylesheets().add("neu.css");
        VBox vBox=new VBox(20,start,stop,reset,l1,speed,l2,zoomIn,zoomOut,save,history);
        vBox.getStylesheets().add("neu.css");
        vBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.setPadding(new Insets(10,30,10,10));
        root.getChildren().addAll(canvas,vBox);
        Scene scene = new Scene(root, 700, 500);
       // scene.getStylesheets().add("neu.css");
        stage.setTitle("Hello!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void draw(GraphicsContext graphics) {
        // clear graphics
        graphics.setFill(Color.LAVENDER);
        graphics.fillRect(0, 0, 500, 500);

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                if (gridarr[i][j] == 1) {
                    graphics.setFill(Color.gray(0.5, 0.5));
                    graphics.fillRect(i * gridsize, j * gridsize, gridsize, gridsize);
                    graphics.setFill(Color.PURPLE);
                    graphics.fillRect((i * gridsize) + 1, (j * gridsize) + 1, gridsize - 2, gridsize - 2);
                }else {
                    graphics.setFill(Color.gray(0.5, 0.5));
                    graphics.fillRect(i * gridsize, j * gridsize, gridsize, gridsize);
                    graphics.setFill(Color.LAVENDER);
                    graphics.fillRect((i * gridsize) + 1, (j * gridsize) + 1, gridsize - 2, gridsize - 2);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}