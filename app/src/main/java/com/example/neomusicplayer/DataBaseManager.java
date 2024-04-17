package com.example.neomusicplayer;

import java.util.ArrayList;

public class DataBaseManager {
    private static DataBaseManager refrence=null;
    static ArrayList<String> playlistNames=new ArrayList<>();
    static ArrayList<String> playlistSongs=new ArrayList<>();
    public static DataBaseManager getInstance(){
        if(refrence==null){
            refrence=new DataBaseManager();
        }
        return refrence;
    }
}
