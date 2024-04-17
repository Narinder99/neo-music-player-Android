package com.example.neomusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.neomusicplayer.player.setWindowFlag;


public class MainActivity extends AppCompatActivity implements com.example.neomusicplayer.songName {

    CircleImageView songsImage;
    ImageView playbtn, searchBtn, infoBtn;
    TextView songName, artistName;//used in mini music player
    SeekBar seekBar;
    String playingSongName; // get Name of the song which is playing
    LinearLayout miniMusicPlayer;
    TabLayout tabLayout;
    TabItem songs, artist, playlists;
    ViewPager viewPager;
    pagerController mpageContoller;
    static ConstraintLayout constraintLayout;
    static Boolean isSelected = false;
    static Boolean isSelectAll = false;
    static Context context;
    static ViewGroup parentView;
    static View view;
    static TextView songSelected;
    static Button selectAll, back, delete, addToPlaylist;

    int x = -1;// use so that when no song is played it send name as no track selected
    Animation animation;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private View decoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // create new player class

        super.onCreate(savedInstanceState);
        context = getApplicationContext();

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

        setContentView(R.layout.activity_main);
        songsImage = findViewById(R.id.songsImage);
        songName = findViewById(R.id.songName);
        artistName = findViewById(R.id.artist_miniPlayer);
        playbtn = findViewById(R.id.playbtn);
        seekBar = findViewById(R.id.seekBar);
        tabLayout = findViewById(R.id.tabLayout);
        songs = findViewById(R.id.songs);
        artist = findViewById(R.id.artist);
        playlists = findViewById(R.id.playlists);
        viewPager = findViewById(R.id.viewPager);
        searchBtn = findViewById(R.id.search_button);
        infoBtn = findViewById(R.id.moreInfo);

