package com.rapidzz.mymusicmap.other.factory

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.VisibleForTesting
import com.rapidzz.mymusicmap.datamodel.source.UserRepository
import com.rapidzz.mymusicmap.viewmodel.*
import com.rapidzz.yourmusicmap.viewmodel.LoginViewModel


/**
 * A creator is used to inject the product ID into the ViewModel
 *
 *
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
class ViewModelFactory private constructor(
    private val application: Application,
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(RegisterViewModel::class.java) ->
                    RegisterViewModel(application, userRepository)
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(application, userRepository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE
                ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE
                    ?: ViewModelFactory(
                        application,
                        UserRepository(application.applicationContext)
                    )
                    .also { INSTANCE = it }
            }


        @VisibleForTesting fun destroyInstance() {
            INSTANCE = null
        }
    }
}
