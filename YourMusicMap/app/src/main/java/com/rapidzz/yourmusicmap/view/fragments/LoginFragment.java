package com.rapidzz.yourmusicmap.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.databinding.FragmentLoginBinding;
import com.rapidzz.yourmusicmap.other.util.ReplaceFragmentManger;

public class LoginFragment extends BaseFragment implements View.OnClickListener{
    public static final String TAG = LoginFragment.class.getSimpleName();
    private Context context;
    FragmentLoginBinding binding;
    ReplaceFragmentManger replaceFragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //replaceFragment = new ReplaceFragmentManger();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      //  binding = FragmentLoginBinding.inflate(inflater);
        return container;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvCreateAccount.setOnClickListener(this);
        binding.btLogin.setOnClickListener(this);

        //((MainActivity)context).binding.rlToolbarMain.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvCreateAccount:
                replaceFragment.replaceFragment(new SignupFragment(),SignupFragment.TAG,null,context);
                break;
            case R.id.btLogin:
                replaceFragment.replaceFragment(new MapFragment(),MapFragment.TAG,null,context);
                break;
        }
    }
}
