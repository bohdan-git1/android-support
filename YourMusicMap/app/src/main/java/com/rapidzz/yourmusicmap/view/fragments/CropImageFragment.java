package com.rapidzz.yourmusicmap.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rapidzz.yourmusicmap.databinding.FragmentCropPhotoBinding;

import static android.app.Activity.RESULT_OK;

public class CropImageFragment extends Fragment {
    public static final String TAG = CropImageFragment.class.getSimpleName();
    private Context context;
    FragmentCropPhotoBinding binding;

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

//        ((MainActivity)context).bindingng.contentMain.rlToolbarMain.setVisibility(View.VISIBLE);
//        ((MainActivity)context).binding.contentMain.etSearchSong.setVisibility(View.GONE);
//        ((MainActivity)context).binding.contentMain.ivMenu.setVisibility(View.GONE);
//        ((MainActivity)context).binding.contentMain.tvToolbarTitle.setVisibility(View.VISIBLE);
//        ((MainActivity)context).binding.contentMain.tvToolbarTitle.setText("Crop Photo");

//        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setActivityTitle("My Crop")
//                        .setCropShape(CropImageView.CropShape.RECTANGLE)
//                        .setCropMenuCropButtonTitle("Done")
//                        .start(getActivity());
//            }
//        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//
//                Log.e("Uri",resultUri.toString());
//                Glide.with(context)
//                        .load(resultUri)
//                        .apply(RequestOptions.circleCropTransform())
//                        .into(binding.ivProfile);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//                Log.e("error",error.toString());
//            }
//        }
//    }
}
