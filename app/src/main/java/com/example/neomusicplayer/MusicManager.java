package com.example.neomusicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;

public class MusicManager {
    private static MusicManager refrence=null;
    static MediaPlayer mediaPlayer;
    static String songData="No Track";
    static int noOfArtist=1;
    static String queueSongsListName="noListSelected";
    static ArrayList<String> artists=new ArrayList<>();
    static ArrayList<String> queueSongs = new ArrayList<>();
    static HashMap<String,String> songsNameData=new HashMap<String, String>();
    static HashMap<String,String> songsNamePosition=new HashMap<String, String>();
    static HashMap<String,String> songsPositionData=new HashMap<String, String>();
    static HashMap<String,String> songsDataName=new HashMap<String, String>();
    static HashMap<String, Bitmap> songNameImage=new HashMap<>();
    static HashMap<String, String> songsNameArtist=new HashMap<>();
    static HashMap<String, String> songsArtistName=new HashMap<>();
    static ArrayList<String> folderNames=new ArrayList<>();
    static ArrayList<String> songsByFolderName=new ArrayList<>();
    static ArrayList<String > selectedSongs = new ArrayList<>();
    static ArrayList<Integer> shuffledArray= new ArrayList<>();

    public static MusicManager getInstance(){
        if(refrence==null){
            refrence=new MusicManager();
        }
        return refrence;
    }

    public void initMusicplayer(Context context, Uri songResourceUri){
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.reset();
        }
        mediaPlayer = MediaPlayer.create(context,songResourceUri);
        songData=songResourceUri.toString();
       // mediaPlayer.seekTo(30);
    }
    public void startMusicPlayer(){
        try {
            mediaPlayer.start();
        }catch (Exception e){

        }

    }
    public void pauseMusicPlayer(){
        mediaPlayer.pause();
    }

    public void transfer(){
        int x=0;
        while (x<=songsNameData.size()){

        }
    }
}
