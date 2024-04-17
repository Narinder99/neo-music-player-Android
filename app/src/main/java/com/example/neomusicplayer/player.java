package com.example.neomusicplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.FrameMetricsAggregator;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.SeekBar;
import android.widget.Toast;

import com.jgabrielfreitas.core.BlurImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class player extends AppCompatActivity implements queueToPlayer {

    ImageView plyBtn;
    TextView currentDuration,songMaxDuration,songTitle,artistView;
    BlurImageView songsBackgroundImage;
    CircleImageView songsForegroundImage;
    QueueDialog queueDialog;
    FragmentManager fragmentManager;

    SeekBar seekBar;
    Runnable runnable;
    Handler handler = new Handler();
    RelativeLayout relativeLayout;

    int songDurationByYou;

    private View decoreView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decoreView=getWindow().getDecorView();
        decoreView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility==0){
                    decoreView.setSystemUiVisibility(hideBar());
                }
            }
        });
        statusBar();

        setContentView(R.layout.player);
        seekBar=findViewById(R.id.seekBar);
       // seekBar.getSolidColor(R.color.White);
        artistView=findViewById(R.id.artist_view);
        songTitle=findViewById(R.id.songTitle);
        songsBackgroundImage=findViewById(R.id.music_background_image);
        songsForegroundImage=findViewById(R.id.music_foreground_image);
        relativeLayout=findViewById(R.id.musicPlayer);
        currentDuration=findViewById(R.id.currentDuration);
        songMaxDuration=findViewById(R.id.songMaxDuration);

       fragmentManager=getSupportFragmentManager();
       queueDialog=new QueueDialog(getApplicationContext(),player.this);
       songsForegroundImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               queueDialog.show(fragmentManager,"Don't Know What To Write Here");
           }
       });

        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(player.this) {
            public void onSwipeTop() {
                //queueDialog.show(fragmentManager,"any");
                Toast.makeText(player.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                //Toast.makeText(player.this, "right", Toast.LENGTH_SHORT).show();
                playPreviousSong();
            }
            public void onSwipeLeft() {
                //Toast.makeText(player.this, "left", Toast.LENGTH_SHORT).show();
                playNextSong();
            }
            public void onSwipeBottom() {
               // Toast.makeText(player.this, "bottom", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

        });

        plyBtn=findViewById(R.id.plybtn);
        plyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicManager.mediaPlayer!=null&&MusicManager.mediaPlayer.isPlaying()){
                    MusicManager.getInstance().pauseMusicPlayer();
                    Toast.makeText(player.this, "Hello", Toast.LENGTH_SHORT).show();
                    plyBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }else{
                    MusicManager.getInstance().startMusicPlayer();
                    Toast.makeText(player.this, "Buddy", Toast.LENGTH_SHORT).show();
                    plyBtn.setImageResource(R.drawable.ic_pause_black_24dp);
                }
            }
        });
        seekBarListener();
    }
    public void seekBarListener(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentDuration.setText(milliSecondsToTimer(progress));
                if(milliSecondsToTimer(MusicManager.mediaPlayer.getCurrentPosition()).equals(milliSecondsToTimer(MusicManager.mediaPlayer.getDuration()))){
                   try {
                       playNextSong();
                   }catch (Exception e){}
                }
                if(fromUser) {
                    Log.d("hellohe", "onProgressChanged: " + progress);
                    MusicManager.mediaPlayer.seekTo(progress);//seek the song
                    songDurationByYou = progress;
                    // seekBar.setProgress(progress); // set the seek
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("helloheh", "onProgressChanged:1 "+songDurationByYou);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("helloheh", "onProgressChanged: "+songDurationByYou);
            }
        });

    }

    public void seekBarCycle(){
        seekBar.setProgress(MusicManager.mediaPlayer.getCurrentPosition());

        if (MusicManager.mediaPlayer.isPlaying()){
            Log.d("000000001", "seekBarCycle: "+MusicManager.mediaPlayer.getCurrentPosition());

            Log.d("00000000", "seekBarCycle: "+MusicManager.mediaPlayer.getDuration());
            runnable = new Runnable() {
                @Override
                public void run() {

                    seekBarCycle();
                }
            };
            handler.postDelayed(runnable,1);
        }
    }

    public void setScreenView(){

        String songtitle =MusicManager.songsDataName.get(MusicManager.songData);
        Log.d("asdfghj", "setScreenView: "+songtitle);
        String songData=MusicManager.songsNameData.get(songtitle);
        Log.d("asdfghj1", "setScreenView: "+songData);
        artistView.setText(MusicManager.songsNameArtist.get(songtitle));
        songMaxDuration.setText(milliSecondsToTimer(MusicManager.mediaPlayer.getDuration()));
        try {
            MediaMetadataRetriever mmdr= new MediaMetadataRetriever();
            mmdr.setDataSource(songData);
            byte[] art = mmdr.getEmbeddedPicture();
            Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
            songsBackgroundImage.setImageBitmap(songImage);
            songsForegroundImage.setImageBitmap(songImage);
            songsBackgroundImage.setBlur(18);
        }
        catch (Exception e){
            songsForegroundImage.setImageResource(R.drawable.icon1);
        }

        songTitle.setText(songtitle);

        if(MusicManager.mediaPlayer!=null&&MusicManager.mediaPlayer.isPlaying()){
            plyBtn.setImageResource(R.drawable.ic_pause_black_24dp);
        }else{
            plyBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }

        if(MusicManager.mediaPlayer!=null){
            seekBar.setMax(MusicManager.mediaPlayer.getDuration());
            seekBarCycle();}
    }

    public void playNextSong(){
        Toast.makeText(this, "How ae you", Toast.LENGTH_SHORT).show();
        int position;
        String songData;
        switch (MusicManager.queueSongsListName){
            case "allMusic":
                position= Integer.parseInt(MusicManager.songsNamePosition.get(MusicManager.songsDataName.get(MusicManager.songData)));
                position++;
                songData=MusicManager.songsPositionData.get(""+position+"");
                break;
            case "songsByUser":
                position=MusicManager.queueSongs.indexOf(MusicManager.songsDataName.get(MusicManager.songData));

                Log.d("0010011", "playNextSong: "+MusicManager.queueSongs.get(position));
                position++;
                songData=MusicManager.songsNameData.get(MusicManager.queueSongs.get(position));
                Log.d("0010012", "playNextSong: "+MusicManager.queueSongs.get(position));
                break;
            case "shuffleSongs":
                //Log.d("001001", "playNextSong: "+Integer.parseInt(MusicManager.songsNamePosition.get(MusicManager.songsDataName.get(MusicManager.songData))));
                position=MusicManager.shuffledArray.indexOf(Integer.parseInt(MusicManager.songsNamePosition.get(MusicManager.songsDataName.get(MusicManager.songData)))-1);
                //Log.d("0010011", "playNextSong: "+position);
                position++;
                //Log.d("0010011", "playNextSong: "+MusicManager.shuffledArray.get(position));
                songData=MusicManager.songsPositionData.get(""+(MusicManager.shuffledArray.get(position)+1)+"");
                break;
                default:
                    songData=null;
        }
        Uri uri= Uri.parse(songData);
        MusicManager.getInstance().initMusicplayer(getApplicationContext(),uri);
        MusicManager.getInstance().startMusicPlayer();


        setScreenView();
    }

    public void playPreviousSong(){
        int position;
        String songData;
        switch (MusicManager.queueSongsListName){
            case "allMusic":
                position= Integer.parseInt(MusicManager.songsNamePosition.get(MusicManager.songsDataName.get(MusicManager.songData)));
                if(position!=1){
                    position--;}
                    songData=MusicManager.songsPositionData.get(""+position+"");
                    break;
            case "songsByUser":
                position=MusicManager.queueSongs.indexOf(MusicManager.songsDataName.get(MusicManager.songData));
                if (position!=0){
                position--;}
                songData=MusicManager.songsNameData.get(MusicManager.queueSongs.get(position));
                break;
            case "shuffleSongs":
                //Log.d("001001", "playNextSong: "+Integer.parseInt(MusicManager.songsNamePosition.get(MusicManager.songsDataName.get(MusicManager.songData))));
                position=MusicManager.shuffledArray.indexOf(Integer.parseInt(MusicManager.songsNamePosition.get(MusicManager.songsDataName.get(MusicManager.songData)))-1);
                //Log.d("0010011", "playNextSong: "+position);
                if (position!=0){
                    position--;
                }
                //Log.d("0010011", "playNextSong: "+MusicManager.shuffledArray.get(position));
                songData=MusicManager.songsPositionData.get(""+(MusicManager.shuffledArray.get(position)+1)+"");
                break;
                default:
                    songData=null;
        }

        Uri uri= Uri.parse(songData);
        MusicManager.getInstance().initMusicplayer(getApplicationContext(),uri);
        MusicManager.getInstance().startMusicPlayer();
        setScreenView();
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void statusBar(){
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

    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString =  minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private int hideBar(){
        return //View.SYSTEM_UI_FLAG_FULLSCREEN
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            decoreView.setSystemUiVisibility(hideBar());
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        setScreenView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
    }

    @Override
    public void start(String songData) {
        Log.d("hellobo", "start: he"+songData);
        queueDialog.dismiss();
        queueDialog.show(fragmentManager,"Don't Know What To Write Here");
        setScreenView();
    }
}