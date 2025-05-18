package com.project.rekapatrol.data.repository

import com.project.rekapatrol.data.request.InputSafetyPatrolsRequest
import com.project.rekapatrol.data.request.LoginRequest
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import com.project.rekapatrol.data.response.LoginResponse
import com.project.rekapatrol.data.response.LogoutResponse
import com.project.rekapatrol.network.ApiService
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


}