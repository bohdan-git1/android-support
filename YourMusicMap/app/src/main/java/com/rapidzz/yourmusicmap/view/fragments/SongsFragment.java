package com.rapidzz.yourmusicmap.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rapidzz.mymusicmap.datamodel.model.fan.Song;
import com.rapidzz.mymusicmap.other.extensions.OneShotEvent;
import com.rapidzz.mymusicmap.other.util.SessionManager;
import com.rapidzz.yourmusicmap.databinding.FragmentSongsListBinding;
import com.rapidzz.yourmusicmap.view.Adapters.SongsListAdapter;
import com.rapidzz.yourmusicmap.viewmodel.SongViewModel;

import java.io.File;
import java.util.ArrayList;

public class SongsFragment extends BaseFragment{
    public static final String TAG = SongsFragment.class.getSimpleName();
    private Context context;
    FragmentSongsListBinding binding;
    private SongsListAdapter adapter;
    private ArrayList<Song> songs;
    SongViewModel viewModel;

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


        setInfo();
        getInfo();
    }

    private void getInfo() {
        String id =  new SessionManager(context).getUserId();
        viewModel.getSongs(id,true);
    }

    private void setInfo() {
        songs = new ArrayList<Song>();
        viewModel = ViewModelProviders.of(this).get(SongViewModel.class);
        viewModelCallbacks(viewModel);

        adapter = new SongsListAdapter(context,songs);
        binding.rvSongs.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSongs.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void viewModelCallbacks(SongViewModel viewModel) {
        viewModel.getSnackbarMessage().observe(this, new Observer<OneShotEvent<String>>() {
            @Override
            public void onChanged(@io.reactivex.annotations.Nullable OneShotEvent<String> stringOneShotEvent) {
                String msg = stringOneShotEvent.getContentIfNotHandled();
                showAlertDialog(msg);
            }
        });

        viewModel.getProgressBar().observe(this, new Observer<OneShotEvent<Boolean>>() {
            @Override
            public void onChanged(@io.reactivex.annotations.Nullable OneShotEvent<Boolean> booleanOneShotEvent) {
                showProgressDialog(false);
            }
        });

        viewModel.mSongListingResponseMutableLiveData.observe(this, songArrayList -> {

            songs.clear();
            songs.addAll(songArrayList);
            if(adapter != null)
                adapter.notifyDataSetChanged();
            //getFragmentManager().popBackStack();
            //Log.e("Response",""+user.getFirstName());
            //getFragmentManager().popBackStack();
        });
    }
}
