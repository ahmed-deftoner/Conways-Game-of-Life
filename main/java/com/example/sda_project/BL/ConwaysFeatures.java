package com.example.sda_project.BL;

import javafx.collections.ObservableList;

public interface ConwaysFeatures {
    int getGridSize();
    int getGridArray(int i,int j);
    void setGridArray(int i,int j,int x);
    void setSpeed(double val);
    double getSpeed();
    void ZoomIn();
    void ZoomOut();
    void setNameFile(String name);
    ObservableList<String> getTextData();
}
