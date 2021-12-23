package com.example.sda_project.BL;

import com.example.sda_project.TextFile.TextFile;
import com.example.sda_project.TextFile.TextReader;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ConwaysGame implements Game,ConwaysFeatures{
    private final Grid grid;
    private final GameLogic gameLogic;
    private final TextFile textReader;
    private final GameUtils gameUtils;
    private String nameFile;

    public ConwaysGame(){
        grid=new Grid();
        gameLogic=new GameLogic();
        gameUtils=new GameUtils();
        textReader=new TextReader();
        textReader.ReadNames();
    }

    public void Start(){}

    public void Stop(){}

    public void Step(){
        gameLogic.Step(grid);
    }

    public void Reset(){
        grid.Reset();
    }

    public int getGridSize(){
        return grid.getGridSize();
    }


    public int getGridArray(int i,int j){
        return grid.getGridArray(i,j);
    }

    public void setGridArray(int i,int j,int x){
        grid.setGridArray(i,j,x);
    }

    public void setSpeed(double val){
        gameUtils.setSpeed(val);
    }

    public double getSpeed(){return gameUtils.getSpeed();}

    public void ZoomIn(){
        grid.setGridSize(gameUtils.ZoomIn(grid.getGridSize()));
    }

    public void ZoomOut(){
        grid.setGridSize(gameUtils.ZoomOut(grid.getGridSize()));
    }

    public void setNameFile(String name){
        nameFile=name;
    }

    public void Save(){
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
        textReader.SaveText(nameFile, array);
    }

    public ObservableList<String> getTextData(){
        return textReader.getData();
    }

    public void History(){
        ArrayList<Integer> array=new ArrayList<>();
        textReader.ReadCellInfo(nameFile,array);
        grid.setGridSize(array.get(0));
        for(int i=2;i<array.get(1) + 2;++i) {
            grid.setGridArray(array.get(i),array.get(i+array.get(1)),1);
        }
    }
}
