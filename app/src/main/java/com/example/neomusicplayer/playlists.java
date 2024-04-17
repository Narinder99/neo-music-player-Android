package com.example.neomusicplayer;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.EditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;


public class playlists extends Fragment {

    LinearLayout addLayout;

    public playlists() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_playlists,container,false);
        addLayout =view.findViewById(R.id.addPlaylist);
        RecyclerView recyclerView=view.findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new playlistAdapter(getContext()));
        final DatabaseClass objDatabaseClass=new DatabaseClass(getContext());

        addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbuilder=new AlertDialog.Builder(getContext());
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
                        Log.d("newnewnew", "onClick: "+playlistName.getText());
                        objDatabaseClass.createPlaylistNamesDataTabel(String.valueOf(playlistName.getText()));
                        dialog.cancel();
                    }
                });
            }
        });
        return view;
    }
}
