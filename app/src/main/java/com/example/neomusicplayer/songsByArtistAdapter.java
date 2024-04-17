package com.example.neomusicplayer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class songsByArtistAdapter extends RecyclerView.Adapter<songsByArtistAdapter.songViewByArtistHolder> {
    ArrayList<String> songNamesByArtist;
    String artistNameS;
    Context context;
    songByArtistAdapterToMainClass songByArtistAdapterToMainClassobj;
    public songsByArtistAdapter(@NonNull Context context,ArrayList<String> songNamesByArtist,String artistName) {
    this.songNamesByArtist=songNamesByArtist;
    this.artistNameS=artistName;
    this.context=context;
    }

    @NonNull
    @Override
    public songViewByArtistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_view,parent,false);
        return new songViewByArtistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull songViewByArtistHolder holder, int position) {
        holder.songName.setText(songNamesByArtist.get(position));
        holder.artistName.setText(artistNameS);

        try {
            if(MusicManager.songNameImage.get(songNamesByArtist.get(position))!=null){
                holder.songImage.setImageBitmap(MusicManager.songNameImage.get(songNamesByArtist.get(position)));}
            else{
            }
        }catch (Exception e){
        }
    }

    @Override
    public int getItemCount() {
        return songNamesByArtist.size();
    }

    public class songViewByArtistHolder extends RecyclerView.ViewHolder {
        TextView songName,artistName;
        ImageView songImage;
        LinearLayout linearLayout;
        public songViewByArtistHolder(@NonNull View itemView) {
            super(itemView);
            songName=itemView.findViewById(R.id.songs_view);
            artistName=itemView.findViewById(R.id.artist_view);
            songImage=itemView.findViewById(R.id.songsImageView);
            linearLayout=itemView.findViewById(R.id.songs_View_Layout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MusicManager.queueSongsListName="songsByUser";
                    MusicManager.queueSongs=songNamesByArtist;
                    songByArtistAdapterToMainClassobj=(songByArtistAdapterToMainClass)context;
                    songByArtistAdapterToMainClassobj.playNewSong(songNamesByArtist.get(getAdapterPosition()));
                }
            });
        }
    }
}
