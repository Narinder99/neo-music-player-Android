package com.example.neomusicplayer;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.LinearLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.neomusicplayer.player.setWindowFlag;

public class songsByArtists extends AppCompatActivity implements songByArtistAdapterToMainClass {
    TextView artistName, noOfTracks, songName, artistMiniplayer;
    LinearLayout linearLayout;
    ImageView artistImage, plybtn;
    CircleImageView songsImage;
    View decoreView;
    int x=-1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decoreView = getWindow().getDecorView();
        decoreView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decoreView.setSystemUiVisibility(hideBar());
                }
            }
        });
        statusBar();
        setContentView(R.layout.songs_by_artists);
        Intent intent = getIntent();

        artistName = findViewById(R.id.artistName);
        noOfTracks = findViewById(R.id.noOfTacks);
        artistImage = findViewById(R.id.artistImage);
        songName = findViewById(R.id.songName);
        artistMiniplayer = findViewById(R.id.artist_miniPlayer);
        plybtn = findViewById(R.id.playbtn);
        songsImage = findViewById(R.id.songsImage);
        RecyclerView recyclerView=findViewById(R.id.songsByArtistRecyclerView);
        linearLayout=findViewById(R.id.miniMusicPlayer);
        noOfTracks=findViewById(R.id.noOfTacks);

        //get position of artist which is selected
        int position = (int) intent.getSerializableExtra("position");

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //to get Names Of Songs which is by same Artist
        DatabaseClass databaseClass=new DatabaseClass(this);
        ArrayList<String> songNamesByArtist=databaseClass.songNameByArtists(MusicManager.artists.get(position));
        recyclerView.setAdapter(new songsByArtistAdapter(this,songNamesByArtist,MusicManager.artists.get(position)));

        plybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    playMiniPlayer();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainPlayer();
            }
        });
        setScreenView();

        artistName.setText(MusicManager.artists.get(position));
        noOfTracks.setText(""+songNamesByArtist.size()+" Track");
        artistImage.setImageBitmap(MusicManager.songNameImage.get(MusicManager.songsArtistName.get(MusicManager.artists.get(position))));

    }

    private int hideBar() {
        return //View.SYSTEM_UI_FLAG_FULLSCREEN// status bar hide
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }

    public void statusBar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void playMiniPlayer() {
        if (MusicManager.mediaPlayer != null && MusicManager.mediaPlayer.isPlaying()) {
            // mediaPlayer.pause();
            MusicManager.getInstance().pauseMusicPlayer();
            plybtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        } else {

            MusicManager.getInstance().startMusicPlayer();
            //mediaPlayer.start();
            plybtn.setImageResource(R.drawable.ic_pause_black_24dp);
        }
    }

    public void setScreenView() {
        String songtitle = MusicManager.songsDataName.get(MusicManager.songData);
        String ArtistName = MusicManager.songsNameArtist.get(songtitle);
        songName.setText(songtitle);
        artistMiniplayer.setText(ArtistName);
        if (MusicManager.mediaPlayer != null && MusicManager.mediaPlayer.isPlaying()) {
            plybtn.setImageResource(R.drawable.ic_pause_black_24dp);
        } else {
            plybtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
        try {
            if (MusicManager.songData.equals("No Track")) {
                MediaMetadataRetriever mmdr = new MediaMetadataRetriever();
                mmdr.setDataSource(MusicManager.songData);
                byte[] art = mmdr.getEmbeddedPicture();
                Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
                songsImage.setImageBitmap(songImage);
            }
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void playNewSong(String songName) {
        String songData=MusicManager.songsNameData.get(songName);
        Uri uri=Uri.parse(songData);
        MusicManager.getInstance().initMusicplayer(this,uri);
        MusicManager.getInstance().startMusicPlayer();
        x=1;
        setScreenView();
    }

    public void startMainPlayer(){
        Pair[] pairs=new Pair[3];
        pairs[0]=new Pair<View, String>(plybtn,"playBtn");
        pairs[1]=new Pair<View, String>(songName,"songTittle");
        pairs[2]=new Pair<View, String>(songsImage,"circularView");
        if(x==-1){//work till songadapter not send name when it send the value get changed x=1
            Toast.makeText(this, "No Track Selected..", Toast.LENGTH_SHORT).show();
        }
        else {

            Intent intent = new Intent(songsByArtists.this, player.class);
            // intent.putExtra("songTitle",playingSongName);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(songsByArtists.this,pairs);
                startActivity(intent,options.toBundle());
            }
            else {
                startActivity(intent);
            }
        }
    }
}
