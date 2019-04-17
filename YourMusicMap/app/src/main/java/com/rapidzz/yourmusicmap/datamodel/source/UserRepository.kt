package com.rapidzz.mymusicmap.datamodel.source

import android.content.Context
import com.rapidzz.mymusicmap.datamodel.model.fan.User
import com.rapidzz.mymusicmap.datamodel.model.responses.*
import com.rapidzz.mymusicmap.datamodel.source.remote.ApiService
import com.rapidzz.mymusicmap.datamodel.source.remote.RetrofitClientInstance
import com.rapidzz.yourmusicmap.datamodel.model.PlacesAutoComplete
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
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
                    if (response.body()?.error == false)
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

    fun isTrackAvailable(latitude: String, longitude: String, callback: UserDataSource.IsTrackAvailableCallback) {
        val params: HashMap<String, String> = HashMap()

        params.let {
            it.put("lat", latitude)
            it.put("long", longitude)
        }


        getApiService().isTrackAvailable(params).enqueue(object : Callback<IsTrackAvailableResponse> {
            override fun onResponse(call: Call<IsTrackAvailableResponse>, response: Response<IsTrackAvailableResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.error == false)
                        callback.onResponse(!response.body()!!.error, response.body()!!.message)
                    else
                        callback.onResponse(!response.body()!!.error, response.body()!!.message)
                } else {
                    callback.onError(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<IsTrackAvailableResponse>, t: Throwable) {
                callback.onError(t.message!!)
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
                    if (response.body()?.error == false)
                        callback.onRegister(response.body()!!)
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

    fun saveSong(title: String, path: String, userId: Int, lat: Double, lng: Double, callback: UserDataSource.SaveSongCallback) {
        val params: HashMap<String, String> = HashMap()
        /* params.let {
             it.put("title", title)
             it.put("path", path)
             it.put("user_id", userId)
             it.put("lat", lat)
             it.put("lng", lng)
         }
 */
        val MEDIA_TYPE_PNG = MediaType.parse("image/jpeg")
        val file = File(path)
        val requestBody = RequestBody.create(MEDIA_TYPE_PNG, file)
        val body = MultipartBody.Part.createFormData("track", file.name, requestBody)

        getApiService().addSong(title, path, userId, lat, lng, body).enqueue(object : Callback<SongResponse> {
            override fun onResponse(call: Call<SongResponse>, response: Response<SongResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.error == false)
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

    fun getSongListing(userId: String, callback: UserDataSource.GetSongCallback) {
        val params: HashMap<String, String> = HashMap()
        params.let {

            it.put("user_id", userId)
        }

        getApiService().getSongListing(params).enqueue(object : Callback<SongListingResponse> {
            override fun onResponse(call: Call<SongListingResponse>, response: Response<SongListingResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.error == false)
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

    fun uploadMedia(userId: String, filePath: String, callback: UserDataSource.LoginCallback) {
        val MEDIA_TYPE_PNG = MediaType.parse("image/jpeg")
        val file = File(filePath)
        val requestBody = RequestBody.create(MEDIA_TYPE_PNG, file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestBody)

        getApiService().uploadMedia(userId, body).enqueue(object : Callback<User> {
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
}