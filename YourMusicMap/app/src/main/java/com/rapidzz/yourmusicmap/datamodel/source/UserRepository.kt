package com.rapidzz.mymusicmap.datamodel.source

import android.content.Context
import com.rapidzz.mymusicmap.datamodel.model.responses.*
import com.rapidzz.mymusicmap.datamodel.source.remote.ApiService
import com.rapidzz.mymusicmap.datamodel.source.remote.RetrofitClientInstance
import com.rapidzz.yourmusicmap.datamodel.model.PlacesAutoComplete
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
            it.put("password", password)
        }

        getApiService().login(params).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    if(response.body()?.error == false)
                        callback.onLogin(response.body()?.user!!)
                    else
                        callback.onPayloadError(ApiErrorResponse(
                                400,
                                response.body()?.message!!,
                                ""))

                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun signup(name: String, email: String, password: String, phone: String, callback: UserDataSource.RegisterCallback) {
        val params: HashMap<String, String> = HashMap()
        params.let {
            it.put("name", name)
            it.put("username", email.toLowerCase())
            it.put("password", password)
            it.put("mobile_no", phone)
        }

        getApiService().register(params).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    if(response.body()?.error == false)
                    callback.onRegister(response.body()?.user!!)
                    else
                        callback.onPayloadError(ApiErrorResponse(
                                400,
                                response.body()?.message!!,
                                ""))
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun saveSong(title: String, path: String, userId: String, lat: String, lng: String, callback: UserDataSource.SaveSongCallback) {
        val params: HashMap<String, String> = HashMap()
        params.let {
            it.put("title", title)
            it.put("path", path)
            it.put("user_id", userId)
            it.put("lat", lat)
            it.put("lng", lng)
        }

        getApiService().addSong(params).enqueue(object : Callback<SongResponse> {
            override fun onResponse(call: Call<SongResponse>, response: Response<SongResponse>) {
                if (response.isSuccessful) {
                    if(response.body()?.error == false)
                        callback.onSaveSong(response.body()!!)
                    else
                        callback.onPayloadError(ApiErrorResponse(
                                400,
                                response.body()?.message!!,
                                ""))
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }

    fun getSongListing( userId: String, callback: UserDataSource.GetSongCallback) {
        val params: HashMap<String, String> = HashMap()
        params.let {

            it.put("user_id", userId)
          }

        getApiService().getSongListing(params).enqueue(object : Callback<SongListingResponse> {
            override fun onResponse(call: Call<SongListingResponse>, response: Response<SongListingResponse>) {
                if (response.isSuccessful) {
                    if(response.body()?.error == false)
                        callback.onSongListing(response.body()!!)
                    else
                        callback.onPayloadError(ApiErrorResponse(
                                400,
                                response.body()?.message!!,
                                ""))
                } else {
                    callback.onPayloadError(ErrorUtils.parseError(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<SongListingResponse>, t: Throwable) {
                callback.onPayloadError(ErrorUtils.parseError(t))
            }
        })
    }
}