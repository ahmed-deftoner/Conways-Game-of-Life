package com.example.sda_project;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HelloApplication extends Application {
    private double x;
    private double y;
    private int[][] gridarr=new int[100][100];
    private int gridsize=20;
    private double speed=1;
    private ObservableList<String> data = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws IOException {

        Scanner scanner = null;
        try {
            scanner = new Scanner( new File("names.txt") );
            while (scanner.hasNext()) {
                String text = scanner.useDelimiter("\n").next();
                data.add(text);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(int i=0;i<100;++i)
            for(int j=0;j<100;++j)
                gridarr[i][j]=0;
        HBox root = new HBox(10);
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
                     graphics.fillRect(roundx+1,roundy+1,gridsize-2,gridsize-2);
                }
        );
        AnimationTimer runAnimation = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // only update once every second
                if ((now - lastUpdate) >= TimeUnit.MILLISECONDS.toNanos((long) (500*speed))) {
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
                    for(int i=0;i<100;++i)
                        for(int j=0;j<100;++j)
                            gridarr[i][j]=0;
                    draw(graphics);
                }
        );
        reset.setMaxSize(100,100);
        Label l1=new Label("Speed");
        l1.setPadding(new Insets(0,50,0,0));
        Slider speed = new Slider(0, 10, 5);
        speed.valueProperty().addListener(
                (observable, oldvalue, newvalue) ->
                {
                    double i = newvalue.doubleValue();
                    this.speed=i;
                });
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
                    if (result.isPresent()) {
                        String name=result.get();
                        boolean found=false;
                        for(int i=0;i<data.size();++i){
                            if(data.get(i)==name){
                                found=true;
                                break;
                            }
                        }
                        if(found==false)
                            data.add(name);
                        FileWriter fw = null;
                        try {
                            fw=new FileWriter(name+".txt");
                            List<Integer> rows = new ArrayList<>();
                            List<Integer> column = new ArrayList<>();
                            int counter=0;
                            for(int i=0;i<100;++i) {
                                for (int j = 0; j < 100; ++j) {
                                    if(gridarr[i][j]==1) {
                                        counter++;
                                        rows.add(i);
                                        column.add(j);
                                    }
                                }
                            }
                            fw.write(gridsize+"\n");
                            fw.write(counter+"\n");
                            for(int i=0;i<counter;++i)
                                fw.write(rows.get(i)+"\n");
                            for(int i=0;i<counter;++i)
                                fw.write(column.get(i)+"\n");
                            fw.close();
                            fw=new FileWriter("names.txt",true);
                            fw.write(name+"\n");
                            fw.close();
                        } catch (IOException e) {
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
                    ListView<String> listView = new ListView<String>(data);
                    listView.setPrefSize(200, 250);

                    listView.setItems(data);
                    listView.getSelectionModel().selectedItemProperty().addListener(
                            (ObservableValue<? extends String> ov, String old_val,
                             String new_val) -> {
                                try {
                                    Scanner scanner2 = new Scanner(new File(new_val+".txt"));
                                    int [] tall = new int [100];
                                    int i = 0;
                                    while(scanner2.hasNextInt())
                                    {
                                        tall[i++] = scanner2.nextInt();
                                    }
                                    gridsize=tall[0];
                                    for(int j=2;j<=tall[1];++j){
                                        gridarr[10][10]=1;
                                        gridarr[tall[j]][tall[j+tall[1]]]=1;
                                        System.out.println(tall[j]);
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
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
        //history.setStyle(styles);
       // root.getStylesheets().add("neu.css");
        VBox vBox=new VBox(20,start,stop,reset,l1, speed,l2,zoomIn,zoomOut,save,history);
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

    public void tick(GraphicsContext graphics) {
        int[][] next = new int[100][100];

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                int neighbors = countAliveNeighbors(i, j);

                if (neighbors == 3) {
                    next[i][j] = 1;
                }else if (neighbors < 2 || neighbors > 3) {
                    next[i][j] = 0;
                }else {
                    next[i][j] = gridarr[i][j];
                }
            }
        }
        gridarr = next;
        draw(graphics);
    }

    private int countAliveNeighbors(int i, int j) {
        int sum = 0;
        int iStart = i == 0 ? 0 : -1;
        int iEndInclusive = i == gridarr.length - 1 ? 0 : 1;
        int jStart = j == 0 ? 0 : -1;
        int jEndInclusive = j == gridarr[0].length - 1 ? 0 : 1;

        for (int k = iStart; k <= iEndInclusive; k++) {
            for (int l = jStart; l <= jEndInclusive; l++) {
                sum += gridarr[i + k][l + j];
            }
        }

        sum -= gridarr[i][j];

        return sum;
    }

    public static void main(String[] args) {
        launch();
    }
}