package com.project.rekapatrol.data.repository

import com.project.rekapatrol.data.request.InputSafetyPatrolsRequest
import com.project.rekapatrol.data.request.LoginRequest
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import com.project.rekapatrol.data.response.LoginResponse
import com.project.rekapatrol.data.response.LogoutResponse
import com.project.rekapatrol.network.ApiService
import retrofit2.Response

class Repository(private val apiService: ApiService) {
    suspend fun authLogin(nip: String, password: String): Response<LoginResponse>{
        val bodyRequest = LoginRequest(nip, password)
        return apiService.authLogin(bodyRequest)
    }

    suspend fun authLogout(): Response<LogoutResponse>{
        return apiService.authLogout()
    }

    suspend fun inputSafetyPatrols(
        finding_path: List<String>,
        finding_description: String,
        location: String,
        category: String,
        risk: String,
        checkup_date: String
    ): Response<InputSafetyPatrolsResponse>{
        val bodyRequest = InputSafetyPatrolsRequest(
                finding_path, finding_description, location, category, risk, checkup_date
        )
        return apiService.inputSafetyPatrols(bodyRequest)
    }
}