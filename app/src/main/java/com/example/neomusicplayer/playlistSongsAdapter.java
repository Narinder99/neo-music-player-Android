package com.example.neomusicplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class playlistSongsAdapter extends RecyclerView.Adapter<playlistSongsAdapter.playlistSongsViewHolder> {
    Context context;

    public playlistSongsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public playlistSongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == R.layout.songs_view) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_view, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_button, parent, false);
        }

        return new playlistSongsViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull playlistSongsViewHolder holder, final int position) {
        if (position == DataBaseManager.playlistSongs.size()) {
        } else {
            Log.d("checktwo", "onBindViewHolder: " + DataBaseManager.playlistSongs.get(position));
            holder.songsView.setText(MusicManager.songsDataName.get(DataBaseManager.playlistSongs.get(position)));
            holder.artistView.setText(MusicManager.songsNameArtist.get(MusicManager.songsDataName.get(DataBaseManager.playlistSongs.get(position))));
            holder.imageView.setImageBitmap(MusicManager.songNameImage.get(MusicManager.songsDataName.get(DataBaseManager.playlistSongs.get(position))));
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MusicManager.queueSongsListName = "songsByUser";
                    MusicManager.queueSongs.clear();
                    int x=0;
                    while (DataBaseManager.playlistSongs.size()>x){
                        MusicManager.queueSongs.add(MusicManager.songsDataName.get(DataBaseManager.playlistSongs.get(x)));
                   x++;
                    }
                    String data = DataBaseManager.playlistSongs.get(position);
                    Uri uri = Uri.parse(data);
                    MusicManager.getInstance().initMusicplayer(context, uri);
                    MusicManager.getInstance().startMusicPlayer();
                    Intent intent = new Intent(context, player.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return DataBaseManager.playlistSongs.size() + 1;
    }

    public int getItemViewType(int position) {
        return (position == DataBaseManager.playlistSongs.size()) ? R.layout.add_button : R.layout.songs_view;
    }

    public class playlistSongsViewHolder extends RecyclerView.ViewHolder {
        TextView songsView, artistView;
        LinearLayout linearLayout;
        ImageView imageView;
        Context context;

        public playlistSongsViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);
            this.context = context;
            linearLayout = itemView.findViewById(R.id.songs_View_Layout);
            artistView = itemView.findViewById(R.id.artist_view);
            songsView = itemView.findViewById(R.id.songs_view);
            imageView = itemView.findViewById(R.id.songsImageView);

        }
    }
}
