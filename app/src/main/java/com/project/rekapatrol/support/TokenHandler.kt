package com.project.rekapatrol.support

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class TokenHandler(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "userPref", Context.MODE_PRIVATE
    )

    companion object {
        private const val TOKEN_KEY = "token"
    }

    fun saveToken(token: String){
        Log.d("TokenHandler", "Saving token: $token")
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        val token = sharedPreferences.getString(TOKEN_KEY, null)
        Log.d("TokenHandler", "Retrieved token: $token")
        return token
    }

}