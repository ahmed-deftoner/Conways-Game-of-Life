package com.example.sda_project.TextFile;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public interface TextFile {
    void ReadNames();
    void SaveText(String names, ArrayList<Integer> arr);
    void ReadCellInfo(String name,ArrayList<Integer> arr);
    ObservableList<String> getData();
}
