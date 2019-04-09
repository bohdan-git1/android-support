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

    @POST("signup")
    fun register(@Body body: HashMap<String, String>): retrofit2.Call<UserResponse>

    @POST("login")
    fun login(@Body body: HashMap<String, String>): Call<UserResponse>

    @POST("add_song_on_location")
    fun addSong(@Body body: HashMap<String, String>): Call<SongResponse>

    @POST("get_user_song_list")
    fun getSongListing(@Body body: HashMap<String, String>): Call<SongListingResponse>

    @Multipart
    @POST("fans/{id}/media")
    fun uploadMedia(
            @Path("id") fanId: String,
            @Part file: MultipartBody.Part
            //@Part("file") file: RequestBody
    ): Call<User>


    @GET
    fun getPlacePredictions(@Url url: String): Call<PlacesAutoComplete>

}