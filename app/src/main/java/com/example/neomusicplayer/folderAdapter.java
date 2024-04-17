package com.example.neomusicplayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import java.util.zip.Inflater;

public class folderAdapter extends RecyclerView.Adapter<folderAdapter.folderViewHolder> {
    Context context;

    public folderAdapter(Context context){
        this.context=context;
    }
    @NonNull
    @Override
    public folderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_view, parent, false);
        return new folderViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull folderViewHolder holder, int position) {
        holder.folderName.setText(MusicManager.folderNames.get(position));
    }

    @Override
    public int getItemCount() {
        return MusicManager.folderNames.size();
    }

    public class folderViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;
Context context;
        public folderViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);
            this.context=context;
            final DatabaseClass databaseClass=new DatabaseClass(context);
            folderName = itemView.findViewById(R.id.folderName);
            folderName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,folderSongs.class);
                    intent.putExtra("folderName",MusicManager.folderNames.get(getAdapterPosition()));
                    databaseClass.getSongsNameByFolder(String.valueOf(MusicManager.folderNames.get(getAdapterPosition())));
                    context.startActivity(intent);
                }
            });
        }
    }
}
