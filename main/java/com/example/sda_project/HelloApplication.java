package com.example.sda_project;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Optional;
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
                   //  draw(graphics);
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
                  //  tick(graphics);
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
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        //First Employee
                        JSONObject Details = new JSONObject();
                        Details.put("Name", name);
                        Details.put("Time", dtf.format(now));
                        Details.put("GridSize", gridsize);
                        JSONArray rows=new JSONArray();
                        JSONArray column=new JSONArray();
                        for(int i=0;i<100;++i) {
                            for (int j = 0; j < 100; ++j) {
                                if(gridarr[i][j]==1) {
                                    rows.add(i);
                                    column.add(j);
                                }
                            }
                        }
                        Details.put("Rows",rows);
                        Details.put("Columns",column);

                        JSONObject obj = new JSONObject();
                        obj.put("game", Details);

                        //Add employees to list
                        JSONArray gameList = new JSONArray();
                        gameList.add(obj);

                        //Write JSON file
                        try (FileWriter file = new FileWriter("D:/C#/SDA_project/data.json",true)) {
                            //We can write any JSONArray or JSONObject instance to the file
                            file.write(gameList.toJSONString());
                            file.flush();

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
                    JSONArray gameList=null;

                    JSONParser jsonParser = new JSONParser();

                    try (FileReader reader = new FileReader("D:/C#/SDA_project/data.json"))
                    {
                        //Read JSON file
                        Object obj = jsonParser.parse(reader);

                        gameList = (JSONArray) obj;
                        System.out.println(gameList);

                        //Iterate over employee array
                        gameList.forEach( emp -> parseGameObject( (JSONObject) emp ) );

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                  /*  JSONParser jsonParser = new JSONParser();
                    try {
                        //Parsing the contents of the JSON file
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("D:/C#/SDA_project/data.json"));
                        //Forming URL
                        System.out.println("Contents of the JSON are: ");
                        System.out.println("Name: "+jsonObject.get("Name"));
                        System.out.println("Time: "+jsonObject.get("Date"));
                        System.out.println("Gridsize: "+jsonObject.get("GridSize"));
                        //Retrieving the array
                        JSONArray rows = (JSONArray) jsonObject.get("Rows");
                        System.out.println("");
                        System.out.println("rows: ");
                        //Iterating the contents of the array
                        Iterator<Integer> iterator = rows.iterator();
                        while(iterator.hasNext()) {
                            System.out.println(iterator.next());
                        }
                        JSONArray cols = (JSONArray) jsonObject.get("Columns");
                        System.out.println("");
                        System.out.println("cols: ");
                        //Iterating the contents of the array
                        iterator=cols.iterator();
                        while(iterator.hasNext()) {
                            System.out.println(iterator.next());
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*/
                  //  Scene scene1=new Scene(300,300);

                    ListView<String> listView = new ListView<String>(data);
                    listView.setPrefSize(200, 250);

                    listView.setItems(data);
                    listView.getSelectionModel().selectedItemProperty().addListener(
                            (ObservableValue<? extends String> ov, String old_val,
                             String new_val) -> {
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

    private void parseGameObject(JSONObject game)
    {
        //Get employee object within list
        JSONObject obj = (JSONObject) game.get("game");

        System.out.println("Contents of the JSON are: ");
        System.out.println("Name: "+obj.get("Name"));
        data.addAll((String) obj.get("Name"));
        System.out.println("Time: "+obj.get("Time"));
        System.out.println("Gridsize: "+obj.get("GridSize"));
        //Retrieving the array
        JSONArray rows = (JSONArray) obj.get("Rows");
        System.out.println("");
        System.out.println("rows: ");
        //Iterating the contents of the array
        Iterator<Integer> iterator = rows.iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        JSONArray cols = (JSONArray) obj.get("Columns");
        System.out.println("");
        System.out.println("cols: ");
        //Iterating the contents of the array
        iterator=cols.iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
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