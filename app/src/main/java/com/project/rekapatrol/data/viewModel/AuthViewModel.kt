package com.project.rekapatrol.data.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.rekapatrol.data.repository.Repository
import com.project.rekapatrol.data.response.LoginResponse
import com.project.rekapatrol.data.response.LogoutResponse
import com.project.rekapatrol.data.response.ResetPasswordResponse
import com.project.rekapatrol.data.response.ResetPasswordResult
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

    private val _resetPasswordResult = MutableLiveData<ResetPasswordResult>()
    val resetPasswordResult: LiveData<ResetPasswordResult> get() = _resetPasswordResult

    fun login(nip: String, password: String){
        viewModelScope.launch {
            try {
                val response = repository.authLogin(nip, password)

                if (response.isSuccessful) {
                    _authLoginResult.value = response.body()

                    response.body()?.user?.positionId.let {
                        if (it != 1) {
                            response.body()?.token?.let {
                                tokenHandler.saveToken(it)
                            }
                            response.body()?.user?.position?.positionName?.let {
                                tokenHandler.saveUserRole(it)
                                Toast.makeText(context, "Selamat datang, $it", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Admin tidak bisa akses mobile!", Toast.LENGTH_SHORT).show()
                        }
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

    fun resetPassword(nip: String, password: String, passwordConfirmation: String) {
        viewModelScope.launch {
            try {
                val response = repository.resetPassword(nip, password, passwordConfirmation)

                if (response.isSuccessful && response.body() != null) {
                    _resetPasswordResult.value = ResetPasswordResult(
                        success = true,
                        message = response.body()!!.message,
                        data = response.body()
                    )
                } else {
                    val errorBody = response.errorBody()?.string()
                    val json = JSONObject(errorBody ?: "{}")
                    val message = json.optString("message", "Terjadi kesalahan.")
                    val errors = json.optJSONObject("errors")
                    val firstPasswordError = errors?.optJSONArray("password")?.optString(0)

                    _resetPasswordResult.value = ResetPasswordResult(
                        success = false,
                        message = firstPasswordError ?: message
                    )
                }
            } catch (e: Exception) {
                _resetPasswordResult.value = ResetPasswordResult(
                    success = false,
                    message = "Terjadi kesalahan jaringan atau server."
                )
            }
        }
    }
}