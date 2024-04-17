package com.example.neomusicplayer;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class songsAdapter extends RecyclerView.Adapter<songsAdapter.songsViewHolder>{

    songName songname;
    Context context;
    //String songPath;
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    public songsAdapter(Context context){
        this.context=context;

    }
    @Override
    public songsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_view,parent,false);
        return new songsViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull final songsViewHolder holder, int position) {

        final int finalPosition=position+1;
        holder.songsView.setText(MusicManager.songsDataName.get(MusicManager.songsPositionData.get(""+finalPosition+"")));
        holder.artistView.setText(MusicManager.songsNameArtist.get(MusicManager.songsDataName.get(MusicManager.songsPositionData.get(""+finalPosition+""))));
        try {
            if(MusicManager.songNameImage.get(MusicManager.songsDataName.get(MusicManager.songsPositionData.get(""+finalPosition+"")))!=null){
            holder.imageView.setImageBitmap(MusicManager.songNameImage.get(MusicManager.songsDataName.get(MusicManager.songsPositionData.get(""+finalPosition+""))));}
            else{}
        }catch (Exception e){}
        if (MainActivity.isSelectAll.equals(true)){
            MusicManager.selectedSongs.clear();
            int x=0;
            while(MusicManager.songsPositionData.size()>x){
            MusicManager.selectedSongs.add(MusicManager.songsPositionData.get(""+(x+1)+""));
            x++;}
            holder.songsCompleteView.setBackgroundColor(Color.GREEN);
            MainActivity.incrementSelectedSongs();
        }
        if (MainActivity.isSelected.equals(false)){
            holder.songsCompleteView.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.songsViewlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MusicManager.selectedSongs.clear();
                MainActivity.isSelected=true;
                MainActivity.addSelectView();
                return false;
            }
        });
        holder.songsViewlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isSelectAll=false;
                if (MainActivity.isSelected){
                    if (MusicManager.selectedSongs.contains(MusicManager.songsPositionData.get(""+finalPosition+""))){
                        MusicManager.selectedSongs.remove(MusicManager.songsPositionData.get(""+finalPosition+""));
                        holder.songsCompleteView.setBackgroundColor(Color.TRANSPARENT);
                        if (MusicManager.selectedSongs.isEmpty()){
                            MainActivity.isSelected=false;
                            MainActivity.removeSelectView();
                        }
                    }
                    else{
                        MusicManager.selectedSongs.add(MusicManager.songsPositionData.get(""+finalPosition+""));
                        holder.songsCompleteView.setBackgroundColor(Color.GREEN);
                    }
                    MainActivity.incrementSelectedSongs();
                    Log.d("meRock   ", "onClick: "+MusicManager.selectedSongs);
                }
                else {
                    MusicManager.queueSongsListName="allMusic";
                    songname= (songName) context;//send songName to MainActivity
                    songname.sendName(MusicManager.songsDataName.get(MusicManager.songsPositionData.get(""+finalPosition+"")));
                }
            }
        });

        if (MusicManager.selectedSongs!=null){
            if (MusicManager.selectedSongs.contains(MusicManager.songsPositionData.get(""+finalPosition+""))){
                holder.songsCompleteView.setBackgroundColor(Color.GREEN);
            }
        }

    }

    @Override
    public int getItemCount() {
        try {
            return MusicManager.songNameImage.size()+1;
        }catch (Exception e){
            return 0;
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class songsViewHolder extends RecyclerView.ViewHolder {

        TextView songsView,artistView;
        LinearLayout songsViewlayout,songsCompleteView;
        ImageView imageView;
        Context context;
        public songsViewHolder(@NonNull View itemView , Context context) {
            super(itemView);
            this.context=context;
            songsViewlayout =itemView.findViewById(R.id.songs_View_Layout);
            songsCompleteView=itemView.findViewById(R.id.songsCompleteView);
            artistView=itemView.findViewById(R.id.artist_view);
            songsView = itemView.findViewById(R.id.songs_view);
            imageView = itemView.findViewById(R.id.songsImageView);
        }
    }
}
