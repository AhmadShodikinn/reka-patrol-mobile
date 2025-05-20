package com.project.rekapatrol.data.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.rekapatrol.data.repository.Repository
import com.project.rekapatrol.data.response.DataItemSafetyPatrols
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

    private val _safetyPatrolList = MutableLiveData<List<DataItemSafetyPatrols>>()
    val safetyPatrolList: LiveData<List<DataItemSafetyPatrols>> = _safetyPatrolList

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


    fun fetchSafetyPatrolList(perPage: Int = 10, page: Int = 1) {
        viewModelScope.launch {
            try {
                val response = repository.getSafetyPatrolsList(
                    relations = listOf("pic"),
                    perPage = perPage,
                    page = page
                )

                if (response.isSuccessful) {
                    val list = response.body()?.data?.filterNotNull() ?: emptyList()
                    _safetyPatrolList.value = list
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("GeneralViewModel", "Fetch gagal: $error")
                }
            } catch (e: Exception) {
                Log.e("GeneralViewModel", "Server error: ${e.message}", e)
            }
        }
    }

}
