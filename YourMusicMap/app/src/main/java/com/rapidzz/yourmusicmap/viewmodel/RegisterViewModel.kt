package com.rapidzz.mymusicmap.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import android.util.Patterns
import com.rapidzz.mymusicmap.datamodel.model.fan.User
import com.rapidzz.mymusicmap.datamodel.source.UserRepository
import com.rapidzz.yourmusicmap.R
import com.rapidzz.yourmusicmap.viewmodel.BaseAndroidViewModel

class RegisterViewModel(context: Application, private val userRepository: UserRepository) : BaseAndroidViewModel(context) {

    var user: MutableLiveData<User> = MutableLiveData()


}