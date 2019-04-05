package com.rapidzz.yourmusicmap.viewmodel

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Patterns
import com.rapidzz.mymusicmap.datamodel.model.fan.User
import com.rapidzz.mymusicmap.datamodel.model.responses.ApiErrorResponse
import com.rapidzz.mymusicmap.datamodel.model.responses.AuthenticateResponse
import com.rapidzz.mymusicmap.datamodel.source.UserDataSource
import com.rapidzz.mymusicmap.datamodel.source.UserRepository
import com.rapidzz.mymusicmap.other.util.SessionManager
import com.rapidzz.yourmusicmap.R


class LoginViewModel(context: Application, private val userRepository: UserRepository) : BaseAndroidViewModel(context){

    private lateinit var user: MutableLiveData<User>

    fun login(email: String, password: String) {
            if(email.trim().isNullOrEmpty() || password.isNullOrEmpty())
                showSnackbarMessage(getString(R.string.error_message_enter_missing_detail))
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()  || password.isNullOrEmpty())
                showSnackbarMessage(getString(R.string.error_message_email_password_invalid))
            else{
                showProgressBar(true)
                userRepository.login(email,password, object : UserDataSource.LoginCallback {
                    override fun onLogin(user: User) {

                    }

                    override fun onPayloadError(error: ApiErrorResponse) {
                        showProgressBar(false)
                        showSnackbarMessage(error.message)
                    }
                })
            }
    }
}