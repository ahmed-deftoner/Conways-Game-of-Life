package com.example.sda_project.TextFile;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class TextReader implements TextFile {

    private ObservableList<String> data = FXCollections.observableArrayList();

    public void ReadNames(){
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
    }

    public ObservableList<String> getData(){
        return data;
    }

    public void SaveText(String name,ArrayList<Integer> arr){
        boolean found=false;
        for (String datum : data) {
            if (Objects.equals(datum, name)) {
                found = true;
                break;
            }
        }
        if(!found)
            data.add(name);
        FileWriter fw;
        try {
            fw=new FileWriter(name+".txt");
            List<Integer> rows = new ArrayList<>();
            List<Integer> column = new ArrayList<>();
            for(int i=1;i<arr.size();++i) {
                if(i % 2 != 0)
                    rows.add( arr.get(i));
                else
                    column.add( arr.get(i));
            }
            System.out.println(rows.size());
            fw.write(arr.get(0) + "\n");
            fw.write(rows.size() + "\n");
            for(int i=0;i<rows.size();++i)
                fw.write(rows.get(i)+"\n");
            for(int i=0;i<column.size();++i)
                fw.write(column.get(i)+"\n");
            fw.close();
            fw=new FileWriter("names.txt",true);
            fw.write(name+"\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ReadCellInfo(String val,ArrayList<Integer> arr){
        try {
            Scanner scanner = new Scanner(new File(val+".txt"));
            int [] tall = new int [1000];
            int i = 0;
            while(scanner.hasNextInt())
            {
                tall[i++] = scanner.nextInt();
            }
            for(int j=0;j<i;++j){
                arr.add(tall[j]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
