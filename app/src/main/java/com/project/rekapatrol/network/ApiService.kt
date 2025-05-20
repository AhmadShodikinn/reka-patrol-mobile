package com.project.rekapatrol.network

import com.project.rekapatrol.data.request.InputSafetyPatrolsRequest
import com.project.rekapatrol.data.request.LoginRequest
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import com.project.rekapatrol.data.response.ListSafetyPatrolsResponse
import com.project.rekapatrol.data.response.LoginResponse
import com.project.rekapatrol.data.response.LogoutResponse
import com.project.rekapatrol.data.response.TindakLanjutSafetyPatrolsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("safety-patrols")
    suspend fun getInputSafetyPatrolsList(
        @Query("relations[]") relations: List<String> = listOf("pic"),
        @Query("per_page") perPage: Int = 2,
        @Query("page") page: Int = 2
    ): Response<ListSafetyPatrolsResponse>

    //perbaiki ini ye kang
    @PUT("safety-patrols/{id}")
    suspend fun updateSafetyPatrol(
        @Query("relations[]") relations: List<String> = listOf("findings"),
        @Path("id") safetyPatrolId: Int,
        @Part("action_description") actionDescription: RequestBody,
        @Part actionPath: MultipartBody.Part
    ): Response<TindakLanjutSafetyPatrolsResponse>

}