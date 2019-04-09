package com.rapidzz.yourmusicmap.view.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.rapidzz.mymusicmap.datamodel.model.fan.Song;
import com.rapidzz.yourmusicmap.databinding.RvSongsBinding;
import com.rapidzz.yourmusicmap.other.util.ReplaceFragmentManger;
import com.rapidzz.yourmusicmap.view.fragments.PlaySongsFragment;

import java.util.ArrayList;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Song> songs;

    public SongsListAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public SongsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SongsListAdapter.ViewHolder(RvSongsBinding.inflate(LayoutInflater.from(context)));
    }

    @Override
    public void onBindViewHolder(@NonNull SongsListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RvSongsBinding binding;

        public ViewHolder(@NonNull RvSongsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int pos) {
            binding.tvSongTitle.setText(songs.get(pos).getTitle());

            binding.llMusicList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ReplaceFragmentManger replaceFragment = new ReplaceFragmentManger();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Songs",songs.get(pos));
                    bundle.putSerializable("SongsList",songs);
                    bundle.putInt("index",pos);
                    replaceFragment.replaceFragment(new PlaySongsFragment(),PlaySongsFragment.TAG,bundle,context);
                }
            });
        }

    }
}
