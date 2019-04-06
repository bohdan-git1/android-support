package com.rapidzz.mymusicmap.datamodel.source

import android.content.Context
import com.rapidzz.mymusicmap.datamodel.model.fan.User
import com.rapidzz.mymusicmap.datamodel.model.responses.*
import com.rapidzz.mymusicmap.datamodel.source.remote.ApiService
import com.rapidzz.mymusicmap.datamodel.source.remote.RetrofitClientInstance
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class UserRepository(ctx: Context) {
    var context: Context
    init {
        context = ctx
    }


    fun getApiService(): ApiService {
        return RetrofitClientInstance.getInstance(context)!!.getService()
    }


    fun login(email: String, password: String, callback: UserDataSource.LoginCallback) {
        val params: HashMap<String, String> = HashMap()
        params.let {
            it.put("username", email)
            it.put("email", email.toLowerCase())
            it.put("password", password)
        }

        getApiService().register(params).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    callback.onLogin(response.body()!!)
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun signup(name: String, email: String, password: String, phone: String, callback: UserDataSource.RegisterCallback) {
        val params: HashMap<String, String> = HashMap()
        params.let {
            it.put("name", name)
            it.put("email", email.toLowerCase())
            it.put("password", password)
            it.put("phone_no", phone)
        }

        getApiService().register(params).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    callback.onRegister(response.body()!!)
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }
}