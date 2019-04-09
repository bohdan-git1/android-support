package com.rapidzz.yourmusicmap.viewmodel

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Patterns
import com.rapidzz.mymusicmap.datamodel.model.fan.User
import com.rapidzz.mymusicmap.datamodel.model.responses.ApiErrorResponse
import com.rapidzz.mymusicmap.datamodel.source.UserDataSource
import com.rapidzz.mymusicmap.datamodel.source.UserRepository
import com.rapidzz.yourmusicmap.R


class LoginViewModel(context: Application, private val userRepository: UserRepository) : BaseAndroidViewModel(context){


    lateinit var mUser: MutableLiveData<User>

    fun getUser(): LiveData<User> {
        if (!::mUser.isInitialized) {
            mUser = MutableLiveData()
        }
        return mUser
    }

    fun login(email: String, password: String) {
            if(email.trim().isNullOrEmpty() || password.isNullOrEmpty())
                showSnackbarMessage(getString(R.string.error_message_enter_missing_detail))
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()  || password.isNullOrEmpty())
                showSnackbarMessage(getString(R.string.error_message_email_password_invalid))
            else{
                showProgressBar(true)
                userRepository.login(email,password, object : UserDataSource.LoginCallback {
                    override fun onLogin(user: User) {
                        showProgressBar(false)
                        mUser.value = user;
                    }

                    override fun onPayloadError(error: ApiErrorResponse) {
                        showProgressBar(false)
                        showSnackbarMessage(error.message)
                    }
                })
            }
    }

    fun uploadMedia(userId: String, filePath: String) {

        userRepository.uploadMedia(userId,filePath, object : UserDataSource.LoginCallback {
            override fun onLogin(user: User) {
                showProgressBar(false)
                mUser.value = user;
            }

            override fun onPayloadError(error: ApiErrorResponse) {
                showProgressBar(false)
                showSnackbarMessage(error.message)
            }
        })
    }


}