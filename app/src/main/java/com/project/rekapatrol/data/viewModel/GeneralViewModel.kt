package com.project.rekapatrol.data.viewModel

import android.content.Context
import android.os.Build
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
import com.project.rekapatrol.data.response.DashboardNotifyResponse
import com.project.rekapatrol.data.response.DataItemCriterias
import com.project.rekapatrol.data.response.DataItemDocuments
import com.project.rekapatrol.data.response.DataItemInspeksi
import com.project.rekapatrol.data.response.DataItemSafetyPatrols
import com.project.rekapatrol.data.response.DetailInspeksiResponse
import com.project.rekapatrol.data.response.DetailSafetyPatrolResponse
import com.project.rekapatrol.data.response.InputInspeksiResponse
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import com.project.rekapatrol.data.response.LogoutResponse
import com.project.rekapatrol.data.response.TindakLanjutInspeksiResponse
import com.project.rekapatrol.data.response.TindakLanjutSafetyPatrolsResponse
import com.project.rekapatrol.data.response.UpdateInspeksiMemoResponse
import com.project.rekapatrol.data.response.UpdateInspeksiResponse
import com.project.rekapatrol.data.response.UpdateSafetyMemoResponse
import com.project.rekapatrol.data.response.UpdateSafetyPatrolResponse
import com.project.rekapatrol.data.response.UpdateValidEntryInspectionResponse
import com.project.rekapatrol.data.response.UpdateValidEntryResponse
import com.project.rekapatrol.data.response.UploadMemosResponse
import com.project.rekapatrol.ui.helper.savePdfToDownloads
import com.project.rekapatrol.ui.helper.savePdftoDownloadUnder
import com.project.rekapatrol.ui.helper.getCurrentMonthDateRange
import com.project.rekapatrol.ui.helper.saveExcelToDownloadUnder
import com.project.rekapatrol.ui.helper.saveExcelToDownloads
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.json.JSONObject

