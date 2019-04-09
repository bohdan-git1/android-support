package com.rapidzz.mymusicmap.datamodel.source.remote
import com.rapidzz.mymusicmap.datamodel.model.responses.*
import com.rapidzz.yourmusicmap.datamodel.model.PlacesAutoComplete
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST
import java.util.*


interface ApiService {

    @POST("signup")
    fun register(@Body body: HashMap<String, String>): retrofit2.Call<UserResponse>

    @POST("login")
    fun login(@Body body: HashMap<String, String>): Call<UserResponse>

    @POST("add_song_on_location")
    fun addSong(@Body body: HashMap<String, String>): Call<SongResponse>

    @POST("get_user_song_list")
    fun getSongListing(@Body body: HashMap<String, String>): Call<SongListingResponse>

    @GET
    fun getPlacePredictions(@Url url: String): Call<PlacesAutoComplete>

}