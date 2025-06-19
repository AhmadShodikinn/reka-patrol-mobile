package com.project.rekapatrol.network

import android.util.Log
import com.project.rekapatrol.support.SessionHandler
import com.project.rekapatrol.support.TokenHandler
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenInterceptor(private val tokenHandler: TokenHandler): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenHandler.getToken()
        val requestBuilder = chain.request().newBuilder()
            .header("Accept", "application/json")

        token?.let {
            requestBuilder
                .header("Authorization", "Bearer $it")
        }

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401) {
            Log.w("TokenInterceptor", "401 Unauthorized – menghapus token dari penyimpanan")
            tokenHandler.removeToken()
            SessionHandler.triggerSessionExpired()
        }

        return response
//
//        Log.d("TokenInterceptor", "Interceptor dipanggil")
//        Log.d("TokenInterceptor", "Token yang dikirim: $token")
//
//        val newRequest: Request = chain.request().newBuilder()
//            .header("Accept", "application/json")
//            .header("Authorization", "Bearer $token")
//            .build()
//        return chain.proceed(newRequest)
    }

}