package com.rapidzz.yourmusicmap.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.rapidzz.mymusicmap.datamodel.model.fan.User;
import com.rapidzz.mymusicmap.other.extensions.AppExtKt;
import com.rapidzz.mymusicmap.other.extensions.OneShotEvent;
import com.rapidzz.mymusicmap.view.activities.LandingActivity;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.databinding.FragmentSignupBinding;
import com.rapidzz.yourmusicmap.other.Event;
import com.rapidzz.yourmusicmap.other.util.SnackbarUtils;
import com.rapidzz.yourmusicmap.viewmodel.LoginViewModel;
import com.rapidzz.yourmusicmap.viewmodel.SignupViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static android.app.Activity.RESULT_OK;


public class SignupFragment extends BaseFragment implements SignupViewModel.CallbackFacebookLogin {
    public static final String TAG = SignupFragment.class.getSimpleName();

    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";

    private static final int RC_GOOGLE_SIGN_IN = 101;

    CallbackManager mCallbackManager;
    GoogleSignInClient mGoogleSignInClient;

    SignupViewModel mSignupViewModel;
    FragmentSignupBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater);
        init();
        return binding.getRoot();
    }

    private void init() {
        mSignupViewModel = ViewModelProviders.of(this).get(SignupViewModel.class);
        viewModelCallbacks(mSignupViewModel);

        mSignupViewModel.setFacebookLoginCallback(this);
        mGoogleSignInClient = mSignupViewModel.setupGoogleLogin(context);

        binding.llFacebook.setOnClickListener(__->{
            LoginManager.getInstance().logInWithReadPermissions(
                    this, Arrays.asList(EMAIL, PUBLIC_PROFILE));
        });

        binding.llGoogle.setOnClickListener(__->{
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_GOOGLE_SIGN_IN);
        });

//        binding.fbLoginBtn.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE));
        ((LandingActivity)context).mCallbackManager =
                mCallbackManager = mSignupViewModel.setupFacebookLogin();

        binding.btSubmit.setOnClickListener(v -> {
            mSignupViewModel.signup(binding.etName.getText().toString()
                    ,binding.etEmail.getText().toString()
                    ,binding.etMobileNo.getText().toString()
                    ,binding.etPassword.getText().toString());
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((LandingActivity)context).mCallbackManager
                .onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }

        if(requestCode == RC_GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acct = task.getResult(ApiException.class);
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                binding.etName.setText(personName);
                binding.etEmail.setText(personEmail);

                Log.e(TAG, "Google login success with data!");
            } catch (ApiException e) {
                e.printStackTrace();
                Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    public void viewModelCallbacks(SignupViewModel viewModel){
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

        viewModel.mUserMutableLiveData.observe(this, user -> {

            //Log.e("Response",""+user.getFirstName());
            //getFragmentManager().popBackStack();
        });
    }

    @Override
    public void onFacebookLoginResponse(String personName, String personEmail) {
        Log.e(TAG, "onFacebookLoginResponse -> graphResponse.");

        binding.etName.setText(personName);
        binding.etEmail.setText(personEmail);
    }

}
