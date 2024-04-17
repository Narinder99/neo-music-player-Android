package com.example.neomusicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jgabrielfreitas.core.BlurImageView;

import java.util.HashMap;


public class songs extends Fragment implements songName {
    RecyclerView recyclerView;


    String songPath;
    BlurImageView imageView;
    public songs(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("notworking", "onCreateView: ");
        View view =inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView=view.findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        try {
            recyclerView.scrollToPosition(Integer.parseInt(MusicManager.songsNamePosition.get(MusicManager.songsDataName.get(MusicManager.songData))));
        }catch (Exception e){}
        songsAdapter obj=new songsAdapter(getContext());
        recyclerView.setAdapter(obj);
        return view;
    }

    @Override
    public void sendName(String Name) {
        songPath=MusicManager.songsNameData.get(Name);
        Log.d("badboy12", "onBindViewHolder: "+songPath);
      /*  MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(String.valueOf(songPath));
        byte [] data = mmr.getEmbeddedPicture();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        addLayout.setImageBitmap(bitmap);*/
    }

}
