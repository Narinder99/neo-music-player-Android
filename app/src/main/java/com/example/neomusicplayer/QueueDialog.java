package com.example.neomusicplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.zip.Inflater;

public class QueueDialog extends DialogFragment{
Context context;
    RecyclerView recyclerView;
    TextView songsView,artistView;
    ImageView imageView;
    Activity activity;
    public QueueDialog(Context context,Activity activity){
        this.activity=activity;
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.queue_dialog,container,false);
        artistView=view.findViewById(R.id.artist_view);
        songsView = view.findViewById(R.id.songs_view);
        imageView = view.findViewById(R.id.songsImageView);
        artistView.setText(MusicManager.songsNameArtist.get(MusicManager.songsDataName.get(MusicManager.songData)));
        songsView.setText(MusicManager.songsDataName.get(MusicManager.songData));
        imageView.setImageBitmap(MusicManager.songNameImage.get(MusicManager.songsDataName.get(MusicManager.songData)));
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.background_color);
    //    Window window = getDialog().getWindow();
      //  assert window != null;
        //WindowManager.LayoutParams layoutParams = window.getAttributes();
        //layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        //getDialog().getWindow().setAttributes(layoutParams);
        recyclerView=view.findViewById(R.id.queue_RecyclerView);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //if(Integer.parseInt(MusicManager.songsNamePosition.get(MusicManager.songsDataName.get(MusicManager.songData)))>3){
       // recyclerView.scrollToPosition(Integer.parseInt(MusicManager.songsNamePosition.get(MusicManager.songsDataName.get(MusicManager.songData)))-3);}
        recyclerView.setAdapter(new queueDialogAdapter(getContext(),getActivity()));
        Log.d("123456", "onBindViewHolder: ");
        return view;
    }

}
