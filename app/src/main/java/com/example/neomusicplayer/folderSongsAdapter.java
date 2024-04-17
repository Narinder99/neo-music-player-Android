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

public class folderSongsAdapter extends RecyclerView.Adapter<folderSongsAdapter.folderSongsViewHolder> {
    Context context;
    public folderSongsAdapter(Context context){
        this.context=context;
    }
    @NonNull
    @Override
    public folderSongsAdapter.folderSongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_view, parent, false);
        return new folderSongsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull folderSongsAdapter.folderSongsViewHolder holder, int position) {
        holder.songsView.setText(MusicManager.songsByFolderName.get(position));
        holder.artistView.setText(MusicManager.songsNameArtist.get(MusicManager.songsByFolderName.get(position)));
        try {
            holder.imageView.setImageBitmap(MusicManager.songNameImage.get(MusicManager.songsByFolderName.get(position)));
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return MusicManager.songsByFolderName.size();
    }

    public class folderSongsViewHolder extends RecyclerView.ViewHolder {
        TextView songsView, artistView;
        LinearLayout linearLayout;
        ImageView imageView;

        public folderSongsViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.songs_View_Layout);
            artistView = itemView.findViewById(R.id.artist_view);
            songsView = itemView.findViewById(R.id.songs_view);
            imageView = itemView.findViewById(R.id.songsImageView);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MusicManager.queueSongs.clear();
                    MusicManager.queueSongsListName="songsByUser";
                    MusicManager.queueSongs=MusicManager.songsByFolderName;
                    String name=MusicManager.songsByFolderName.get(getAdapterPosition());
                    Uri uri=Uri.parse(MusicManager.songsNameData.get(name));
                    MusicManager.getInstance().initMusicplayer(context,uri);
                    MusicManager.getInstance().startMusicPlayer();
                    Intent intent = new Intent(context,player.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
