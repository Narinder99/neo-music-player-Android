package com.example.neomusicplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class queueDialogAdapter extends RecyclerView.Adapter<queueDialogAdapter.queueViewHolder> {
    int x=0;
    Context context;
    Activity activity;
    queueToPlayer mqueueToPlayer;
    queueViewHolder holder;
    public  queueDialogAdapter(Context context,Activity activity){
        this.context=context;
        this.activity=activity;
    }
    @NonNull
    @Override
    public queueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=  LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_dialog_view,parent,false);
        return new queueViewHolder(view,context,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull final queueViewHolder holder, final int position) {
        final String Name;
        switch (MusicManager.queueSongsListName) {
            case "allMusic":
                Name = MusicManager.songsDataName.get(MusicManager.songsPositionData.get("" + (position + 1) + ""));
                holder.textView.setText(Name);
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int x=position;
                        String songData= MusicManager.songsPositionData.get(""+(x+1)+"");
                        Uri uri=Uri.parse(songData);
                        MusicManager.getInstance().initMusicplayer(context,uri);
                        MusicManager.getInstance().startMusicPlayer();
                        mqueueToPlayer=(queueToPlayer) context;
                        mqueueToPlayer.start(songData);
                    }
                });
                break;
            case "songsByUser":
                Name=MusicManager.queueSongs.get(position);
                holder.textView.setText(Name);
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int x=position;
                        String songData= MusicManager.songsNameData.get(Name);
                        Log.d("hello", "onClick: "+songData);
                        Uri uri=Uri.parse(songData);
                        MusicManager.getInstance().initMusicplayer(context,uri);
                        MusicManager.getInstance().startMusicPlayer();
                        mqueueToPlayer=(queueToPlayer) context;
                        mqueueToPlayer.start(songData);
                    }
                });
                break;
            case "singleTrack":
                Name=MusicManager.queueSongs.get(position);
                holder.textView.setText(Name);
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri=Uri.parse(MusicManager.songData);
                        MusicManager.getInstance().initMusicplayer(context,uri);
                        MusicManager.getInstance().startMusicPlayer();
                        mqueueToPlayer=(queueToPlayer) context;
                        mqueueToPlayer.start(MusicManager.songData);}
                });
                break;
            case "shuffleSongs":
                Name=MusicManager.songsDataName.get(MusicManager.songsPositionData.get(""+(MusicManager.shuffledArray.get(position)+1)+""));
                holder.textView.setText(Name);
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String songData=MusicManager.songsNameData.get(Name);
                        Uri uri=Uri.parse(songData);
                        MusicManager.getInstance().initMusicplayer(context,uri);
                        MusicManager.getInstance().startMusicPlayer();
                        mqueueToPlayer=(queueToPlayer) context;
                        mqueueToPlayer.start(MusicManager.songData);}
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (MusicManager.queueSongsListName){
            case "allMusic":
                return MusicManager.songsDataName.size();
            case "songsByUser":
                return MusicManager.queueSongs.size();
            case "singleTrack":
                return 1;
            case "shuffleSongs":
                return MusicManager.shuffledArray.size();
            default:
                return 0;
        }
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public class queueViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView,playPause;
        Context context;
        Activity activity;
        int x,y;
        public queueViewHolder(@NonNull View itemView, final Context context, final Activity activity) {
            super(itemView);
            this.activity=activity;
            this.context=context;
            playPause=itemView.findViewById(R.id.song_Status);
            textView=itemView.findViewById(R.id.queue_RecyclerView_text);
            imageView=itemView.findViewById(R.id.queue_RecyclerView_image);
             y=getAdapterPosition();

        }

    }
}
