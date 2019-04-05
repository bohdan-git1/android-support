package com.rapidzz.yourmusicmap.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.rapidzz.mymusicmap.other.extensions.OneShotEvent


open class BaseAndroidViewModel(context: Application) : AndroidViewModel(context) {

    init {

    }

    val snackbarMessage = MutableLiveData<OneShotEvent<String>>()
    val progressBar = MutableLiveData<OneShotEvent<Boolean>>()

    protected fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }

    protected fun getString(resId: Int): String{
        return getContext().getString(resId)
    }

    protected fun showSnackbarMessage(message: String) {
        snackbarMessage.value = OneShotEvent(message)
    }

    protected fun showProgressBar(show: Boolean){
        progressBar.value = OneShotEvent(show)
    }
}