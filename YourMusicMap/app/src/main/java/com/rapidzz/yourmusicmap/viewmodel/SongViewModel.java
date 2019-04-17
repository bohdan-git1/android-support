package com.rapidzz.yourmusicmap.viewmodel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import android.util.Log;
import android.widget.Toast;

import com.rapidzz.mymusicmap.datamodel.model.fan.Song;
import com.rapidzz.mymusicmap.datamodel.model.responses.ApiErrorResponse;
import com.rapidzz.mymusicmap.datamodel.model.responses.IsTrackAvailableResponse;
import com.rapidzz.mymusicmap.datamodel.model.responses.SongListingResponse;
import com.rapidzz.mymusicmap.datamodel.model.responses.SongResponse;
import com.rapidzz.mymusicmap.datamodel.model.responses.UserResponse;
import com.rapidzz.mymusicmap.datamodel.source.UserDataSource;
import com.rapidzz.mymusicmap.datamodel.source.UserRepository;
import com.rapidzz.mymusicmap.datamodel.source.remote.RetrofitClientInstance;
import com.rapidzz.yourmusicmap.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SongViewModel extends BaseAndroidViewModel {


    private UserRepository mUserRepository;
    public MutableLiveData<SongResponse> mSongMutableLiveData = new MutableLiveData();
    public MutableLiveData<ArrayList<Song>> mSongListingResponseMutableLiveData = new MutableLiveData();

    public SongViewModel(@NonNull Application application) {
        super(application);
        mUserRepository = new UserRepository(application);
    }

    public void doSaveSong(String title, String path, String id, String lat, String lng) {

        if (title.trim().isEmpty() || path.isEmpty() || lat.isEmpty() || lng.isEmpty())
            showSnackbarMessage(getString(R.string.error_message_enter_missing_detail));
        else {
            showProgressBar(true);
            mUserRepository.saveSong(title, path, Integer.parseInt(id),
                    Double.parseDouble(lat),
                    Double.parseDouble(lng), new UserDataSource.SaveSongCallback() {
                        @Override
                        public void onSaveSong(@NotNull SongResponse response) {
                            Log.e("Success", response.toString());
                            showProgressBar(false);
                            showSnackbarMessage(response.getMessage());
                            mSongMutableLiveData.setValue(response);
                        }

                        @Override
                        public void onPayloadError(@NotNull ApiErrorResponse error) {
                            Log.e("Success", error.toString());
                            showProgressBar(false);
                            showSnackbarMessage(error.getMessage());
                        }
                    });
        }
    }

    public void getSongs(String userId, Boolean doShow) {

        //if (doShow )
        showProgressBar(doShow);
        mUserRepository.getSongListing(userId, new UserDataSource.GetSongCallback() {
            @Override
            public void onSongListing(@NotNull SongListingResponse response) {
                Log.e("Success", response.toString());
                showProgressBar(false);
                mSongListingResponseMutableLiveData.setValue(response.getSongs());
            }

            @Override
            public void onPayloadError(@NotNull ApiErrorResponse error) {
                Log.e("Success", error.toString());
                showProgressBar(false);
                showSnackbarMessage(error.getMessage());
            }
        });
    }

    public void isTrackAvailable(String latitude, String longitude,
                                 UserDataSource.IsTrackAvailableCallback callback) {
        mUserRepository.isTrackAvailable(latitude, longitude,
                new UserDataSource.IsTrackAvailableCallback() {
                    @Override
                    public void onError(@NotNull String error) {
                        callback.onError(error);
                    }

                    @Override
                    public void onResponse(boolean trackAvailable, @NotNull String message) {
                        callback.onResponse(trackAvailable, message);
                    }
                });
    }

}
