package com.example.sda_project.UI;

import com.example.sda_project.BL.ConwaysFeatures;
import com.example.sda_project.BL.ConwaysGame;
import com.example.sda_project.BL.Game;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameOfLife extends Application {
    private final ConwaysGame conwaysGame=new ConwaysGame();
    private final Game game =conwaysGame;
    private final ConwaysFeatures feature=conwaysGame;

    @Override
    public void start(Stage stage) {
        HBox root = new HBox(10);
        Canvas canvas=new Canvas(500,500);
        GraphicsContext graphics=canvas.getGraphicsContext2D();
        draw(graphics);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                      int roundx= (int) mouseEvent.getX()/feature.getGridSize();
                      int roundy=(int) mouseEvent.getY()/ feature.getGridSize();
                      if(feature.getGridArray(roundx,roundy)==0) {
                          graphics.setFill(Color.PURPLE);
                          feature.setGridArray(roundx,roundy,1);
                      }
                      else{
                          graphics.setFill(Color.LAVENDER);
                          feature.setGridArray(roundx,roundy,0);
                      }
                     roundx=roundx*feature.getGridSize();
                     roundy=roundy*feature.getGridSize();
                     graphics.fillRect(roundx+1,roundy+1,feature.getGridSize()-2,feature.getGridSize()-2);
                }
        );
        AnimationTimer runAnimation = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // only update once every second
                if ((now - lastUpdate) >= TimeUnit.MILLISECONDS.toNanos((long) (500* feature.getSpeed()))) {
                    game.Step();
                    draw(graphics);
                    lastUpdate = now;
                }
            }
        };
        Button start=new Button("Start");
        start.setSkin(new MyButtonSkin(start));
        start.setMaxSize(100,100);
        start.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                    runAnimation.start();
                }
        );
        Button stop=new Button("Stop");
        stop.setSkin(new MyButtonSkin(stop));
        stop.setMaxSize(100,100);
        stop.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                    runAnimation.stop();
                }
        );
        Button reset=new Button("Reset");
        reset.setSkin(new MyButtonSkin(reset));
        reset.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                    game.Reset();
                    draw(graphics);
                }
        );
        reset.setMaxSize(100,100);
        Label l1=new Label("Speed");
        l1.setPadding(new Insets(0,50,0,0));
        Slider speed = new Slider(0, 10, 5);
        speed.setMajorTickUnit(1);
        speed.valueProperty().addListener(
                (observable, oldvalue, newvalue) ->
                {
                    feature.setSpeed(newvalue.doubleValue());
                });
        Label l2=new Label("Zoom");
        l2.setPadding(new Insets(0,50,0,0));
        Button zoomIn=new Button("+");
        zoomIn.setSkin(new MyButtonSkin(zoomIn));
        zoomIn.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                    feature.ZoomIn();
                    draw(graphics);
                }
        );
        Button zoomOut=new Button("-");
        zoomOut.setSkin(new MyButtonSkin(zoomOut));
        zoomOut.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                    feature.ZoomOut();
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
                    if (result.isPresent()) {
                        feature.setNameFile(result.get());
                        game.Save();
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
                    ListView<String> listView = new ListView<>(feature.getTextData());
                    listView.setPrefSize(200, 250);

                    listView.setItems(feature.getTextData());
                    listView.getSelectionModel().selectedItemProperty().addListener(
                            (ObservableValue<? extends String> ov, String old_val,
                             String new_val) -> {
                                feature.setNameFile(new_val);
                                game.History();
                                draw(graphics);
                                stage1.close();
                                System.out.println(new_val);
                            });
                    StackPane root1 = new StackPane();
                    root1.getChildren().add(listView);
                    stage1.setScene(new Scene(root1, 200, 250));
                    stage1.initModality(Modality.APPLICATION_MODAL);
                    stage1.showAndWait();
                }
        );
        VBox vBox=new VBox(20,start,stop,reset,l1, speed,l2,zoomIn,zoomOut,save,history);
        vBox.getStylesheets().add("neu.css");
        vBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.setPadding(new Insets(10,30,10,10));
        root.getChildren().addAll(canvas,vBox);
        Scene scene = new Scene(root, 700, 500);
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
                if (feature.getGridArray(i,j) == 1) {
                    graphics.setFill(Color.gray(0.5, 0.5));
                    graphics.fillRect(i * feature.getGridSize(), j * feature.getGridSize(), feature.getGridSize(), feature.getGridSize());
                    graphics.setFill(Color.PURPLE);
                    graphics.fillRect((i * feature.getGridSize()) + 1, (j * feature.getGridSize()) + 1, feature.getGridSize() - 2, feature.getGridSize() - 2);
                }else {
                    graphics.setFill(Color.gray(0.5, 0.5));
                    graphics.fillRect(i * feature.getGridSize(), j * feature.getGridSize(), feature.getGridSize(), feature.getGridSize());
                    graphics.setFill(Color.LAVENDER);
                    graphics.fillRect((i * feature.getGridSize()) + 1, (j * feature.getGridSize()) + 1, feature.getGridSize() - 2, feature.getGridSize() - 2);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}