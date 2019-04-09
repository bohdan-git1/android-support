package com.rapidzz.yourmusicmap.view.fragments;

import android.content.Context;
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
import com.rapidzz.mymusicmap.other.util.SessionManager;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.databinding.FragmentProfileBinding;
import com.rapidzz.yourmusicmap.other.util.ReplaceFragmentManger;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = ProfileFragment.class.getSimpleName();
    private Context context;
    FragmentProfileBinding binding;
    ReplaceFragmentManger replaceFragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceFragment = new ReplaceFragmentManger();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ((MainActivity)context).binding.contentMain.rlToolbarMain.setVisibility(View.GONE);

        ///////////////////////click events////////////////////////////
        binding.ivProfile.setOnClickListener(this);

        String firstName =  new SessionManager(context).getFirstName();
        String lastName =  new SessionManager(context).getLastName();


        Log.e("Image",String.valueOf(CropImageFragment.resultUri));

        binding.etName.setText(firstName + " " + lastName);
        binding.etEmail.setText(new SessionManager(context).getEmail());
        binding.etMobileNo.setText(new SessionManager(context).getPhone());



        Glide.with(context).
                load(CropImageFragment.resultUri).
                apply(RequestOptions.circleCropTransform().
                        placeholder(R.drawable.dp_placeholder)).
                into(binding.ivProfile);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivProfile:
                replaceFragment.replaceFragment(new CropImageFragment(),CropImageFragment.TAG,null,context);
                break;

            default:
                break;
        }
    }
}
