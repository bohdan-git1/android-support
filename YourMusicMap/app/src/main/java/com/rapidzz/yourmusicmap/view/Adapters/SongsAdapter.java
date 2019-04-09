package com.rapidzz.yourmusicmap.view.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rapidzz.mymusicmap.datamodel.model.fan.Song;
import com.rapidzz.yourmusicmap.R;

import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    private AppCompatActivity activity;
    private ArrayList<Song> mSongArrayList;

    public SongsAdapter(AppCompatActivity activity, ArrayList<Song> songs) {
        this.activity = activity;
        this.mSongArrayList = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_item_song, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int pos) {
        final Song offer = mSongArrayList.get(pos);

        holder.tvTitle.setText(offer.getTitle());

        holder.lnSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CLICK",mSongArrayList.get(pos).getTitle());

            }
        });


    }

    @Override
    public int getItemCount() {
        return mSongArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private LinearLayout lnSong;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title);
            lnSong = itemView.findViewById(R.id.lnSong);

        }
    }
}
