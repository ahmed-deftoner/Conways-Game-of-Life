package com.example.sda_project.BL;

public class Grid {
    private int[][] gridArray=new int[100][100];
    private int gridSize;

    public Grid(){
        gridSize=20;
        for(int i=0;i<100;++i)
            for(int j=0;j<100;++j)
                gridArray[i][j]=0;
    }

    public void setGridArray(int[][] gridArray) {
        this.gridArray = gridArray;
    }

    public void setGridArray(int i, int j, int c) {
        gridArray[i][j]=c;
    }

    public int getGridArray(int i,int j){
        return gridArray[i][j];
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getGridSize(){
        return gridSize;
    }

    public void Reset(){
        for(int i=0;i<100;++i)
            for(int j=0;j<100;++j)
                gridArray[i][j]=0;
    }
}
