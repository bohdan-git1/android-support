package com.rapidzz.yourmusicmap.view.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rapidzz.mymusicmap.datamodel.model.fan.Song;
import com.rapidzz.mymusicmap.other.extensions.OneShotEvent;
import com.rapidzz.mymusicmap.other.util.SessionManager;
import com.rapidzz.yourmusicmap.databinding.FragmentSongsListBinding;
import com.rapidzz.yourmusicmap.other.util.ReplaceFragmentManger;
import com.rapidzz.yourmusicmap.view.Adapters.SongsListAdapter;
import com.rapidzz.yourmusicmap.view.activities.MainActivity;
import com.rapidzz.yourmusicmap.viewmodel.SongViewModel;

import java.util.ArrayList;

public class SongsFragment extends BaseFragment {
    public static final String TAG = SongsFragment.class.getSimpleName();

    private SongsListAdapter adapter;
    private ArrayList<Song> songs;

    SongViewModel viewModel;
    FragmentSongsListBinding binding;

    private String songIdToPlay = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSongsListBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)context).binding.appbar.tvEdit.setVisibility(View.GONE);

        setInfo();
        getInfo();
    }

    private void getInfo() {
        String id = new SessionManager(context).getUserId();
        viewModel.getSongs(id, true);
    }

    private void setInfo() {
        songs = new ArrayList<>();
        viewModel = ViewModelProviders.of(this).get(SongViewModel.class);
        viewModelCallbacks(viewModel);

        adapter = new SongsListAdapter(context, songs);
        binding.rvSongs.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSongs.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void moveToPlaySong() {

        Song song = new Song();

        int pos = -1;

        for (int i = 0; i < songs.size(); i++) {
            if (songIdToPlay.equalsIgnoreCase(songs.get(i).getId())) {
                pos = i;
                song = songs.get(i);
                break;
            }
        }

        // resetting to null
        songIdToPlay = null;

        ReplaceFragmentManger replaceFragment = new ReplaceFragmentManger();

        Bundle bundle = new Bundle();
        bundle.putSerializable("Songs", song);
        bundle.putSerializable("SongsList", songs);
        bundle.putInt("index", pos);
        replaceFragment.replaceFragment(new PlaySongsFragment(), PlaySongsFragment.TAG, bundle, context);

    }

    public void viewModelCallbacks(SongViewModel viewModel) {
        viewModel.getSnackbarMessage().observe(getViewLifecycleOwner(), new Observer<OneShotEvent<String>>() {
            @Override
            public void onChanged(@io.reactivex.annotations.Nullable OneShotEvent<String> stringOneShotEvent) {
                String msg = stringOneShotEvent.getContentIfNotHandled();
                if (msg != null)
                showAlertDialog(msg);
            }
        });

        viewModel.getProgressBar().observe(getViewLifecycleOwner(), new Observer<OneShotEvent<Boolean>>() {
            @Override
            public void onChanged(@io.reactivex.annotations.Nullable OneShotEvent<Boolean> booleanOneShotEvent) {
                showProgressDialog(false);
            }
        });

        viewModel.mSongListingResponseMutableLiveData.observe(getViewLifecycleOwner(), songArrayList -> {

            songs.clear();
            songs.addAll(songArrayList);

            if(songs.size() > 0){
                if (songIdToPlay != null && !songIdToPlay.isEmpty())
                    moveToPlaySong();
            }

            if (adapter != null)
                adapter.notifyDataSetChanged();
            //getFragmentManager().popBackStack();
            //Log.e("Response",""+user.getFirstName());
            //getFragmentManager().popBackStack();
        });
    }

    public static SongsFragment newInstance(String songIdToPlay) {
        SongsFragment fragment = new SongsFragment();
        fragment.songIdToPlay = songIdToPlay;
        return fragment;
    }
}
