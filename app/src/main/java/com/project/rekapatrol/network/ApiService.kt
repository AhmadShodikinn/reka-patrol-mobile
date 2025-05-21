package com.project.rekapatrol.network

import com.project.rekapatrol.data.request.LoginRequest
import com.project.rekapatrol.data.response.CriteriaResponse
import com.project.rekapatrol.data.response.DetailInspeksiResponse
import com.project.rekapatrol.data.response.InputInspeksiResponse
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import com.project.rekapatrol.data.response.ListInspeksiResponse
import com.project.rekapatrol.data.response.ListSafetyPatrolsResponse
import com.project.rekapatrol.data.response.LoginResponse
import com.project.rekapatrol.data.response.LogoutResponse
import com.project.rekapatrol.data.response.TindakLanjutInspeksiResponse
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

    @Multipart
    @POST("safety-patrols/{id}")
    suspend fun updateSafetyPatrol(
        @Path("id") safetyPatrolId: Int,
        @Query("_method") _method:String,
        @Query("relations[]") relations: List<String> = listOf("findings"),
        @Part("action_description") actionDescription: RequestBody,
        @Part actionPath: MultipartBody.Part
    ): Response<TindakLanjutSafetyPatrolsResponse>

    @Multipart
    @POST("inspections")
    suspend fun inputInspeksi(
        @Part("criteria_id") criteria_id: RequestBody,
        @Part("findings_description") findingsDescription: RequestBody,
        @Part("inspection_location") inspection_location: RequestBody,
        @Part("value") value: RequestBody,
        @Part("suitability") suitability: RequestBody,
        @Part("checkup_date") checkupDate: RequestBody,
        @Part finding_paths: List<MultipartBody.Part>
    ): Response<InputInspeksiResponse>

    @GET("criterias")
    suspend fun getCriterias(
        @Query("relations[]") relations: String = "location",
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1,
        @Query("criteria_type") criteriaType: String,
        @Query("location_id") locationId: Int
    ): Response<CriteriaResponse>

    @GET("inspections")
    suspend fun getInspectionsList(
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1
    ): Response<ListInspeksiResponse>

    @Multipart
    @POST("inspections/{id}")
    suspend fun updateInspection(
        @Path("id") safetyPatrolId: Int,
        @Query("_method") _method:String,
        @Part("action_description") actionDescription: RequestBody,
        @Part actionPath: MultipartBody.Part
    ): Response<TindakLanjutInspeksiResponse>

    @GET("inspections/{id}")
    suspend fun getDetailInspection(
        @Path("id") id: Int
    ): Response<DetailInspeksiResponse>

}