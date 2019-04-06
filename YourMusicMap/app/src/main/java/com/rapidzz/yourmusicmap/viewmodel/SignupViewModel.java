package com.rapidzz.yourmusicmap.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Bundle;
import android.print.PageRange;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.rapidzz.mymusicmap.datamodel.model.fan.User;
import com.rapidzz.mymusicmap.datamodel.model.responses.ApiErrorResponse;
import com.rapidzz.mymusicmap.datamodel.source.UserDataSource;
import com.rapidzz.mymusicmap.datamodel.source.UserRepository;
import com.rapidzz.yourmusicmap.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

public class SignupViewModel extends BaseAndroidViewModel {


    public static final String GOOGLE_REQUEST_ID_TOKEN = "419424438944-tt3qjrffb5vfkofce570fcbb1moscmqa.apps.googleusercontent.com";
    public static final String FB_LOGIN_PERMISSIONS = "email,id,first_name," +
            "last_name,middle_name," +
            "name,name_format,picture,short_name";

    private CallbackManager mCallbackManager;
    private CallbackFacebookLogin mCallbackFacebookLogin;
    private UserRepository mUserRepository;

    private GoogleSignInClient mGoogleSignInClient;
    //var user: MutableLiveData<User> = MutableLiveData()

    public MutableLiveData<User> mUserMutableLiveData = new MutableLiveData();

    public SignupViewModel(@NonNull Application application) {
        super(application);
        mUserRepository = new UserRepository(application);
    }

    public GoogleSignInClient setupGoogleLogin(Context context){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GOOGLE_REQUEST_ID_TOKEN)
                .requestEmail()
                .build();

        return mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public CallbackManager setupFacebookLogin(){

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("FB-Login", "FB-Login -> onSuccess : $loginResult");
                GraphRequest gRequest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), (object, response) -> {

                            try {
                                String name = object.getString("name");
                                String email = object.getString("email");
                                String profilePic = object.getJSONObject("picture")
                                        .getJSONObject("data").getString("url");

                                if(mCallbackFacebookLogin != null)
                                    mCallbackFacebookLogin.onFacebookLoginResponse(name, email);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        });

                Bundle params = new Bundle();
                params.putString("fields", FB_LOGIN_PERMISSIONS);
                gRequest.setParameters(params);
                gRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("FB-Login", "FB-Login -> Canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("FB-Login", "FB-Login -> onError : " + error.getMessage());
            }
        });

        return mCallbackManager;
    }

    public void setFacebookLoginCallback(CallbackFacebookLogin mCallbackFacebookLogin){
        this.mCallbackFacebookLogin = mCallbackFacebookLogin;
    }

    public void signup(String name, String email, String phoneNo, String password){

        if(email.trim().isEmpty() || password.isEmpty())
            showSnackbarMessage(getString(R.string.error_message_enter_missing_detail));
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()  || password.isEmpty())
            showSnackbarMessage(getString(R.string.error_message_email_password_invalid));
        else{
            showProgressBar(true);
            mUserRepository.signup(name, email, password, phoneNo, new UserDataSource.RegisterCallback() {
                @Override
                public void onRegister(@NotNull User user) {
                    Log.e("Success",user.toString());
                    showProgressBar(false);
                    mUserMutableLiveData.setValue(user);
                }

                @Override
                public void onPayloadError(@NotNull ApiErrorResponse error) {
                    Log.e("Success",error.toString());
                    showProgressBar(false);
                    showSnackbarMessage(error.getMessage());
                }
            });
        }
    }

    public interface CallbackFacebookLogin{
        void onFacebookLoginResponse(String personName, String personEmail);
    }
}
