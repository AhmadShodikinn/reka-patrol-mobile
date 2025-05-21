package com.project.rekapatrol.data.repository

import androidx.paging.PagingSource
import com.project.rekapatrol.data.pagingSource.CriteriasPagingSource
import com.project.rekapatrol.data.pagingSource.DocumentsPagingSource
import com.project.rekapatrol.data.pagingSource.InspeksiPagingSource
import com.project.rekapatrol.data.pagingSource.SafetyPatrolPagingSource
import com.project.rekapatrol.data.request.LoginRequest
import com.project.rekapatrol.data.response.CriteriaResponse
import com.project.rekapatrol.data.response.DataItemCriterias
import com.project.rekapatrol.data.response.DataItemDocuments
import com.project.rekapatrol.data.response.DataItemInspeksi
import com.project.rekapatrol.data.response.DataItemSafetyPatrols
import com.project.rekapatrol.data.response.DetailInspeksiResponse
import com.project.rekapatrol.data.response.InputInspeksiResponse
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import com.project.rekapatrol.data.response.ListSafetyPatrolsResponse
import com.project.rekapatrol.data.response.LoginResponse
import com.project.rekapatrol.data.response.LogoutResponse
import com.project.rekapatrol.data.response.TindakLanjutInspeksiResponse
import com.project.rekapatrol.data.response.TindakLanjutSafetyPatrolsResponse
import com.project.rekapatrol.network.ApiService
import com.project.rekapatrol.support.toIntPlainPart
import com.project.rekapatrol.support.toPlainPart
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File

class Repository(private val apiService: ApiService) {
    suspend fun authLogin(nip: String, password: String): Response<LoginResponse>{
        val bodyRequest = LoginRequest(nip, password)
        return apiService.authLogin(bodyRequest)
    }

    suspend fun authLogout(): Response<LogoutResponse>{
        return apiService.authLogout()
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

    fun getSafetyPatrolsPagingSource(): PagingSource<Int, DataItemSafetyPatrols> {
        return SafetyPatrolPagingSource(apiService)
    }

    suspend fun updateSafetyPatrols(
        safetyPatrolId: Int,
        actionDescription: String,
        actionImagePath: MultipartBody.Part
    ): Response<TindakLanjutSafetyPatrolsResponse> {
        return apiService.updateSafetyPatrol(
            safetyPatrolId = safetyPatrolId,
            _method = "PUT",
            actionDescription = actionDescription.toPlainPart(),
            actionPath = actionImagePath
        )
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

    fun fetchCriteriasSource(criteriaType: String, locationId: Int): PagingSource<Int, DataItemCriterias> {
        return CriteriasPagingSource(apiService, criteriaType, locationId)
    }

    fun getInspeksiPagingSource(): PagingSource<Int, DataItemInspeksi> {
        return InspeksiPagingSource(apiService)
    }

    suspend fun updateInspection(
        safetyPatrolId: Int,
        actionDescription: String,
        actionImagePath: MultipartBody.Part
    ): Response<TindakLanjutInspeksiResponse> {
        return apiService.updateInspection(
            safetyPatrolId = safetyPatrolId,
            _method = "PUT",
            actionDescription = actionDescription.toPlainPart(),
            actionPath = actionImagePath
        )
    }

    suspend fun getInspectionDetail(inspectionId: Int): Response<DetailInspeksiResponse> {
        return apiService.getDetailInspection(inspectionId)
    }

    fun getDocumentPagingSource(): PagingSource<Int, DataItemDocuments> {
        return DocumentsPagingSource(apiService)
    }
}