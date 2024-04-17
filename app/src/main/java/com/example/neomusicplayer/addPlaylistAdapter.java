package com.example.neomusicplayer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.zip.Inflater;
import android.widget.TextView;
import android.widget.Toast;

public class addPlaylistAdapter extends RecyclerView.Adapter<addPlaylistAdapter.addPlaylistViewHolder> {

    Context context;

    public addPlaylistAdapter(Context context){
        this.context=context;
    }
    @NonNull
    @Override
    public addPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_playlist_dialog_view,parent,false);
        return new addPlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull addPlaylistViewHolder holder, final int position) {
        holder.playlistName.setText(DataBaseManager.playlistNames.get(position));
        //Log.d("newtry", "onBindViewHolder: "+DataBaseManager.playlistNames.size());
        holder.playlistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseClass objDatabaseClass = new DatabaseClass(context);
                objDatabaseClass.storeSongsInPlaylist(DataBaseManager.playlistNames.get(position),MusicManager.selectedSongs);
                Toast.makeText(context, "Songs Added..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return DataBaseManager.playlistNames.size();
    }

    public class addPlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView playlistName;
        public addPlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistName=itemView.findViewById(R.id.playlistName);
        }
    }
}
