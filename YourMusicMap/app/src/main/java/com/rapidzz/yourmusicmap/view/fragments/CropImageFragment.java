package com.rapidzz.yourmusicmap.view.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rapidzz.mymusicmap.other.extensions.OneShotEvent;
import com.rapidzz.mymusicmap.other.factory.ViewModelFactory;
import com.rapidzz.mymusicmap.other.util.SessionManager;
import com.rapidzz.yourmusicmap.databinding.FragmentCropPhotoBinding;
import com.rapidzz.yourmusicmap.view.activities.MainActivity;
import com.rapidzz.yourmusicmap.viewmodel.LoginViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class CropImageFragment extends BaseFragment {
    public static final String TAG = CropImageFragment.class.getSimpleName();
    private Context context;
    FragmentCropPhotoBinding binding;
    public static Uri resultUri;
    LoginViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCropPhotoBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)context).binding.appbar.tvEdit.setVisibility(View.GONE);
        ViewModelFactory factory =
                ViewModelFactory.Companion.getInstance(getActivity().getApplication());

        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);
        viewModelCallbacks(viewModel);
        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("My Crop")
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setCropMenuCropButtonTitle("Done")
                        .start(getActivity());
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = new SessionManager(getActivity()).getUserId();
                viewModel.uploadMedia(userId,resultUri.getPath());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                Log.e("Uri",resultUri.toString());
                Glide.with(context)
                        .load(resultUri)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.ivProfile);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("error",error.toString());
            }
        }
    }

    public void viewModelCallbacks(LoginViewModel viewModel) {
        viewModel.getSnackbarMessage().observe(getViewLifecycleOwner(), new Observer<OneShotEvent<String>>() {
            @Override
            public void onChanged(@io.reactivex.annotations.Nullable OneShotEvent<String> stringOneShotEvent) {
                String msg = stringOneShotEvent.getContentIfNotHandled();
                showAlertDialog(msg);

            }
        });

        viewModel.getProgressBar().observe(getViewLifecycleOwner(), new Observer<OneShotEvent<Boolean>>() {
            @Override
            public void onChanged(@io.reactivex.annotations.Nullable OneShotEvent<Boolean> booleanOneShotEvent) {
                showProgressDialog(booleanOneShotEvent.getContentIfNotHandled());
            }
        });

        viewModel.getUser().observe(getViewLifecycleOwner(), songResponse -> {

            getFragmentManager().popBackStack();
//            ((AppCompatActivity)context).getSupportFragmentManager().popBackStack();
            //Log.e("Response",""+user.getFirstName());

        });
    }
}
