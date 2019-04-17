package com.rapidzz.yourmusicmap.view.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.rapidzz.mymusicmap.other.extensions.OneShotEvent;
import com.rapidzz.mymusicmap.other.util.SessionManager;
import com.rapidzz.mymusicmap.view.activities.LandingActivity;
import com.rapidzz.yourmusicmap.databinding.FragmentSignupBinding;
import com.rapidzz.yourmusicmap.viewmodel.SignupViewModel;

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
        viewModel.getSnackbarMessage().observe(getViewLifecycleOwner(), new Observer<OneShotEvent<String>>() {
            @Override
            public void onChanged(@Nullable OneShotEvent<String> stringOneShotEvent) {
                String msg = stringOneShotEvent.getContentIfNotHandled();
                showAlertDialog(msg);

            }
        });

        viewModel.getProgressBar().observe(getViewLifecycleOwner(), new Observer<OneShotEvent<Boolean>>() {
            @Override
            public void onChanged(@Nullable OneShotEvent<Boolean> booleanOneShotEvent) {
                showProgressDialog(booleanOneShotEvent.getContentIfNotHandled());
            }
        });

        viewModel.mUserMutableLiveData.observe(getViewLifecycleOwner(), userResponse -> {

            showSuccessDialog(userResponse.getMessage(),"signup");
            new SessionManager(getActivity()).setUser(userResponse.getUser());
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
