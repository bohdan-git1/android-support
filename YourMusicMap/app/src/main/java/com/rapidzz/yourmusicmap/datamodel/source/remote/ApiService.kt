package com.rapidzz.mymusicmap.datamodel.source.remote
import com.rapidzz.mymusicmap.datamodel.model.fan.User
import com.rapidzz.mymusicmap.datamodel.model.responses.*
import retrofit2.Call
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.Multipart
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import rx.Observable


interface ApiService {

    @POST("fans")
    fun register(@Body body: HashMap<String, String>): Call<User>

    @POST("fans/authenticate")
    fun authenticate(@Body body: HashMap<String, String>): Call<AuthenticateResponse>

    @GET("fans/{id}")
    fun getFanInformation(@Path("id") fanId: String): Call<User>

    @POST("fans/reset-password")
    fun forgotPassword(@Body body: HashMap<String, String>): Call<ForgotPasswordResponse>

    @POST("fans/{id}/change-password")
    fun changePassword(@Path("id") fanId: String, @Body body: HashMap<String, String>): Call<NoContentResponse>

    @POST("fans/{id}/verify")
    fun resendAccountVerificationEmail(@Path("id") fanId: String, @Body body: NoContentResponse): Call<NoContentResponse>

    @PUT("fans/{id}")
    fun updateProfile(@Path("id") fanId: String, @Body body: User): Call<User>

}