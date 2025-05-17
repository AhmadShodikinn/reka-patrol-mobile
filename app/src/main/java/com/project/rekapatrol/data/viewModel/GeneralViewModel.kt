package com.project.rekapatrol.data.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.rekapatrol.data.repository.Repository
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import kotlinx.coroutines.launch
import org.json.JSONObject

class GeneralViewModel(
    private val repository: Repository,
    private val context: Context
): ViewModel() {
    private val _inputSafetyPatrolsResult = MutableLiveData<InputSafetyPatrolsResponse>()
    val inputSafetyPatrolsResponse: LiveData<InputSafetyPatrolsResponse> = _inputSafetyPatrolsResult

    fun inputSafetyPatrol(
        finding_path: List<String>,
        finding_description: String,
        location: String,
        category: String,
        risk: String,
        checkup_date: String
    ){
        viewModelScope.launch { 
            try {
                val response = repository.inputSafetyPatrols(
                    finding_path, finding_description, location, category, risk, checkup_date
                )

                if (response.isSuccessful) {
                    _inputSafetyPatrolsResult.value = response.body()
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