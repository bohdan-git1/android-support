package com.rapidzz.yourmusicmap.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rapidzz.yourmusicmap.databinding.FragmentSongsListBinding;
import com.rapidzz.yourmusicmap.view.Adapters.SongsListAdapter;
import com.rapidzz.yourmusicmap.viewmodel.Songs;

import java.util.ArrayList;

public class SongsFragment extends Fragment {
    public static final String TAG = SongsFragment.class.getSimpleName();
    private Context context;
    FragmentSongsListBinding binding;
    private SongsListAdapter adapter;
    private ArrayList<Songs> songs = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSongsListBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ((MainActivity) context).binding.contentMain.rlToolbarMain.setVisibility(View.VISIBLE);
//        ((MainActivity) context).binding.contentMain.etSearchSong.setVisibility(View.GONE);
//        ((MainActivity) context).binding.contentMain.ivMenu.setVisibility(View.VISIBLE);
//        ((MainActivity) context).binding.contentMain.tvToolbarTitle.setVisibility(View.VISIBLE);
//        ((MainActivity) context).binding.contentMain.tvToolbarTitle.setText("List Of Songs Created");

        getInfo();
        setInfo();
    }

    private void getInfo() {
        songs = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            songs.add(new Songs(1, "", "Pakistan Zindabad - 23 Mar 2019  Sahir Ali Bagga  Pakistan Day 2019"));
        }
    }

    private void setInfo() {
        adapter = new SongsListAdapter(context,songs);
        binding.rvSongs.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSongs.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
