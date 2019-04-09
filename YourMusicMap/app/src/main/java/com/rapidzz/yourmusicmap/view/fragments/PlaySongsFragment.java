package com.rapidzz.yourmusicmap.view.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.rapidzz.songs.Activities.MainActivity;
//import com.rapidzz.songs.databinding.FragmentSongsPlayBinding;
import com.rapidzz.mymusicmap.datamodel.model.fan.Song;
import com.rapidzz.yourmusicmap.databinding.FragmentSongsPlayBinding;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class PlaySongsFragment extends Fragment {
    public static final String TAG = PlaySongsFragment.class.getSimpleName();

    private Context context;
    FragmentSongsPlayBinding binding;
    private Song mSong;


    MediaPlayer mediaPlayer = new MediaPlayer();
    int duration;
    boolean wasPlaying = false;
    int amoungToupdate,index = 0;
    long starttime = 0;
    Thread thread;
    Handler h2 = new Handler();
    Timer timer = new Timer();
    private Handler mHandler = new Handler();
    boolean isPlay = true;


    ArrayList<Song> songsList = new ArrayList<>();

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

        if (getArguments() !=null) {
            mSong = (Song) getArguments().getSerializable("Songs");
            index = getArguments().getInt("index");
            songsList = (ArrayList<Song>) getArguments().getSerializable("SongsList");
        }



        Log.e("path",mSong.getPath());




        binding.ivNextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for (int i=0;i<songsList.size();i++){
                if (index < songsList.size()){
                index++;
                binding.seekBar.setProgress(0);

                    String name = songsList.get(index).getTitle();
                    Log.e("title",name);
                    binding.tvSongTitle.setText(songsList.get(index).getTitle());
                    binding.tvSong.setText(songsList.get(index).getPath());
                    binding.tvSong.setSelected(true);
                    playSong(songsList.get(index).getPath());
                    isPlay = false;
                }
            }
        });

        if (isPlay) {
            binding.tvSongTitle.setText(songsList.get(index).getTitle());
            binding.tvSong.setText(songsList.get(index).getPath());
            binding.tvSong.setSelected(true);

            playSong(songsList.get(index).getPath());
        }


        binding.seekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                int x = (int) Math.ceil(progress / 1000f);

//                if (x < 10)
//                   // seekBarHint.setText("0:0" + x);
//                else
//                    seekBarHint.setText("0:" + x);

                double percent = progress / (double) binding.seekBar.getMax();
//                int offset = binding.seekBar.getThumbOffset();
//                int seekWidth = seekBar.getWidth();
//                int val = (int) Math.round(percent * (seekWidth - 2 * offset));
//                int labelWidth = seekBarHint.getWidth();
//                seekBarHint.setX(offset + seekBar.getX() + val
//                        - Math.round(percent * offset)
//                        - Math.round(percent * labelWidth / 2));

                if (progress > 0 && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    clearMediaPlayer();
//                    fab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, android.R.drawable.ic_media_play));
                    binding.seekBar.setProgress(0);
                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo((int) seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
    }

    private void playSong(String path) {

        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                clearMediaPlayer();
                binding.seekBar.setProgress(0);
                wasPlaying = true;
            }


            if (!wasPlaying) {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }


                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                binding.seekBar.setMax(mediaPlayer.getDuration());
                timer = new Timer();
                timer.schedule(new firstTask(), 0, 500);
                h2.postDelayed(run, 0);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Log.e("Play", "ok");
                        binding.seekBar.setProgress(0);
                        timer.cancel();
                        timer.purge();
                        h2.removeCallbacks(run);
                        //stopPlaying();
                        wasPlaying = !wasPlaying;
                    }
                });
//                new Thread(this).start();
                mediaPlayer.start();
            }
            wasPlaying = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            h2.postDelayed(this, 500);
        }
    };

    class firstTask extends TimerTask {
        @Override
        public void run() {
            h.sendEmptyMessage(0);
        }
    }

    final Handler h = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (!wasPlaying) {
                binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
            clearMediaPlayer();
            timer.cancel();
            timer.purge();
            h2.removeCallbacks(run);
            return false;
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
        timer.cancel();
        timer.purge();
        h2.removeCallbacks(run);
    }
}
