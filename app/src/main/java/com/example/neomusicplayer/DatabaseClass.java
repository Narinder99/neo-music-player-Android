package com.example.neomusicplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseClass extends SQLiteOpenHelper {
    int x = 1;
    public static final String dataBaseName = "infos.db";
    public static final String allSongDataTable = "dataTable";
    public static final String playlistNamesTable = "playlistTable";
    public static final String col_Position = "songPosition";
    public static final String col_Id = "songId";
    public static final String col_Image = "songImage";
    public static final String col_ArtistName = "songArtistName";
    public static final String col_songData = "songData";
    public static final String col_songName = "songName";
    public static final String col_playlistName = "playlistName";
    // Bitmap imageToStoreBitmap;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] imageInByte;

    int lastposition;
    private static final String createDataTable = "CREATE TABLE " + allSongDataTable + "("
            + col_Position + " INTEGER ,"
            + col_songName + " VARCHAR , "
            + col_ArtistName + " VARCHAR , "
            + col_songData + " VARCHAR , "
            + col_Image + " BLOB ,"
            + col_Id + " VARCHAR " + ");";
    private static final String createPlaylistNameTable = "CREATE TABLE " + playlistNamesTable + "("
            + col_Position + " INTEGER ,"
            + col_playlistName + " VARCHAR " + ");";

    private static final String dropDataTable = " Drop table if exists " + allSongDataTable;
    private static final String dropPlayListNameTable = " Drop table if exists " + playlistNamesTable;

    public DatabaseClass(@Nullable Context context) {
        super(context, dataBaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("1111111", "onCreate: database");
        db.execSQL(createPlaylistNameTable);
        db.execSQL(createDataTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("1111111", "onUpgrade: database");
        db.execSQL(dropDataTable);
        db.execSQL(dropPlayListNameTable);
    }

    public boolean insertSongs(String songName, String artistName, String songData, String id, Bitmap image) {

        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap imageToStoreBitmap = image;
        byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        } catch (Exception e) {

        }

        imageInByte = byteArrayOutputStream.toByteArray();
        ContentValues values = new ContentValues();
        values.put(col_Position, x);
        values.put(col_songName, songName);
        values.put(col_Id, id);
        values.put(col_songData, songData);
        values.put(col_ArtistName, artistName);
        values.put(col_Image, imageInByte);
        long result = db.insert(allSongDataTable, null, values);
        x++;
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void storeDataIntoMusicManager() {
        //stoe data fom hee to Music Manage vaiables
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(" select * from " + allSongDataTable, null);

        if (cursor != null) {
            while ((cursor.moveToNext())) {
                MusicManager.songsDataName.put(cursor.getString(3), cursor.getString(1));
                MusicManager.songsNameArtist.put(cursor.getString(1), cursor.getString(2));
                MusicManager.songsArtistName.put(cursor.getString(2), cursor.getString(1));
                MusicManager.songsNameData.put(cursor.getString(1), cursor.getString(3));
                MusicManager.songsNamePosition.put(cursor.getString(1), cursor.getString(0));
                MusicManager.songsPositionData.put(cursor.getString(0), cursor.getString(3));

                byte[] byteArray = cursor.getBlob(4);
                try {


                    Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    MusicManager.songNameImage.put(cursor.getString(1), bm);
                } catch (Exception e) {
                }
                Log.d("buddyboy", "getDataIntoMusicManager: " + cursor.getString(1));
                //Log.d("lllllllllll", "getData: "+cursor.getString(1));
            }
        }
    }

    public void deleteAllRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("hellllll", "deleteAllRows: ");
        int cursor = db.delete(allSongDataTable, null, null);
    }

    public void getArtistName() {
        SQLiteDatabase db = this.getReadableDatabase();
        int x = 0;
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + col_ArtistName + " from " + allSongDataTable + " ORDER BY " + col_ArtistName + " ASC ", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MusicManager.artists.add(cursor.getString(0));
                Log.d("lkjhgfdsa", "getArtistName: " + cursor.getString(0));

            }
        }
    }

    public ArrayList<String> songNameByArtists(String artistName) {
        ArrayList<String> songNameByArtist = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * from " + allSongDataTable + " WHERE songArtistName = '" + artistName + "'", null);
        Log.d("0001234", "songNameByArtists: '" + artistName + "'");

        while (cursor.moveToNext()) {
            songNameByArtist.add(cursor.getString(1));
            Log.d("000123", "songNameByArtists: " + cursor.getString(1));
        }
        return songNameByArtist;
    }

    public void createPlaylistNamesDataTabel(String playlistName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String createDataTable = "CREATE TABLE " + playlistName + "("
                + col_songData + " VARCHAR " + ");";
        db.execSQL(createDataTable);
        ContentValues obj = new ContentValues();
        obj.put(col_Position, 1);
        obj.put(col_playlistName, playlistName);
        db.insert(playlistNamesTable, null, obj);

        Cursor cursor = db.rawQuery(" select * from " + playlistNamesTable, null);
        cursor.moveToLast();
        if (cursor != null) {
            do {
                Log.d("desijatt", "storePlayListNameIntoDataBaseManagerClass: " + cursor.getString(1));
                DataBaseManager.playlistNames.add(cursor.getString(1));
            }
            while (cursor.moveToNext());
        }
    }

    public void storePlayListNameIntoDataBaseManagerClass() {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(" select * from " + playlistNamesTable, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Log.d("desijatt", "storePlayListNameIntoDataBaseManagerClass: " + cursor.getString(1));
                    DataBaseManager.playlistNames.add(cursor.getString(1));
                }
            }
        } catch (Exception e) {

        }
    }

    public void getFolderNames(){
            SQLiteDatabase db = this.getReadableDatabase();
            int x=0;
            MusicManager.folderNames.clear();
            Cursor cursor = db.rawQuery("SELECT DISTINCT " + col_songData + " from " + allSongDataTable + " ORDER BY " + col_songData + " ASC ", null);
           ArrayList<String> allFolderNames=new ArrayList<>();

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    File file=new File(""+cursor.getString(0)+"");
                    allFolderNames.add(file.getParentFile().getName());
                }
            }
            while (allFolderNames.size()>x+1){
                if (!allFolderNames.get(x).equals(allFolderNames.get(x+1))){
                    MusicManager.folderNames.add(allFolderNames.get(x));
                }x++;
            }
            MusicManager.folderNames.add(allFolderNames.get(allFolderNames.size()-1));
    }

    public void getSongsNameByFolder(String folderName){
        SQLiteDatabase db=this.getReadableDatabase();
        MusicManager.songsByFolderName.clear();
        //Cursor cursor = db.rawQuery("SELECT DISTINCT * from " + allSongDataTable, null);
        Cursor cursor=db.query(allSongDataTable,new String[]{col_songName,col_songData},null,null,col_songData,null,null);
        String folder;
        File file;
        if (cursor!=null){
            while (cursor.moveToNext()){
                file=new File(""+cursor.getString(1)+"");
                folder=file.getParentFile().getName();
                if (folder.equals(folderName)){
                    MusicManager.songsByFolderName.add(cursor.getString(0));
                }
            }
        }
    }

    public void storeSongsInPlaylist(String playlistName,ArrayList<String> songData){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        int x=0;
        while(songData.size()>x){
            Log.d("helllo", "storeSongsInPlaylist: "+songData.get(x));
            contentValues.put(col_songData,songData.get(x));
            db.insert(playlistName,null,contentValues);
            x++;
        }
    }

    public void getPlaylistSongs(String playlistName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(" select DISTINCT * from " + playlistName, null);
        DataBaseManager.playlistSongs.clear();
        if (cursor!=null){
            while (cursor.moveToNext()){
                DataBaseManager.playlistSongs.add(cursor.getString(0));
                Log.d("checkone", "getPlaylistSongs: "+cursor.getString(0));
            }
        }
    }
}