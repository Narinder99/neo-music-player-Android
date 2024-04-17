package com.example.neomusicplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class searchBarRecyclerViewAdapter extends RecyclerView.Adapter<searchBarRecyclerViewAdapter.searchBarRecyclerViewHolder> {

    ArrayList<String> FilteredList;
    String songNameOrArtist;
    Context context;

    public searchBarRecyclerViewAdapter(ArrayList<String> FilteredList,String songNameOrArtist,Context context){
        this.FilteredList=FilteredList;
        this.songNameOrArtist=songNameOrArtist;
        this.context=context;
    }

    @NonNull
    @Override
    public searchBarRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (songNameOrArtist.equals("songName")) {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_view_in_search_bar, parent, false);
        }
        else
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_view, parent, false);
        return new searchBarRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull searchBarRecyclerViewHolder holder, int position) {
        if(songNameOrArtist.equals("songName")){
            holder.songsView.setText(FilteredList.get(position));
            holder.artistView.setText(MusicManager.songsNameArtist.get(FilteredList.get(position)));
        }
        else if(songNameOrArtist.equals("artistName")){
            holder.artistView.setText(FilteredList.get(position));
            try {

                holder.artistImageView.setImageBitmap(MusicManager.songNameImage.get(MusicManager.songsArtistName.get(FilteredList.get(position))));
            }catch (Exception e){}
        }
        else{}
    }

    @Override
    public int getItemCount() {
        return FilteredList.size();
    }

    public class searchBarRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView songsView,artistView;
        ImageView artistImageView;
        LinearLayout linearLayout;

        public searchBarRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);


            if (songNameOrArtist.equals("songName")){
                artistView=itemView.findViewById(R.id.artist_view);
                songsView = itemView.findViewById(R.id.songs_view);
                linearLayout=itemView.findViewById(R.id.songs_View_Layout);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.songs_View_Layout:
                                int x=getAdapterPosition();
                                MusicManager.queueSongsListName="singleTrack";
                                    String songName=FilteredList.get(x);
                                    Uri uri=Uri.parse(MusicManager.songsNameData.get(songName));
                                    MusicManager.getInstance().initMusicplayer(context,uri);
                                    MusicManager.getInstance().startMusicPlayer();
                                    Intent intent=new Intent(context,player.class);
                                    context.startActivity(intent);


                        }
                    }
                });
            }
            else if (songNameOrArtist.equals("artistName")){
                artistImageView=itemView.findViewById(R.id.artist_Image);
                artistView=itemView.findViewById(R.id.artist_view);
                artistView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int x=getAdapterPosition();
                        MusicManager.queueSongsListName="singleTrack";
                        String artistName=FilteredList.get(x);
                        //here we use position because in songByAtist we do using position
                        int position=MusicManager.artists.indexOf(artistName);
                        Intent intent=new Intent(context,songsByArtists.class);
                        intent.putExtra("position",position);
                        context.startActivity(intent);
                        
                    }
                });
            }


        }
    }
}
