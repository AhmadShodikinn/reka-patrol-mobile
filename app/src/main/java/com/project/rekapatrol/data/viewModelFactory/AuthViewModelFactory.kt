package com.project.rekapatrol.data.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.rekapatrol.data.repository.Repository
import com.project.rekapatrol.data.viewModel.AuthViewModel
import com.project.rekapatrol.network.ApiConfig
import com.project.rekapatrol.support.TokenHandler

class AuthViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val tokenHandler = TokenHandler(context)
        val apiService = ApiConfig.getApiService("")
        val repository = Repository(apiService)

        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository, tokenHandler, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}