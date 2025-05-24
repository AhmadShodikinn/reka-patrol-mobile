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
        private const val TOKEN_TIMESTAMP_KEY = "token_timestamp"
        private const val ROLE_KEY = "user_role"
        private const val TWO_HOURS_IN_MILLIS = 2 * 60 * 60 * 1000 //2 jam token hilang
    }

    fun saveToken(token: String) {
        val currentTime = System.currentTimeMillis()
        sharedPreferences.edit()
            .putString(TOKEN_KEY, token)
            .putLong(TOKEN_TIMESTAMP_KEY, currentTime)
            .apply()
    }

    fun getToken(): String? {
        val token = sharedPreferences.getString(TOKEN_KEY, null)
        val timestamp = sharedPreferences.getLong(TOKEN_TIMESTAMP_KEY, 0)

        if (token == null || System.currentTimeMillis() - timestamp > TWO_HOURS_IN_MILLIS) {
            Log.d("TokenHandler", "Token expired or missing, removing token.")
            removeToken()
            return null
        }

        Log.d("TokenHandler", "Retrieved token: $token")
        return token
    }

    fun removeToken() {
        sharedPreferences.edit()
            .remove(TOKEN_KEY)
            .remove(TOKEN_TIMESTAMP_KEY)
            .remove(ROLE_KEY)
            .apply()
    }

    fun saveUserRole(role: String) {
        sharedPreferences.edit()
            .putString(ROLE_KEY, role)
            .apply()
    }

    fun getUserRole(): String? {
        return sharedPreferences.getString(ROLE_KEY, null)
    }

    fun removeUserRole() {
        sharedPreferences.edit()
            .remove(ROLE_KEY)
            .apply()
    }
}
