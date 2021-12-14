package com.example.sda_project;

import com.example.sda_project.BL.GameLogic;
import com.example.sda_project.BL.GameUtils;
import com.example.sda_project.BL.Grid;
import com.example.sda_project.TextFile.TextFile;
import com.example.sda_project.TextFile.TextReader;
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
    private final Grid grid=new Grid();
    private final GameLogic game=new GameLogic();
    private final GameUtils gameUtils=new GameUtils();
    private final TextFile textReader=new TextReader();

    @Override
    public void start(Stage stage) {

        textReader.ReadNames();

        HBox root = new HBox(10);
        Canvas canvas=new Canvas(500,500);
        GraphicsContext graphics=canvas.getGraphicsContext2D();
        draw(graphics);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                      int roundx= (int) mouseEvent.getX()/grid.getGridSize();
                      int roundy=(int) mouseEvent.getY()/ grid.getGridSize();
                      if(grid.getGridArray(roundx,roundy)==0) {
                          graphics.setFill(Color.PURPLE);
                          grid.setGridArray(roundx,roundy,1);
                      }
                      else{
                          graphics.setFill(Color.LAVENDER);
                          grid.setGridArray(roundx,roundy,0);
                      }
                     roundx=roundx*grid.getGridSize();
                     roundy=roundy*grid.getGridSize();
                     graphics.fillRect(roundx+1,roundy+1,grid.getGridSize()-2,grid.getGridSize()-2);
                }
        );
        AnimationTimer runAnimation = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // only update once every second
                if ((now - lastUpdate) >= TimeUnit.MILLISECONDS.toNanos((long) (500*gameUtils.getSpeed()))) {
                    tick(graphics);
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
                    grid.Reset();
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
                    gameUtils.setSpeed(newvalue.doubleValue());
                });
        Label l2=new Label("Zoom");
        l2.setPadding(new Insets(0,50,0,0));
        Button zoomIn=new Button("+");
        zoomIn.setSkin(new MyButtonSkin(zoomIn));
        zoomIn.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                    grid.setGridSize(gameUtils.ZoomIn(grid.getGridSize()));
                    draw(graphics);
                }
        );
        Button zoomOut=new Button("-");
        zoomOut.setSkin(new MyButtonSkin(zoomOut));
        zoomOut.addEventHandler(MouseEvent.MOUSE_CLICKED,
                mouseEvent -> {
                    grid.setGridSize(gameUtils.ZoomOut(grid.getGridSize()));
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
                        ArrayList<Integer> array=new ArrayList<>();
                        array.add(grid.getGridSize());
                        for(int i=0;i<100;++i){
                            for(int j=0;j<100;++j) {
                                 if(grid.getGridArray(i,j)==1) {
                                     array.add(i);
                                     array.add(j);
                                 }
                            }
                        }
                        textReader.SaveText(result.get(), array);
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
                    ListView<String> listView = new ListView<>(textReader.getData());
                    listView.setPrefSize(200, 250);

                    listView.setItems(textReader.getData());
                    listView.getSelectionModel().selectedItemProperty().addListener(
                            (ObservableValue<? extends String> ov, String old_val,
                             String new_val) -> {
                                ArrayList<Integer> array=new ArrayList<>();
                                textReader.ReadCellInfo(new_val,array);
                                grid.setGridSize(array.get(0));
                                for(int i=2;i<array.get(1) + 2;++i) {
                                    grid.setGridArray(array.get(i),array.get(i+array.get(1)),1);
                                }
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
                if (grid.getGridArray(i,j) == 1) {
                    graphics.setFill(Color.gray(0.5, 0.5));
                    graphics.fillRect(i * grid.getGridSize(), j * grid.getGridSize(), grid.getGridSize(), grid.getGridSize());
                    graphics.setFill(Color.PURPLE);
                    graphics.fillRect((i * grid.getGridSize()) + 1, (j * grid.getGridSize()) + 1, grid.getGridSize() - 2, grid.getGridSize() - 2);
                }else {
                    graphics.setFill(Color.gray(0.5, 0.5));
                    graphics.fillRect(i * grid.getGridSize(), j * grid.getGridSize(), grid.getGridSize(), grid.getGridSize());
                    graphics.setFill(Color.LAVENDER);
                    graphics.fillRect((i * grid.getGridSize()) + 1, (j * grid.getGridSize()) + 1, grid.getGridSize() - 2, grid.getGridSize() - 2);
                }
            }
        }
    }

    public void tick(GraphicsContext graphics) {
        game.Step(grid);
        draw(graphics);
    }

    public static void main(String[] args) {
        launch();
    }
}