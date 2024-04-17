package com.example.neomusicplayer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.LiveFolders;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class folderSongs extends AppCompatActivity {
    TextView folderName;
    CardView playAllButton;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_song_view);
        folderName=findViewById(R.id.folderName);
        recyclerView=findViewById(R.id.recyclerViewFolderSongs);
        Intent intent=getIntent();
        String name= (String) intent.getSerializableExtra("folderName");
        folderName.setText(name);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new folderSongsAdapter(getApplicationContext()));
    }
}
