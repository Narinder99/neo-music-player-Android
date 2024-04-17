package com.example.neomusicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class searchBarAdapter extends RecyclerView.Adapter<searchBarAdapter.searchBarViewHolder> {
    Context context;
    ArrayList<String> nameFilteredList;
    ArrayList<String> artistFilteredList ;
    int x=1;
    public searchBarAdapter(Context context,ArrayList<String> nameFilteredList,ArrayList<String> artistFilteredList){
        this.context=context;
        this.nameFilteredList=nameFilteredList;
        this.artistFilteredList=artistFilteredList;
        if (!nameFilteredList.isEmpty()&&!artistFilteredList.isEmpty()){
            x=2;
        }
        else if (nameFilteredList.isEmpty()&&artistFilteredList.isEmpty()){
            x=0;
        }
    }
    @NonNull
    @Override
    public searchBarAdapter.searchBarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_bar_view,parent,false);
        return new searchBarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull searchBarAdapter.searchBarViewHolder holder, int position) {
        switch (x){
            case 1:
                if (artistFilteredList.isEmpty()){
                    holder.searchedType.setText("Track");
                    holder.setRecyclerView(nameFilteredList,"songName");
                }
                else{
                    holder.searchedType.setText("Artist");
                    holder.setRecyclerView(artistFilteredList,"artistName");
                }
                break;
            case 2:
                if (position==0){
                    holder.searchedType.setText("Track");
                    holder.setRecyclerView(nameFilteredList,"songName");
                }
                else{
                    holder.searchedType.setText("ARTIST");
                    holder.setRecyclerView(artistFilteredList,"artistName");
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return x;
    }

    public class searchBarViewHolder extends RecyclerView.ViewHolder {
        TextView searchedType;
        RecyclerView recyclerView;
        public searchBarViewHolder(@NonNull View itemView) {
            super(itemView);
            searchedType=itemView.findViewById(R.id.searchedType);
            recyclerView=itemView.findViewById(R.id.searchBarAdapterRecyclerView);
        }
        public void setRecyclerView(ArrayList<String> FilteredList,String songNameOrArtist){
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new searchBarRecyclerViewAdapter(FilteredList,songNameOrArtist,context));
        }
    }
}
