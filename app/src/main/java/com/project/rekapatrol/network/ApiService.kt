package com.project.rekapatrol.network

import com.project.rekapatrol.data.request.InputSafetyPatrolsRequest
import com.project.rekapatrol.data.request.LoginRequest
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import com.project.rekapatrol.data.response.LoginResponse
import com.project.rekapatrol.data.response.LogoutResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun authLogin(@Body request: LoginRequest): Response<LoginResponse>

    @POST("logout")
    suspend fun authLogout(): Response<LogoutResponse>

    @POST("safety-patrols")
    suspend fun inputSafetyPatrols(@Body request: InputSafetyPatrolsRequest): Response<InputSafetyPatrolsResponse>

}