        allStaticMembers();
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
        tabLayout.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));

        fragmentController();

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogView();
            }
        });
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMiniPlayer();
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, searchBar.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            return;
        }
        mBackPressed = System.currentTimeMillis();

    }

    @Override
    public void sendName(String Name) {
        x = 1;// used to tell that some song is selected
        playingSongName = Name;
        Log.d("newbest", "senddata: " + playingSongName);
        initMediaPlayer();
        //String name=String.valueOf(postion);
        //songName.setText(name);
    }

    @Override
    protected void onStart() {
        super.onStart();
        miniMusicPlayer = findViewById(R.id.miniMusicPlayer);
        miniMusicPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainPlayer();
            }
        });
        Log.d("Yess", "onCreate: " + x);
        if (x == 1) {
            setScreenView();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decoreView.setSystemUiVisibility(hideBar());
        }
    }

    private void fragmentController() {
        mpageContoller = new pagerController(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(mpageContoller);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public void initMediaPlayer() {
        String songData = MusicManager.songsNameData.get(playingSongName);
        String ArtistName = MusicManager.songsNameArtist.get(playingSongName);
        Uri songResourceUri = Uri.parse(songData);
        MusicManager.getInstance().initMusicplayer(getApplicationContext(), songResourceUri);

        songName.setText(playingSongName);
        artistName.setText(ArtistName);
        try {
            MediaMetadataRetriever mmdr = new MediaMetadataRetriever();
            mmdr.setDataSource(songData);
            byte[] art = mmdr.getEmbeddedPicture();
            Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
            songsImage.setImageBitmap(songImage);
        } catch (NullPointerException e) {
            songsImage.setImageResource(R.drawable.icon);
        }

        MusicManager.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                //Log.d("boyb3", "initMediaPlayer: "+mediaPlayer);
                //    seekBar.setMax(mediaPlayer.getDuration());
                Log.d("nobadboyMainActivity", "onPrepared:" + MusicManager.mediaPlayer.getDuration());
                //mediaPlayer.start();
                //MusicManager.mediaPlayer.seekTo(30);
                MusicManager.getInstance().startMusicPlayer();
                playbtn.setImageResource(R.drawable.ic_pause_black_24dp);
            }
        });

        MusicManager.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // when song is over
                playbtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            }
        });

    }

    public void startMainPlayer() {
        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(playbtn, "playBtn");
        pairs[1] = new Pair<View, String>(songName, "songTittle");
        pairs[2] = new Pair<View, String>(songsImage, "circularView");
        if (x == -1) {//work till songadapter not send name when it send the value get changed x=1
            Toast.makeText(this, "No Track Selected..", Toast.LENGTH_SHORT).show();
        } else {

            Intent intent = new Intent(getApplicationContext(), player.class);
            // intent.putExtra("songTitle",playingSongName);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }

    public void playMiniPlayer() {
        if (MusicManager.mediaPlayer != null && MusicManager.mediaPlayer.isPlaying()) {
            // mediaPlayer.pause();
            MusicManager.getInstance().pauseMusicPlayer();
            playbtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        } else {
            MusicManager.getInstance().startMusicPlayer();
            //mediaPlayer.start();
            playbtn.setImageResource(R.drawable.ic_pause_black_24dp);
        }
    }

    public void setScreenView() {

        String songtitle = MusicManager.songsDataName.get(MusicManager.songData);
        String ArtistName = MusicManager.songsNameArtist.get(songtitle);
        songName.setText(songtitle);
        artistName.setText(ArtistName);
        if (MusicManager.mediaPlayer != null && MusicManager.mediaPlayer.isPlaying()) {
            playbtn.setImageResource(R.drawable.ic_pause_black_24dp);
        } else {
            playbtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
        try {
            MediaMetadataRetriever mmdr = new MediaMetadataRetriever();
            mmdr.setDataSource(MusicManager.songData);
            byte[] art = mmdr.getEmbeddedPicture();
            Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
            songsImage.setImageBitmap(songImage);
        } catch (NullPointerException e) {
            songsImage.setImageResource(R.drawable.icon);
        }
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

    public void showDialogView() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.option_menu, null);
        TextView shuffleAll, themes, equaliser, settings, shareApp;
        shuffleAll = view.findViewById(R.id.shuffle_all);
        themes = view.findViewById(R.id.themes);
        equaliser = view.findViewById(R.id.equaliser);
        settings = view.findViewById(R.id.setting);
        shareApp = view.findViewById(R.id.share_app);

        mbuilder.setView(view);
        AlertDialog dialog = mbuilder.create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.RIGHT | Gravity.TOP);


        dialog.show();
        dialog.getWindow().setLayout(500, 770);

        shuffleAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.queueSongsListName = "shuffleSongs";
                shuffleArray();
                x = 1;
                playingSongName = MusicManager.songsDataName.get(MusicManager.songsPositionData.get("" + (MusicManager.shuffledArray.get(0) + 1) + ""));
                initMediaPlayer();
            }
        });
    }

    static public void addSelectView() {
        View v = constraintLayout.getViewById(R.id.setting);
        parentView = (ViewGroup) v.getParent();
        Log.d("hllllll", "onClick: " + parentView);
        final int index = parentView.indexOfChild(v);
        Log.d("rocks", "onClick: " + index);
        parentView.removeView(v);
        parentView.addView(view, index);
    }

    static public void removeSelectView() {
        View v = constraintLayout.getViewById(R.id.setting);
        parentView.removeViewAt(0);
        parentView.addView(v, 0);
    }

    static public void incrementSelectedSongs() {

        Log.d("iamgood", "incrementSelectedSongs: " + MusicManager.selectedSongs.size());
        songSelected.setText(MusicManager.selectedSongs.size() + " Selected ");
    }

    public void allStaticMembers() {
        constraintLayout = findViewById(R.id.setting);
        view = View.inflate(context, R.layout.multiselect_view, null);
        songSelected = view.findViewById(R.id.noOfsongs);
        back = view.findViewById(R.id.backButton);
        addToPlaylist = view.findViewById(R.id.addToplaylistButton);
        selectAll = view.findViewById(R.id.selectAllButton);
        delete = view.findViewById(R.id.deleteSongsButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSelectView();
                isSelected = false;
                MusicManager.selectedSongs.clear();
                fragmentController();
            }
        });

        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectAll = true;
                fragmentController();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "This Button Is not Working Now!!", Toast.LENGTH_SHORT).show();
            }
        });
        addToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = getLayoutInflater().inflate(R.layout.add_playlist_dialog, null);
                TextView createNewPlaylist=view.findViewById(R.id.createNewPlaylist);

                // Recycler View
                RecyclerView recyclerView = view.findViewById(R.id.addPlaylistRecyclerView);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new addPlaylistAdapter(MainActivity.this));

                // Alert Dialog
                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.getWindow().setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = 800;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);

                // Button Listener
                createNewPlaylist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Working", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder mbuilder=new AlertDialog.Builder(MainActivity.this);
                        View view=getLayoutInflater().inflate(R.layout.dialog_view,null);
                        final EditText playlistName=view.findViewById(R.id.playlistName);
                        TextView savePlaylistName=view.findViewById(R.id.saveNewPlaylist);
                        mbuilder.setView(view);
                        final AlertDialog dialog=mbuilder.create();
                        dialog.show();
                        savePlaylistName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // String name=String.copyValueOf();
                                DatabaseClass objDatabaseClass = new DatabaseClass(MainActivity.this);
                                Log.d("newnewnew", "onClick: "+playlistName.getText());
                                objDatabaseClass.createPlaylistNamesDataTabel(String.valueOf(playlistName.getText()));
                                dialog.cancel();
                            }
                        });
                    }
                });
            }
        });
    }

    public void shuffleArray() {
        Random randomNumber = new Random();
        int x = 0;
        int y;
        ArrayList<Integer> shuffledArray = new ArrayList<>();
        while (x < MusicManager.songNameImage.size()) {
            y = randomNumber.nextInt(MusicManager.songNameImage.size());
            Log.d("checkworking", "onClick: " + y);
            if (!shuffledArray.contains(y)) {
                shuffledArray.add(y);
                x++;
            }

        }
        MusicManager.shuffledArray = shuffledArray;
        Log.d("arraywegot", "shuffleArray: " + shuffledArray);
    }
}