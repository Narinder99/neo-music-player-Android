package com.example.neomusicplayer;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class welcomeScreen extends AppCompatActivity {

    DatabaseClass databaseClass = new DatabaseClass(this);
    Bitmap bitmap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //Toast.makeText(this, "first", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.welcome_screen);
           // Toast.makeText(this, "Middle", Toast.LENGTH_SHORT).show();
            Permission();
            //Intent intent=new Intent(this,MainActivity.class);
            //startActivity(intent);
            Toast.makeText(this, "second", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            Toast.makeText(this, "nooooooooooo", Toast.LENGTH_SHORT).show();
            databaseClass.storeDataIntoMusicManager();
            databaseClass.getArtistName();
            databaseClass.storePlayListNameIntoDataBaseManagerClass();
            startActivity(intent);
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();
    }

    public void Permission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        //  Toast.makeText(MainActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                        songStoreintoDatabase();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        if (response.isPermanentlyDenied()) {
                        }
                        // check for permanent denial of permission
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void songStoreintoDatabase() {
        try {
            databaseClass.deleteAllRows();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,};
        String orderBy = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

        Cursor cursor = this.managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, orderBy);


        while (cursor.moveToNext()) {
            try {
                mmr.setDataSource(String.valueOf(cursor.getString(3)));
                byte[] image = mmr.getEmbeddedPicture();
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            } catch (Exception e) {
                bitmap = null;
            }
            boolean check = databaseClass.insertSongs(cursor.getString(2), cursor.getString(1), cursor.getString(3), cursor.getString(0), bitmap);
            Log.d("letsee1", "Displaymusicname: " + cursor.getString(0));
        }
       // databaseClass.storeDataIntoMusicManager();
        //databaseClass.getArtistName();
    }
}
