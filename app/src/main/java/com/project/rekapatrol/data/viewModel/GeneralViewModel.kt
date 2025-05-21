package com.project.rekapatrol.data.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.rekapatrol.data.repository.Repository
import com.project.rekapatrol.data.response.CriteriaResponse
import com.project.rekapatrol.data.response.DataItemCriterias
import com.project.rekapatrol.data.response.DataItemSafetyPatrols
import com.project.rekapatrol.data.response.InputInspeksiResponse
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import com.project.rekapatrol.data.response.TindakLanjutSafetyPatrolsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _updateSafetyPatrolsResults = MutableLiveData<TindakLanjutSafetyPatrolsResponse>()
    val updateSafetyPatrolsResponse: LiveData<TindakLanjutSafetyPatrolsResponse> = _updateSafetyPatrolsResults

    private val _inputInspeksiResult = MutableLiveData<InputInspeksiResponse>()
    val inputInspeksiResponse: LiveData<InputInspeksiResponse> = _inputInspeksiResult

    private val _listCriteriaResult = MutableStateFlow<List<DataItemCriterias>>(emptyList())
    val listCriteriaResult: StateFlow<List<DataItemCriterias>> = _listCriteriaResult

    fun inputSafetyPatrol(
        findingPaths: List<MultipartBody.Part>,
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

    fun updateSafetyPatrol(
        safetyPatrolId: Int,
        actionDescription: String,
        actionImagePath: MultipartBody.Part
    ) {
        viewModelScope.launch {
            try {
                val response = repository.updateSafetyPatrols(
                    safetyPatrolId, actionDescription, actionImagePath
                )

                if (response.isSuccessful) {
                    _updateSafetyPatrolsResults.value = response.body()
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

    val safetyPatrolFlow = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { repository.getSafetyPatrolsPagingSource() }
    ).flow.cachedIn(viewModelScope)

    fun inputInspeksi(
        criteriaId: Int,
        findingPaths: List<MultipartBody.Part>,
        findingsDescription: String,
        inspectionLocation: String,
        value: String,
        suitability: String,
        checkupDate: String
    ) {
        viewModelScope.launch {
            try {
                val response = repository.inputInspeksi(
                    criteria_id = criteriaId,
                    findingPaths = findingPaths,
                    findings_description = findingsDescription,
                    inspection_location = inspectionLocation,
                    value = value,
                    suitability = suitability,
                    checkupDate = checkupDate
                )

                if (response.isSuccessful) {
                    _inputInspeksiResult.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = JSONObject(errorBody).optString("message", "Gagal input inspeksi")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("GeneralViewModel", "Error inputInspeksi: ${e.message}", e)
                Toast.makeText(context, "Terjadi kesalahan saat input inspeksi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getCriteriasPaging(criteriaType: String, locationId: Int): Flow<PagingData<DataItemCriterias>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                repository.fetchCriteriasSource(criteriaType, locationId)
            }
        ).flow.cachedIn(viewModelScope)
    }



}
