package com.project.rekapatrol.data.repository

import android.util.Log
import androidx.paging.PagingSource
import com.project.rekapatrol.data.pagingSource.CriteriasPagingSource
import com.project.rekapatrol.data.pagingSource.DocumentsPagingSource
import com.project.rekapatrol.data.pagingSource.InspeksiPagingSource
import com.project.rekapatrol.data.pagingSource.SafetyPatrolPagingSource
import com.project.rekapatrol.data.request.DateRangeRequest
import com.project.rekapatrol.data.request.LoginRequest
import com.project.rekapatrol.data.request.ResetPasswordRequest
import com.project.rekapatrol.data.response.DashboardNotifyResponse
import com.project.rekapatrol.data.response.DataItemCriterias
import com.project.rekapatrol.data.response.DataItemDocuments
import com.project.rekapatrol.data.response.DataItemInspeksi
import com.project.rekapatrol.data.response.DataItemSafetyPatrols
import com.project.rekapatrol.data.response.DetailInspeksiResponse
import com.project.rekapatrol.data.response.DetailSafetyPatrolResponse
import com.project.rekapatrol.data.response.InputInspeksiResponse
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import com.project.rekapatrol.data.response.LoginResponse
import com.project.rekapatrol.data.response.LogoutResponse
import com.project.rekapatrol.data.response.ResetPasswordResponse
import com.project.rekapatrol.data.response.TindakLanjutInspeksiResponse
import com.project.rekapatrol.data.response.TindakLanjutSafetyPatrolsResponse
import com.project.rekapatrol.data.response.UpdateInspeksiResponse
import com.project.rekapatrol.data.response.UpdateSafetyPatrolResponse
import com.project.rekapatrol.data.response.UpdateValidEntryInspectionResponse
import com.project.rekapatrol.data.response.UpdateValidEntryResponse
import com.project.rekapatrol.data.response.UploadMemosResponse
import com.project.rekapatrol.network.ApiService
import com.project.rekapatrol.support.toIntPlainPart
import com.project.rekapatrol.support.toPlainPart
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class Repository(private val apiService: ApiService) {
    suspend fun authLogin(nip: String, password: String): Response<LoginResponse>{
        val bodyRequest = LoginRequest(nip, password)
        return apiService.authLogin(bodyRequest)
    }

    suspend fun authLogout(): Response<LogoutResponse>{
        return apiService.authLogout()
    }

    suspend fun resetPassword(nip: String, password: String, passwordConfirmation: String): Response<ResetPasswordResponse> {
        val bodyRequest = ResetPasswordRequest(nip, password, passwordConfirmation)
        return apiService.resetPassword(bodyRequest)
    }

    suspend fun inputSafetyPatrols(
        findingPaths: List<MultipartBody.Part>,
        findingsDescription: String,
        location: String,
        category: String,
        risk: String,
        checkupDate: String
    ): Response<InputSafetyPatrolsResponse> {
        return apiService.inputSafetyPatrols(
            findingsDescription = findingsDescription.toPlainPart(),
            location = location.toPlainPart(),
            category = category.toPlainPart(),
            risk = risk.toPlainPart(),
            checkupDate = checkupDate.toPlainPart(),
            finding_paths = findingPaths
        )
    }

    fun getSafetyPatrolsPagingSource(fromYear: Int?, fromMonth: Int?): PagingSource<Int, DataItemSafetyPatrols> {
        return SafetyPatrolPagingSource(apiService, fromYear, fromMonth)
    }

    suspend fun getSafetyPatrolDetail(safetyPatrolId: Int): Response<DetailSafetyPatrolResponse> {
        return apiService.getDetailSafetyPatrol(safetyPatrolId)
    }

    suspend fun TindakLanjutSafetyPatrol(
        safetyPatrolId: Int,
        actionDescription: String,
        actionImagePath: MultipartBody.Part
    ): Response<TindakLanjutSafetyPatrolsResponse> {
        return apiService.TindakLanjutSafetyPatrol(
            safetyPatrolId = safetyPatrolId,
            _method = "PUT",
            actionDescription = actionDescription.toPlainPart(),
            actionPath = actionImagePath
        )
    }

    suspend fun updateSafetyPatrol(
        safetyPatrolId: Int,
        findingPaths: List<MultipartBody.Part>,
        findingsDescription: String,
        location: String,
        category: String,
        risk: String,
        checkupDate: String
    ): Response<UpdateSafetyPatrolResponse> {
        return apiService.updateSafetyPatrol(
            safetyPatrolId = safetyPatrolId,
            _method = "PUT",
            findingsDescription = findingsDescription.toPlainPart(),
            location = location.toPlainPart(),
            category = category.toPlainPart(),
            risk = risk.toPlainPart(),
            checkupDate = checkupDate.toPlainPart(),
            finding_paths = findingPaths
        )
    }

    suspend fun updateIsValidEntry(
        safetyPatrolId: Int,
        isValidEntry: Boolean
    ): Response<UpdateValidEntryResponse> {
        return apiService.updateValidEntry(
            safetyPatrolId = safetyPatrolId,
            _method = "PUT",
            isValidEntry = isValidEntry.toIntPlainPart()
        )
    }

    suspend fun deleteSafetyPatrol(safetyPatrolId: Int): Boolean {
        return try {
            val response = apiService.deleteSafetyPatrol(safetyPatrolId)
            response.isSuccessful
        } catch (e: HttpException) {
            Log.d("Repository", e.message())
            false
        }
    }

    suspend fun inputInspeksi(
        criteria_id: Int,
        findingPaths: List<MultipartBody.Part>,
        findings_description: String,
        inspection_location: String,
        value: String,
        suitability: Boolean,
        checkupDate: String
    ): Response<InputInspeksiResponse> {
        return apiService.inputInspeksi(
            criteria_id = criteria_id.toPlainPart(),
            findingsDescription = findings_description.toPlainPart(),
            inspection_location = inspection_location.toPlainPart(),
            value = value.toPlainPart(),
            suitability = suitability.toIntPlainPart(),
            checkupDate = checkupDate.toPlainPart(),
            finding_paths = findingPaths
        )
    }

    suspend fun updateInspection(
        safetyPatrolId: Int,
        findingPaths: List<MultipartBody.Part>,
        findings_description: String,
        inspection_location: String,
        value: String,
        suitability: Boolean,
        checkupDate: String
    ): Response<UpdateInspeksiResponse> {
        return apiService.updateInspection(
            safetyPatrolId = safetyPatrolId,
            _method = "PUT",
            findingsDescription = findings_description.toPlainPart(),
            inspection_location = inspection_location.toPlainPart(),
            value = value.toPlainPart(),
            suitability = suitability.toIntPlainPart(),
            checkupDate = checkupDate.toPlainPart(),
            finding_paths = findingPaths
        )
    }

    suspend fun updateIsValidEntryInspection(
        inspectionId: Int,
        isValidEntry: Boolean
    ): Response<UpdateValidEntryInspectionResponse> {
        return apiService.updateValidEntryInspection(
            safetyPatrolId = inspectionId,
            _method = "PUT",
            isValidEntry = isValidEntry.toIntPlainPart()
        )
    }

    suspend fun deleteInspection(inspectionId: Int): Boolean {
        return try {
            val response = apiService.deleteInspection(inspectionId)
            response.isSuccessful
            true
        } catch (e: HttpException) {
            Log.d("Repository", e.message())
            false
        }
    }


    fun fetchCriteriasSource(criteriaType: String, locationId: Int): PagingSource<Int, DataItemCriterias> {
        return CriteriasPagingSource(apiService, criteriaType, locationId)
    }

    fun getInspeksiPagingSource(fromYear: Int?, fromMonth: Int?): PagingSource<Int, DataItemInspeksi> {
        return InspeksiPagingSource(apiService, fromYear, fromMonth)
    }

    suspend fun TindakLanjutInspection(
        safetyPatrolId: Int,
        actionDescription: String,
        actionImagePath: MultipartBody.Part
    ): Response<TindakLanjutInspeksiResponse> {
        return apiService.TindakLanjutInspection(
            safetyPatrolId = safetyPatrolId,
            _method = "PUT",
            actionDescription = actionDescription.toPlainPart(),
            actionPath = actionImagePath
        )
    }

    suspend fun getInspectionDetail(inspectionId: Int): Response<DetailInspeksiResponse> {
        return apiService.getDetailInspection(inspectionId)
    }

    fun getDocumentPagingSource(documentType: String): PagingSource<Int, DataItemDocuments> {
        return DocumentsPagingSource(apiService, documentType)
    }

    suspend fun downloadDocument(id: Int): Response<ResponseBody> {
        return apiService.downloadDocument(id)
    }

    suspend fun getNotifyDashboard(): Response<DashboardNotifyResponse> {
        return apiService.getNotifyDashboard()
    }

    suspend fun downloadSafetyPatrolRecapExcel(
        fromDate: String,
        toDate: String
    ): Response<ResponseBody> {
        val dateRangeRequest = DateRangeRequest(
            from_date = fromDate,
            to_date = toDate
        )
        return apiService.downloadSafetyPatrolRecap(
            download = 1,
            dateRange = dateRangeRequest
        )
    }

    suspend fun downloadInspectionRecap(
        fromDate: String,
        toDate: String
    ): Response<ResponseBody> {
        val dateRangeRequest = DateRangeRequest(
            from_date = fromDate,
            to_date = toDate
        )
        return apiService.downloadInspectionRecap(
            download = 1,
            dateRange = dateRangeRequest
        )
    }

    suspend fun uploadMemos(
        filePart: MultipartBody.Part,
        documentType: String
    ): Response<UploadMemosResponse> {
        return apiService.uploadMemos(
            file = filePart,
            type = documentType.toPlainPart()
        )
    }

}