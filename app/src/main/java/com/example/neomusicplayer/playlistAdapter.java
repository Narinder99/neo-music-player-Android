package com.example.neomusicplayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.percentlayout.widget.PercentRelativeLayout;


import java.util.zip.Inflater;

public class playlistAdapter extends RecyclerView.Adapter<playlistAdapter.playlistViewHolder> {

    Context context;
    public playlistAdapter(Context context){
        this.context=context;
    }
    @NonNull
    @Override
    public playlistAdapter.playlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_view,parent,false);
        return new playlistViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull playlistAdapter.playlistViewHolder holder, final int position) {

            try {holder.playlistName.setText(DataBaseManager.playlistNames.get(position));}
            catch (Exception e) {}

    }

    @Override
    public int getItemCount() {
        return DataBaseManager.playlistNames.size();
    }

    public class playlistViewHolder extends RecyclerView.ViewHolder {
        TextView playlistName;
        ImageView playlistOptionButton,playAllButton;
        PercentRelativeLayout layout;
        Context context;
        public playlistViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);
            this.context=context;
            playlistName=itemView.findViewById(R.id.playlistName);
            playAllButton=itemView.findViewById(R.id.playAllButton);
            playlistOptionButton=itemView.findViewById(R.id.playlistOptions);
            layout=itemView.findViewById(R.id.playListViewLayout);

            playlistOptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int x=getAdapterPosition();
                    DatabaseClass objDataBase = new DatabaseClass(context);
                    objDataBase.getPlaylistSongs(DataBaseManager.playlistNames.get(x));
                    Intent intent=new Intent(context,playlistSongs.class);
                    intent.putExtra("playlistName",DataBaseManager.playlistNames.get(x));
                    context.startActivity(intent);
                }
            });
        }
    }
}
