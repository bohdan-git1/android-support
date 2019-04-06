package com.rapidzz.mymusicmap.datamodel.source.remote
import com.rapidzz.mymusicmap.datamodel.model.responses.*
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST


interface ApiService {

    @POST("signup")
    fun register(@Body body: HashMap<String, String>): Call<UserResponse>

    @POST("login")
    fun login(@Body body: HashMap<String, String>): Call<UserResponse>


}