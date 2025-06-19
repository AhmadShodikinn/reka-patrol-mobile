package com.project.rekapatrol.data.viewModelFactory

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.rekapatrol.data.repository.Repository
import com.project.rekapatrol.data.viewModel.GeneralViewModel
import com.project.rekapatrol.network.ApiConfig
import com.project.rekapatrol.network.ApiService
import com.project.rekapatrol.support.TokenHandler

class GeneralViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val tokenHandler = TokenHandler(context)
//        val token = tokenHandler.getToken() ?: throw IllegalStateException("Token kosong")

//        Log.e("GeneralViewModelFactory", "Token: $token")
        val apiService = ApiConfig.getApiService(tokenHandler)
        val repository = Repository(apiService)

        return GeneralViewModel(repository, context) as T
    }
}