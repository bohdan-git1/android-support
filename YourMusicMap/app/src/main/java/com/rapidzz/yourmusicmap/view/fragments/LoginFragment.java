package com.rapidzz.yourmusicmap.view.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.rapidzz.mymusicmap.other.extensions.OneShotEvent;
import com.rapidzz.mymusicmap.other.factory.ViewModelFactory;
import com.rapidzz.mymusicmap.other.util.SessionManager;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.databinding.FragmentLoginBinding;
import com.rapidzz.yourmusicmap.other.util.Permission;
import com.rapidzz.yourmusicmap.other.util.ReplaceFragmentManger;
import com.rapidzz.yourmusicmap.view.activities.MainActivity;
import com.rapidzz.yourmusicmap.viewmodel.LoginViewModel;

public class LoginFragment extends BaseFragment implements View.OnClickListener {
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

        requestFineLocationPermissions();
    }

    private void requestFineLocationPermissions() {
        if (!Permission.isPermissionGranted(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Permission.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    public void init() {
        ViewModelFactory factory =
                ViewModelFactory.Companion.getInstance(getActivity().getApplication());

        loginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);
        viewModelCallbacks(loginViewModel);

        binding.llCreateAccount.setOnClickListener(this);
        binding.btLogin.setOnClickListener(this);

        //((MainActivity)context).binding.rlToolbarMain.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        init();
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llCreateAccount:
                replaceFragment.replaceFragment(new SignupFragment(), SignupFragment.TAG, null, context);
                break;
            case R.id.btLogin:

                hideKeyboard();
                loginViewModel.login(binding.etEmail.getText().toString()
                        , binding.etPassword.getText().toString());
                //replaceFragment.replaceFragment(new MapFragment(),MapFragment.TAG,null,context);
                break;
        }
    }

    public void viewModelCallbacks(LoginViewModel viewModel) {
        viewModel.getSnackbarMessage().observe(getViewLifecycleOwner(), new Observer<OneShotEvent<String>>() {
            @Override
            public void onChanged(@Nullable OneShotEvent<String> stringOneShotEvent) {
                String msg = stringOneShotEvent.getContentIfNotHandled();
                if (msg != null)
                    showAlertDialog(msg);

            }
        });

        viewModel.getProgressBar().observe(getViewLifecycleOwner(), new Observer<OneShotEvent<Boolean>>() {
            @Override
            public void onChanged(@Nullable OneShotEvent<Boolean> booleanOneShotEvent) {
                Boolean flag = booleanOneShotEvent.getContentIfNotHandled();
                if(flag != null)
                showProgressDialog(flag);
            }
        });

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {

            Log.e("Response", "" + user.getImage());

            new SessionManager(getActivity()).setUserLoggedIn(true);
            new SessionManager(getActivity()).setUser(user);
            startActivity(new Intent(getActivity(), MainActivity.class));
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 50) {
            requestFineLocationPermissions();
        }
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
