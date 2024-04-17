package com.example.neomusicplayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class artistAdapter extends RecyclerView.Adapter<artistAdapter.artistViewHolder> {

    String[] Artist=new String[MusicManager.songsNameArtist.size()];
    String[] Artist1=new String[MusicManager.songsNameArtist.size()];
    int x=0;
    Context context;

    public artistAdapter(Context context){
        this.context=context;
    }

    @Override
    public artistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_view,parent,false);
        return new artistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull artistAdapter.artistViewHolder holder, int position) {
       holder.textView.setText(MusicManager.artists.get(position));
       try {

           holder.artistImageView.setImageBitmap(MusicManager.songNameImage.get(MusicManager.songsArtistName.get(MusicManager.artists.get(position))));
       }catch (Exception e){}

    }
    @Override
    public int getItemCount() {
       return MusicManager.artists.size();
    }

    public class artistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;
        ImageView artistImageView;
        int position;
        public artistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistImageView=itemView.findViewById(R.id.artist_Image);
            textView=itemView.findViewById(R.id.artist_view);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            position=getAdapterPosition();
            Intent intent=new Intent(context,songsByArtists.class);
            intent.putExtra("position",position);
            context.startActivity(intent);
        }
    }
}
