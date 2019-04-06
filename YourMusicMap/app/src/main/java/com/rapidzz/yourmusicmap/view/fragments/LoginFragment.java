package com.rapidzz.yourmusicmap.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rapidzz.mymusicmap.other.extensions.OneShotEvent;
import com.rapidzz.mymusicmap.other.factory.ViewModelFactory;
import com.rapidzz.mymusicmap.view.activities.GlobalNavigationActivity;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.databinding.FragmentLoginBinding;
import com.rapidzz.yourmusicmap.other.util.ReplaceFragmentManger;
import com.rapidzz.yourmusicmap.view.activities.MainActivity;
import com.rapidzz.yourmusicmap.viewmodel.LoginViewModel;
import com.rapidzz.yourmusicmap.viewmodel.SignupViewModel;

public class LoginFragment extends BaseFragment implements View.OnClickListener{
    public static final String TAG = LoginFragment.class.getSimpleName();
    private Context context;
    FragmentLoginBinding binding;
    ReplaceFragmentManger replaceFragment;
    LoginViewModel loginViewModel;

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
        binding = FragmentLoginBinding.inflate(inflater);
        init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.llCreateAccount.setOnClickListener(this);
        binding.btLogin.setOnClickListener(this);

        //((MainActivity)context).binding.rlToolbarMain.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.llCreateAccount:
                replaceFragment.replaceFragment(new SignupFragment(),SignupFragment.TAG,null,context);
                break;
            case R.id.btLogin:
                loginViewModel.login(binding.etEmail.getText().toString()
                        ,binding.etPassword.getText().toString());
                //replaceFragment.replaceFragment(new MapFragment(),MapFragment.TAG,null,context);
                break;
        }
    }

    public void init(){
        ViewModelFactory factory =
                ViewModelFactory.Companion.getInstance(getActivity().getApplication());

        loginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);
        viewModelCallbacks(loginViewModel);
    }

    public void viewModelCallbacks(LoginViewModel viewModel){
        viewModel.getSnackbarMessage().observe(this, new Observer<OneShotEvent<String>>() {
            @Override
            public void onChanged(@Nullable OneShotEvent<String> stringOneShotEvent) {
                String msg = stringOneShotEvent.getContentIfNotHandled();
                showAlertDialog(msg);

            }
        });

        viewModel.getProgressBar().observe(this, new Observer<OneShotEvent<Boolean>>() {
            @Override
            public void onChanged(@Nullable OneShotEvent<Boolean> booleanOneShotEvent) {
                showProgressDialog(booleanOneShotEvent.getContentIfNotHandled());
            }
        });

        viewModel.getUser().observe(this, user -> {

            Log.e("Response",""+user.getFirstName());
            startActivity(new Intent(getActivity(), MainActivity.class));
            //replaceFragment.replaceFragment(new MapFragment(),MapFragment.TAG,null,context);

            //getFragmentManager().popBackStack();
        });
    }
}
