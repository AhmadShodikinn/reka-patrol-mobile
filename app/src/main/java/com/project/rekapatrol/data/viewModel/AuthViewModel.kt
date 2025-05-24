package com.project.rekapatrol.data.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.rekapatrol.data.repository.Repository
import com.project.rekapatrol.data.response.LoginResponse
import com.project.rekapatrol.data.response.LogoutResponse
import com.project.rekapatrol.support.TokenHandler
import kotlinx.coroutines.launch
import org.json.JSONObject

class AuthViewModel(
    private val repository: Repository,
    private val tokenHandler: TokenHandler,
    private val context: Context
): ViewModel() {
    private val _authLoginResult = MutableLiveData<LoginResponse>()
    val authLoginResult: LiveData<LoginResponse> = _authLoginResult

    fun login(nip: String, password: String){
        viewModelScope.launch {
            try {
                val response = repository.authLogin(nip, password)

                if (response.isSuccessful) {
                    _authLoginResult.value = response.body()

                    response.body()?.token?.let {
                        tokenHandler.saveToken(it)
                    }

                    response.body()?.user?.position?.positionName?.let {
                        tokenHandler.saveUserRole(it)
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = JSONObject(errorBody).getString("message")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context,"Server Error!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}