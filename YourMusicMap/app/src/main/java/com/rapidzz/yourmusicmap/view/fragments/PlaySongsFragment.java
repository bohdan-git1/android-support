package com.rapidzz.yourmusicmap.view.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.rapidzz.songs.Activities.MainActivity;
//import com.rapidzz.songs.databinding.FragmentSongsPlayBinding;
import com.rapidzz.mymusicmap.datamodel.model.fan.Song;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.databinding.FragmentSongsPlayBinding;
import com.rapidzz.yourmusicmap.view.activities.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class PlaySongsFragment extends Fragment {
    public static final String TAG = PlaySongsFragment.class.getSimpleName();

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private Song mSong;

    MediaPlayer mediaPlayer = new MediaPlayer();
    int duration;
    boolean wasPlaying = false;
    int amoungToupdate, index = 0;
    long starttime = 0;
    Thread thread;
    Handler h2 = new Handler();
    Timer timer = new Timer();
    private Handler mHandler = new Handler();
    boolean isPlay = true;
    boolean isLoop = false;


    ArrayList<Song> songsList = new ArrayList<>();

    FragmentSongsPlayBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSongsPlayBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) context).binding.appbar.tvEdit.setVisibility(View.GONE);

        if (getArguments() != null) {
            mSong = (Song) getArguments().getSerializable("Songs");
            index = getArguments().getInt("index");
            songsList = (ArrayList<Song>) getArguments().getSerializable("SongsList");
        }


        binding.ivNextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index != songsList.size() - 1) {
                    index++;
                    binding.seekBar.setProgress(0);

                    timer.cancel();
                    timer.purge();
                    h2.removeCallbacks(run);
                    isPlay = false;
                    if (!wasPlaying) {
                        clearMediaPlayer();
                    }

                    binding.tvSongTitle.setText(songsList.get(index).getTitle());
                    binding.tvSong.setText(songsList.get(index).getPath());
                    binding.tvSong.setSelected(true);

                    mediaPlayer = new MediaPlayer();
                    playSong(songsList.get(index).getPath());
                }
            }
        });


        binding.ivPreviousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index != 0) {
                    index--;
                    binding.seekBar.setProgress(0);

                    timer.cancel();
                    timer.purge();
                    h2.removeCallbacks(run);
                    isPlay = false;

                    if (!wasPlaying) {
                        clearMediaPlayer();
                    }

                    binding.tvSongTitle.setText(songsList.get(index).getTitle());
                    binding.tvSong.setText(songsList.get(index).getPath());
                    binding.tvSong.setSelected(true);

                    mediaPlayer = new MediaPlayer();
                    playSong(songsList.get(index).getPath());

                }
            }
        });


        binding.ivRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                timer.cancel();
//                timer.purge();
//                h2.removeCallbacks(run);
//                isPlay = false;

                if (!wasPlaying) {
                    clearMediaPlayer();
                }

                binding.tvSongTitle.setText(songsList.get(index).getTitle());
                binding.tvSong.setText(songsList.get(index).getPath());
                binding.tvSong.setSelected(true);

                mediaPlayer = new MediaPlayer();

                if (!isLoop){
                    binding.ivRepeat.setImageResource(R.drawable.ic_repeat_black_24dp);
                    mediaPlayer.setLooping(true);
                    isLoop = true;
                }else{
                    binding.ivRepeat.setImageResource(R.drawable.ic_repeat_one_black_24dp);
                    mediaPlayer.setLooping(false);
                    isLoop = false;
                }

                playSong(songsList.get(index).getPath());
                isPlay = true;
            }
        });

//        binding.ivShuffle.setOnClickListener(v -> {
//            int i = 0;
//            Integer next = 0;
//            Random rng = new Random();
//
//            ArrayList<Integer> number = new ArrayList<Integer>();
//            for (i = 1; i <= songsList.size(); ++i) {
//                number.add(i);
//
//                while (true) {
//                    next = rng.nextInt(songsList.size() - 1) + 1;
//                    if (!number.contains(next)) {
//                        number.add(next);
//                        break;
//                    }
//                }
////                Collections.shuffle(number);
//
//                int randomInt = (new Random().nextInt(songsList.size() - 1));
////                    int sound = (int) songsList.get(randomInt);
////                    MediaPlayer mp = MediaPlayer.create(context, randomInt);
//
//
//                Log.e("Next", String.valueOf(randomInt));
//                if (!wasPlaying) {
//                    clearMediaPlayer();
//                }
//
//                binding.tvSongTitle.setText(songsList.get(randomInt).getTitle());
//                binding.tvSong.setText(songsList.get(randomInt).getPath());
//                binding.tvSong.setSelected(true);
//
//                mediaPlayer = new MediaPlayer();
//                mediaPlayer.setLooping(false);
//                playSong(songsList.get(randomInt).getPath());
//                isPlay = false;
//            }
//        });


        if (isPlay) {
            binding.tvSongTitle.setText(songsList.get(index).getTitle());
            binding.tvSong.setText(songsList.get(index).getPath());
            binding.tvSong.setSelected(true);
            playSong(songsList.get(index).getPath());
//            playSong("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3");
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

        Log.e("MediaPlayer", "ok");
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
//        mediaPlayer.release();
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
                if(mediaPlayer != null)
                binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }

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
