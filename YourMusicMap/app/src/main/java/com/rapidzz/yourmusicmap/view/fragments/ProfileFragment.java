package com.rapidzz.yourmusicmap.view.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.rapidzz.yourmusicmap.view.activities.MainActivity;

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

        binding.ivProfile.setEnabled(false);
        binding.etMobileNo.setEnabled(false);
        binding.etEmail.setEnabled(false);
        binding.etName.setEnabled(false);
        binding.btnUpdateProfile.setEnabled(false);

        ((MainActivity)context).binding.appbar.tvEdit.setVisibility(View.VISIBLE);

        ((MainActivity)context).binding.appbar.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).binding.appbar.tvEdit.setVisibility(View.GONE);
                binding.ivProfile.setEnabled(true);
                binding.etMobileNo.setEnabled(true);
                binding.etEmail.setEnabled(true);
                binding.etName.setEnabled(true);
                binding.btnUpdateProfile.setEnabled(true);
            }
        });


        ///////////////////////click events////////////////////////////
        binding.ivProfile.setOnClickListener(this);

        String firstName =  new SessionManager(context).getFirstName();
        String lastName =  new SessionManager(context).getLastName();
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
