package com.rapidzz.mymusicmap.datamodel.source.remote
import com.rapidzz.mymusicmap.datamodel.model.fan.User
import com.rapidzz.mymusicmap.datamodel.model.responses.*
import com.rapidzz.yourmusicmap.datamodel.model.PlacesAutoComplete
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST
import java.util.*


interface ApiService {

    @POST("is_track_available")
    fun isTrackAvailable(@Body body: HashMap<String, String>): Call<IsTrackAvailableResponse>

    @POST("signup")
    fun register(@Body body: HashMap<String, String>): retrofit2.Call<UserResponse>

    @POST("login")
    fun login(@Body body: HashMap<String, String>): Call<UserResponse>

    @POST("add_song_on_location")
    fun addSong(@Body body: HashMap<String, String>): Call<SongResponse>

    @POST("get_user_song_list")
    fun getSongListing(@Body body: HashMap<String, String>): Call<SongListingResponse>

    @Multipart
    @POST("update_image")
    fun uploadMedia(
            @Part("id") fanId: String,
            @Part file: MultipartBody.Part
            //@Part("file") file: RequestBody
    ): Call<User>


    @Multipart
    @POST("add_song_on_location")
    fun addSong(
            @Part("title") title: String,
            @Part("path") path: String,
            @Part("user_id") user_id: Int,
            @Part("lat") lat: Double,
            @Part("lng") lng: Double,
            @Part file: MultipartBody.Part
            //@Part("file") file: RequestBody
    ): Call<SongResponse>


    @GET
    fun getPlacePredictions(@Url url: String): Call<PlacesAutoComplete>



}