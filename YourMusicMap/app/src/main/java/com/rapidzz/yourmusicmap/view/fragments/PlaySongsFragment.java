package com.rapidzz.yourmusicmap.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.rapidzz.songs.Activities.MainActivity;
//import com.rapidzz.songs.databinding.FragmentSongsPlayBinding;
import com.rapidzz.yourmusicmap.databinding.FragmentSongsPlayBinding;

public class PlaySongsFragment extends Fragment {
    public static final String TAG = PlaySongsFragment.class.getSimpleName();

    private Context context;
    FragmentSongsPlayBinding binding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSongsPlayBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ((MainActivity) context).binding.contentMain.rlToolbarMain.setVisibility(View.VISIBLE);
//        ((MainActivity) context).binding.contentMain.etSearchSong.setVisibility(View.GONE);
//        ((MainActivity) context).binding.contentMain.ivMenu.setVisibility(View.VISIBLE);
//        ((MainActivity) context).binding.contentMain.tvToolbarTitle.setVisibility(View.GONE);
    }
}
