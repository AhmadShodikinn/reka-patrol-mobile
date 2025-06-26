package com.project.rekapatrol.network

import android.util.Log
import com.project.rekapatrol.support.TokenHandler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

//    private const val BASE_URL = "http://10.0.2.2:8001/api/"
//    private const val BASE_URL = "http://192.168.18.5:8001/api/"
//    private const val BASE_URL = "http://103.211.26.90/api/"
    private const val BASE_URL = "https://rekapatrol.dikiahmad.site/api/"

    fun getApiService(tokenHandler: TokenHandler): ApiService {
//        Log.d("ApiConfig", "Mendapatkan ApiService dengan token: $token")

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient(tokenHandler))
            .build()
            .create(ApiService::class.java)
    }

    private fun getOkHttpClient(tokenHandler: TokenHandler): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

//        Log.d("ApiConfig", "Mengonfigurasi OkHttpClient dengan token: $token")

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(TokenInterceptor(tokenHandler))
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}
