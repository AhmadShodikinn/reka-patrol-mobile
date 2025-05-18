package com.project.rekapatrol.data.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.rekapatrol.data.repository.Repository
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.json.JSONObject

class GeneralViewModel(
    private val repository: Repository,
    private val context: Context
): ViewModel() {

    private val _inputSafetyPatrolsResult = MutableLiveData<InputSafetyPatrolsResponse>()
    val inputSafetyPatrolsResponse: LiveData<InputSafetyPatrolsResponse> = _inputSafetyPatrolsResult

    fun inputSafetyPatrol(
        findingPaths: List<MultipartBody.Part>,  // Menggunakan List
        findingDescription: String,
        location: String,
        category: String,
        risk: String,
        checkupDate: String
    ) {
        viewModelScope.launch {
            try {
                val response = repository.inputSafetyPatrols(
                    findingPaths, findingDescription, location, category, risk, checkupDate
                )

                if (response.isSuccessful) {
                    _inputSafetyPatrolsResult.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = JSONObject(errorBody).getString("message")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("GeneralViewModel", "Server Error: ${e.message}", e)
                Toast.makeText(context, "Server Error!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
