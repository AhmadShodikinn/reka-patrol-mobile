package com.project.rekapatrol.network

import com.project.rekapatrol.data.request.InputSafetyPatrolsRequest
import com.project.rekapatrol.data.request.LoginRequest
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import com.project.rekapatrol.data.response.LoginResponse
import com.project.rekapatrol.data.response.LogoutResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @POST("login")
    suspend fun authLogin(@Body request: LoginRequest): Response<LoginResponse>

    @POST("logout")
    suspend fun authLogout(): Response<LogoutResponse>

    @Multipart
    @POST("safety-patrols")
    suspend fun inputSafetyPatrols(
        @Part("findings_description") findingsDescription: RequestBody,
        @Part("location") location: RequestBody,
        @Part("category") category: RequestBody,
        @Part("risk") risk: RequestBody,
        @Part("checkup_date") checkupDate: RequestBody,
        @Part finding_paths: List<MultipartBody.Part>
    ): Response<InputSafetyPatrolsResponse>



}