package com.rapidzz.mymusicmap.datamodel.source.remote

import android.content.Context
import android.util.Log
import com.rapidzz.mymusicmap.other.util.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory


class RetrofitClientInstance (ctx: Context){

    private var retrofit: Retrofit? = null
    private val BASE_URL = "http://192.168.43.173:8000/api/"
    private val httpClient = OkHttpClient.Builder()
    var context: Context



    init {
        context = ctx
        if (retrofit == null) {
            initRetrofit()
        }
    }

    fun initRetrofit(){
        var retrofitBuilder = retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());
       /* val authToken = SessionManager(context).getAuthenticationToken()
        if (authToken.isNotEmpty()) {
            val interceptor = AuthenticationInterceptor(authToken,context)
            httpClient.addInterceptor(interceptor)

            //Enable Logging
            //val loggingIntercepter = HttpLoggingInterceptor()
            val loggingIntercepter = getLoggingInterceptor()
            loggingIntercepter.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(loggingIntercepter)
            //

            retrofitBuilder.client(httpClient.build())
        }else{*/
            val interceptor = AuthenticationInterceptor("",context)
            httpClient.addInterceptor(interceptor)
            //Enable Logging
            //val loggingIntercepter = HttpLoggingInterceptor()
            val loggingIntercepter = getLoggingInterceptor()
            loggingIntercepter.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(loggingIntercepter)
            //

            retrofitBuilder.client(httpClient.build())
     //   }

        retrofit = retrofitBuilder.build()

    }

    fun getService(): ApiService {
        return retrofit!!.create<ApiService>(ApiService::class.java!!)
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor{
        val loggingIntercepter = HttpLoggingInterceptor()

        return loggingIntercepter
    }

    fun getRetrofit(): Retrofit? {
        return retrofit
    }


    companion object {
        var singleInstance: RetrofitClientInstance? = null

        fun getInstance(context: Context): RetrofitClientInstance? {
            if (singleInstance == null)
                singleInstance = RetrofitClientInstance(context)

            return singleInstance
        }
    }

    class AuthenticationInterceptor internal constructor(private val authToken: String,private val context: Context) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()

            val builder = original.newBuilder()
                .header("x-access-token", authToken)
            Log.d("x-access-token", authToken)

            val request = builder.build()

            //return chain.proceed(request)

            val response = chain.proceed(request)
            if (response.code() == 401) {
                SessionManager(context).logout()
                SessionManager(context).redirectToLogin(context,response.message())
/*
                val intent = Intent(context,LandingActivity::class.java)
                intent.putExtra(LandingActivity.SKIP_SPLASH,true)
                intent.putExtra(LandingActivity.START_UP_MESSAGe,response.message())
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
*/
            }
            return response
        }
    }

}