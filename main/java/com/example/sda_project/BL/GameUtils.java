package com.example.sda_project.BL;

public class GameUtils {
    private double speed;
    private int zoom;

    public GameUtils(){
        speed=1;
        zoom=0;
    }

    public void setSpeed(double speed){
        this.speed=speed;
    }

    public int ZoomIn(int n){
       return  n+=5;
    }

    public int ZoomOut(int n){
        return n-=5;
    }

    public double getSpeed(){
        return speed;
    }


}
