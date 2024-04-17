package com.example.neomusicplayer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

public class playlistSongs extends AppCompatActivity {
    TextView playListName;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_songs);
        playListName=findViewById(R.id.playlistName);
        recyclerView=findViewById(R.id.recyclerViewPlayListSongs);
        Intent intent=getIntent();
        String name= (String) intent.getSerializableExtra("playlistName");
        playListName.setText(name);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new playlistSongsAdapter(getApplicationContext()));

    }
}