class GeneralViewModel(
    private val repository: Repository,
    private val context: Context
): ViewModel() {
    private val _authLogoutResult = MutableLiveData<LogoutResponse>()
    val authLogoutResult: LiveData<LogoutResponse> = _authLogoutResult

    private val _sessionExpired = MutableLiveData<Boolean>()
    val sessionExpired: LiveData<Boolean> get() = _sessionExpired

    private val _inputSafetyPatrolsResult = MutableLiveData<InputSafetyPatrolsResponse>()
    val inputSafetyPatrolsResponse: LiveData<InputSafetyPatrolsResponse> = _inputSafetyPatrolsResult

    private val _safetyPatrolList = MutableLiveData<List<DataItemSafetyPatrols>>()
    val safetyPatrolList: LiveData<List<DataItemSafetyPatrols>> = _safetyPatrolList

    private val _updateSafetyPatrolsActionResults = MutableLiveData<TindakLanjutSafetyPatrolsResponse>()
    val updateSafetyPatrolsActionResponse: LiveData<TindakLanjutSafetyPatrolsResponse> = _updateSafetyPatrolsActionResults

    private val _updateValidEntryResult = MutableLiveData<UpdateValidEntryResponse?>()
    val updateValidEntryResult: LiveData<UpdateValidEntryResponse?> get() = _updateValidEntryResult

    private val _updateValidEntryInspectionResult = MutableLiveData<UpdateValidEntryInspectionResponse?>()
    val updateValidEntryInspectionResult: LiveData<UpdateValidEntryInspectionResponse?> get() = _updateValidEntryInspectionResult

    private val _updateHasMemoResult = MutableLiveData<UpdateInspeksiMemoResponse>()
    val updateHasMemoResult: LiveData<UpdateInspeksiMemoResponse> = _updateHasMemoResult

    private val _updateHasMemoSafetyResult = MutableLiveData<UpdateSafetyMemoResponse>()
    val updateHasMemoSafetyResult: LiveData<UpdateSafetyMemoResponse> = _updateHasMemoSafetyResult

    private val _updateSafetyPatrolResults = MutableLiveData<UpdateSafetyPatrolResponse>()
    val updateSafetyPatrolResponse: LiveData<UpdateSafetyPatrolResponse> = _updateSafetyPatrolResults

    private val _deleteSafetyPatrolStatus = MutableStateFlow<Boolean?>(null)
    val deleteSafetyPatrolStatus: StateFlow<Boolean?> = _deleteSafetyPatrolStatus.asStateFlow()

    private val _safetyPatrolDetailResult = MutableLiveData<DetailSafetyPatrolResponse>()
    val safetyPatrolDetailResponse: LiveData<DetailSafetyPatrolResponse> = _safetyPatrolDetailResult

    private val _inputInspeksiResult = MutableLiveData<InputInspeksiResponse>()
    val inputInspeksiResponse: LiveData<InputInspeksiResponse> = _inputInspeksiResult

    private val _listCriteriaResult = MutableStateFlow<List<DataItemCriterias>>(emptyList())
    val listCriteriaResult: StateFlow<List<DataItemCriterias>> = _listCriteriaResult

    private val _updateInspectionResults = MutableLiveData<UpdateInspeksiResponse>()
    val updateInspectionResponse: LiveData<UpdateInspeksiResponse> = _updateInspectionResults

    private val _updateInspectionActionResults = MutableLiveData<TindakLanjutInspeksiResponse>()
    val updateInspectionActionResponse: LiveData<TindakLanjutInspeksiResponse> =_updateInspectionActionResults

    private val _inspectionDetailResuls = MutableLiveData<DetailInspeksiResponse>()
    val inspectionDetailResposne: LiveData<DetailInspeksiResponse> = _inspectionDetailResuls

    private val _deleteInspectionStatus = MutableStateFlow<Boolean?>(null)
    val deleteInspectionStatus: StateFlow<Boolean?> = _deleteInspectionStatus.asStateFlow()

    private val _dashboardNotificationResult = MutableLiveData<DashboardNotifyResponse?>()
    val dashboardNotificationResponse: LiveData<DashboardNotifyResponse?> = _dashboardNotificationResult

    private val _uploadMemosResult = MutableLiveData<UploadMemosResponse>()
    val uploadMemosResult: LiveData<UploadMemosResponse> = _uploadMemosResult

    private val _totalUnsolved = MutableLiveData<Int>()
    val totalUnsolved: LiveData<Int> = _totalUnsolved

    fun onSessionExpired() {
        _sessionExpired.postValue(true)
    }

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

    fun getDetailSafetyPatrol(safetyPatrolId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getSafetyPatrolDetail(safetyPatrolId)

                if (response.isSuccessful) {
                    _safetyPatrolDetailResult.value = response.body()
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

    fun TindakLanjutSafetyPatrol(
        safetyPatrolId: Int,
        actionDescription: String,
        actionImagePath: MultipartBody.Part
    ) {
        viewModelScope.launch {
            try {
                val response = repository.TindakLanjutSafetyPatrol(
                    safetyPatrolId, actionDescription, actionImagePath
                )

                if (response.isSuccessful) {
                    _updateSafetyPatrolsActionResults.value = response.body()
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
        findingPaths: List<MultipartBody.Part>,
        findingDescription: String,
        location: String,
        category: String,
        risk: String,
        checkupDate: String
    ) {
        viewModelScope.launch {
            try {
                val response = repository.updateSafetyPatrol(
                    safetyPatrolId,
                    findingPaths,
                    findingDescription,
                    location,
                    category,
                    risk,
                    checkupDate
                )

                if (response.isSuccessful) {
                    _updateSafetyPatrolResults.value = response.body()
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

    fun updateIsValidEntry(safetyPatrolId: Int, isValidEntry: Boolean, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.updateIsValidEntry(safetyPatrolId, isValidEntry)

                if (response.isSuccessful) {
                    _updateValidEntryResult.value = response.body()
                    onComplete()
                    Toast.makeText(context, "Status validasi berhasil diperbarui.", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = JSONObject(errorBody).getString("message")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("GeneralViewModel", "Server Error: ${e.message}", e)
                Toast.makeText(context, "Gagal update validasi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateSafetyHasMemo(
        safetyPatrolId: Int,
        hasMemo: Int?,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.updateSafetyHasMemo(safetyPatrolId, hasMemo)

                if (response.isSuccessful) {
                    _updateHasMemoSafetyResult.value = response.body()
                    onComplete()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = JSONObject(errorBody ?: "{}").optString("message", "Gagal update memo pada temuan.")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("GeneralViewModel", "Error updateSafetyHasMemo: ${e.message}", e)
                Toast.makeText(context, "Gagal update memo!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteSafetyPatrol(safetyPatrolId: Int) {
        viewModelScope.launch {
            _deleteSafetyPatrolStatus.value = null
            try {
                val success = repository.deleteSafetyPatrol(safetyPatrolId)
                _deleteSafetyPatrolStatus.value = success
                Toast.makeText(context, if (success) "Safety Patrol berhasil dihapus!" else "Gagal menghapus Safety Patrol.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("GeneralViewModel", "Server Error: ${e.message}", e)
                Toast.makeText(context, "Server error!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getSafetyPatrolFlow(toDate: String?, fromDate: String?, isValid: String?): Flow<PagingData<DataItemSafetyPatrols>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { repository.getSafetyPatrolsPagingSource(toDate, fromDate, isValid) }
        ).flow.cachedIn(viewModelScope)
    }

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

    fun getInspeksiFlow(fromDate: String?, toDate: String?, isValid: String?): Flow<PagingData<DataItemInspeksi>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {repository.getInspeksiPagingSource(fromDate, toDate, isValid)}
        ).flow.cachedIn(viewModelScope)
    }

    fun TindakLanjutInspection(
        inspectionId: Int,
        actionDescription: String,
        actionImagePath: MultipartBody.Part
    ) {
        viewModelScope.launch {
            try {
                val response = repository.TindakLanjutInspection(
                    inspectionId, actionDescription, actionImagePath
                )

                if (response.isSuccessful) {
                    _updateInspectionActionResults.value = response.body()
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

    fun updateInspection(
        inspectionId: Int,
        findingPaths: List<MultipartBody.Part>,
        findingsDescription: String,
        inspectionLocation: String,
        value: String,
        suitability: Boolean,
        checkupDate: String
    ) {
        viewModelScope.launch {
            try {
                val response = repository.updateInspection(
                    safetyPatrolId = inspectionId,
                    findingPaths = findingPaths,
                    findings_description = findingsDescription,
                    inspection_location = inspectionLocation,
                    value = value,
                    suitability = suitability,
                    checkupDate = checkupDate
                )

                if (response.isSuccessful){
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

    fun updateIsValidEntryInspection(inspectionId: Int, isValidEntry: Boolean, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.updateIsValidEntryInspection(inspectionId, isValidEntry)

                if (response.isSuccessful) {
                    _updateValidEntryInspectionResult.value = response.body()
                    onComplete()
                    Toast.makeText(context, "Status validasi berhasil diperbarui.", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = JSONObject(errorBody).getString("message")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("GeneralViewModel", "Server Error: ${e.message}", e)
                Toast.makeText(context, "Gagal update validasi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateInspectionHasMemo(inspectionId: Int, hasMemo: Int?, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.updateInspectionHasMemo(inspectionId, hasMemo)

                if (response.isSuccessful) {
                    _updateHasMemoResult.value = response.body()
                    onComplete()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = JSONObject(errorBody ?: "{}").optString("message", "Gagal update memo pada temuan.")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("GeneralViewModel", "updateInspectionHasMemo error: ${e.message}", e)
                Toast.makeText(context, "Terjadi kesalahan saat update memo pada temuan.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteInspection(inspectionId: Int) {
        viewModelScope.launch {
            _deleteInspectionStatus.value = null
            try {
                val success = repository.deleteInspection(inspectionId)
                _deleteInspectionStatus.value = success
                Toast.makeText(context, if (success) "Inspeksi berhasil dihapus!" else "Gagal menghapus Inspeksi.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("GeneralViewModel", "Server Error: ${e.message}", e)
                Toast.makeText(context, "Server error!", Toast.LENGTH_SHORT).show()
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

    fun getDocumentFlow(documentType: String): Flow<PagingData<DataItemDocuments>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { repository.getDocumentPagingSource(documentType) }
        ).flow.cachedIn(viewModelScope)
    }

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

    fun getInformationDashboard() {
        viewModelScope.launch {
            try {
                val response = repository.getNotifyDashboard()
                if (response.isSuccessful) {
                    val body = response.body()
                    _dashboardNotificationResult.value = body

                    val totalUnsolvedValue = listOfNotNull(
                        body?.data?.inspections?.unsolved,
                        body?.data?.safetyPatrols?.unsolved
                    ).sum()

                    _totalUnsolved.value = totalUnsolvedValue

                } else {
                    val error = response.errorBody()?.string()
                    val message = JSONObject(error).getString("message")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("GeneralViewModel", "Error: ${e.message}", e)
                Toast.makeText(context, "Gagal mengambil data notifikasi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun downloadSafetyPatrolRecapExcel() {
        val (fromDate, toDate) = getCurrentMonthDateRange()

        viewModelScope.launch {
            try {
                val response = repository.downloadSafetyPatrolRecapExcel(
                    fromDate,
                    toDate
                )
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val fileName = "rekap_safety_patrol_${fromDate}_to_${toDate}.xlsx"
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            saveExcelToDownloads(context, body, fileName)
                        } else {
                            saveExcelToDownloadUnder(context, body, fileName)
                        }
                    }
                } else {
                    val message = JSONObject(response.errorBody()?.string() ?: "").optString("message", "Download gagal")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Terjadi kesalahan saat download", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun downloadInspectionRecapExcel() {
        val (fromDate, toDate) = getCurrentMonthDateRange()

        viewModelScope.launch {
            try {
                val response = repository.downloadInspectionRecap(
                    fromDate,
                    toDate
                )
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val fileName = "rekap_inspeksi${fromDate}_to_${toDate}.xlsx"
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            saveExcelToDownloads(context, body, fileName)
                        } else {
                            saveExcelToDownloadUnder(context, body, fileName)
                        }
                    }
                } else {
                    val message = JSONObject(response.errorBody()?.string() ?: "").optString("message", "Download gagal")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Terjadi kesalahan saat download", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun uploadMemos(filePart: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val response = repository.uploadMemos(filePart, "memo")
                if (response.isSuccessful) {
                    _uploadMemosResult.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = JSONObject(errorBody ?: "{}").optString("message", "Gagal upload dokumen memo")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("GeneralViewModel", "uploadMemos error: ${e.message}", e)
                Toast.makeText(context, "Terjadi kesalahan saat upload dokumen memo", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
