package com.project.rekapatrol.data.viewModel

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.project.rekapatrol.data.response.DetailInspeksiResponse
import com.project.rekapatrol.data.response.InputInspeksiResponse
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import com.project.rekapatrol.data.response.LogoutResponse
import com.project.rekapatrol.data.response.TindakLanjutInspeksiResponse
import com.project.rekapatrol.data.response.TindakLanjutSafetyPatrolsResponse
import com.project.rekapatrol.ui.helper.savePdfToDownloads
import com.project.rekapatrol.ui.helper.savePdftoDownloadUnder
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
    private val _authLogoutResult = MutableLiveData<LogoutResponse>()
    val authLogoutResult: LiveData<LogoutResponse> = _authLogoutResult

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

    private val _updateInspectionResults = MutableLiveData<TindakLanjutInspeksiResponse>()
    val updateInspectionResponse: LiveData<TindakLanjutInspeksiResponse> =_updateInspectionResults

    private val _inspectionDetailResuls = MutableLiveData<DetailInspeksiResponse>()
    val inspectionDetailResposne: LiveData<DetailInspeksiResponse> = _inspectionDetailResuls

    fun logout(){
        viewModelScope.launch {
            try {
                val response = repository.authLogout()

                if (response.isSuccessful) {
                    _authLogoutResult.value = response.body()
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
        suitability: Boolean,
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

    val inspeksiFlow = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { repository.getInspeksiPagingSource() }
    ).flow.cachedIn(viewModelScope)

    fun updateInspection(
        inspectionId: Int,
        actionDescription: String,
        actionImagePath: MultipartBody.Part
    ) {
        viewModelScope.launch {
            try {
                val response = repository.updateInspection(
                    inspectionId, actionDescription, actionImagePath
                )

                if (response.isSuccessful) {
                    _updateInspectionResults.value = response.body()
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

    fun getDetailInspection(inspectionId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getInspectionDetail(inspectionId)
                if (response.isSuccessful) {
                    _inspectionDetailResuls.value = response.body()
                } else {
                    val error = response.errorBody()?.string()
                    val message = JSONObject(error).getString("message")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("GeneralViewModel", "Error: ${e.message}", e)
                Toast.makeText(context, "Gagal mengambil data inspeksi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val documentFlow = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { repository.getDocumentPagingSource() }
    ).flow.cachedIn(viewModelScope)

    fun downloadDocument(context: Context, id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.downloadDocument(id)
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val fileName = "document_$id.pdf"

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            // Android 10+ pakai MediaStore
                            savePdfToDownloads(context, body, fileName)
                        } else {
                            // Android 9 ke bawah pakai cara lama
                            savePdftoDownloadUnder(context, body, fileName)
                        }
                    }
                } else {
                    Log.e("Download", "Gagal: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Download", "Exception: ${e.message}", e)
            }
        }
    }





}
