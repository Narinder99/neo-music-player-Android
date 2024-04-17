package com.example.neomusicplayer;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class searchBar extends AppCompatActivity {

    TextView cancelText;
    EditText searchview;
    RecyclerView recyclerView;
    ArrayList<String> nameFilteredList = new ArrayList<>();
    ArrayList<String> artistFilteredList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar);
        cancelText=findViewById(R.id.cancelSearchBar);
        recyclerView=findViewById(R.id.SearchBar_RecyclerView);
        searchview=findViewById(R.id.searchView);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
                recyclerView.setAdapter(new searchBarAdapter(searchBar.this,nameFilteredList,artistFilteredList));
            }
        });
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void filter(String Data) {
        nameFilteredList.clear();
        artistFilteredList.clear();
        int x=1;
        while (x<=MusicManager.songsNameData.size()){
            if(MusicManager.songsDataName.get(MusicManager.songsPositionData.get(""+x+"")).toLowerCase().contains(Data.toLowerCase())){
                nameFilteredList.add(MusicManager.songsDataName.get(MusicManager.songsPositionData.get(""+x+"")));
            }
            if (MusicManager.artists.size()>=x){
            if (MusicManager.artists.get(x-1).toLowerCase().contains(Data.toLowerCase())){
                artistFilteredList.add(MusicManager.artists.get(x-1));}}
                x++;
        }
        Log.d("newname", "filter: "+nameFilteredList);
        Log.d("newartist", "filter: "+artistFilteredList);
    }
}
