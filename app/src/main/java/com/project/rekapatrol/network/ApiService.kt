package com.project.rekapatrol.network

import com.project.rekapatrol.data.request.DateRangeRequest
import com.project.rekapatrol.data.request.LoginRequest
import com.project.rekapatrol.data.response.CriteriaResponse
import com.project.rekapatrol.data.response.DashboardNotifyResponse
import com.project.rekapatrol.data.response.DetailInspeksiResponse
import com.project.rekapatrol.data.response.DetailSafetyPatrolResponse
import com.project.rekapatrol.data.response.InputInspeksiResponse
import com.project.rekapatrol.data.response.InputSafetyPatrolsResponse
import com.project.rekapatrol.data.response.ListDocumentResponse
import com.project.rekapatrol.data.response.ListInspeksiResponse
import com.project.rekapatrol.data.response.ListSafetyPatrolsResponse
import com.project.rekapatrol.data.response.LoginResponse
import com.project.rekapatrol.data.response.LogoutResponse
import com.project.rekapatrol.data.response.TindakLanjutInspeksiResponse
import com.project.rekapatrol.data.response.TindakLanjutSafetyPatrolsResponse
import com.project.rekapatrol.data.response.UpdateInspeksiResponse
import com.project.rekapatrol.data.response.UpdateSafetyPatrolResponse
import com.project.rekapatrol.data.response.UploadMemosResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //Autentikasi
    @POST("login")
    suspend fun authLogin(
        @Body request: LoginRequest,
        @Query("relations") relations: String = "position"
    ): Response<LoginResponse>

    @POST("logout")
    suspend fun authLogout(): Response<LogoutResponse>
    //End of autentikasi

    //Safety-patrol
    //input safety-patrol
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

    //get list safety-patrol
    @GET("safety-patrols")
    suspend fun getInputSafetyPatrolsList(
        @Query("relations[]") relations: List<String> = listOf("pic"),
        @Query("per_page") perPage: Int = 2,
        @Query("page") page: Int = 2
    ): Response<ListSafetyPatrolsResponse>

    //get safety-patrol by id
    @GET("safety-patrols/{id}")
    suspend fun getDetailSafetyPatrol(
        @Path("id") id: Int,
        @Query("relations[]") relationsWorker: String = "worker",
        @Query("relations[]") relationsPic: String = "pic",
        @Query("relations[]") relationsFindings: String = "findings",
    ): Response<DetailSafetyPatrolResponse>

    //input tindak lanjut safety-patrol
    @Multipart
    @POST("safety-patrols/{id}")
    suspend fun TindakLanjutSafetyPatrol(
        @Path("id") safetyPatrolId: Int,
        @Query("_method") _method:String,
        @Query("relations[]") relations: List<String> = listOf("findings"),
        @Part("action_description") actionDescription: RequestBody,
        @Part actionPath: MultipartBody.Part
    ): Response<TindakLanjutSafetyPatrolsResponse>

    //update safety-patrol
    @Multipart
    @POST("safety-patrols/{id}")
    suspend fun updateSafetyPatrol(
        @Path("id") safetyPatrolId: Int,
        @Query("_method") _method:String,
        @Part("findings_description") findingsDescription: RequestBody,
        @Part("location") location: RequestBody,
        @Part("category") category: RequestBody,
        @Part("risk") risk: RequestBody,
        @Part("checkup_date") checkupDate: RequestBody,
        @Part finding_paths: List<MultipartBody.Part>
    ): Response<UpdateSafetyPatrolResponse>

    @DELETE("safety-patrols/{id}")
    suspend fun deleteSafetyPatrol(
        @Path("id") safetyPatrolId: Int
    ): Response<Unit>
    //End of safety-patrol

    //inspection
    //input inspection
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

    //get criterias
    @GET("criterias")
    suspend fun getCriterias(
        @Query("relations[]") relations: String = "location",
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1,
        @Query("criteria_type") criteriaType: String,
        @Query("location_id") locationId: Int
    ): Response<CriteriaResponse>

    //get inspections list
    @GET("inspections")
    suspend fun getInspectionsList(
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1
    ): Response<ListInspeksiResponse>

    //tindak lanjut inspection
    @Multipart
    @POST("inspections/{id}")
    suspend fun TindakLanjutInspection(
        @Path("id") safetyPatrolId: Int,
        @Query("_method") _method:String,
        @Part("action_description") actionDescription: RequestBody,
        @Part actionPath: MultipartBody.Part
    ): Response<TindakLanjutInspeksiResponse>

    @Multipart
    @POST("inspections/{id}")
    suspend fun updateInspection(
        @Path("id") safetyPatrolId: Int,
        @Query("_method") _method:String,
        @Part("findings_description") findingsDescription: RequestBody,
        @Part("inspection_location") inspection_location: RequestBody,
        @Part("value") value: RequestBody,
        @Part("suitability") suitability: RequestBody,
        @Part("checkup_date") checkupDate: RequestBody,
        @Part finding_paths: List<MultipartBody.Part>
    ): Response<UpdateInspeksiResponse>

    @DELETE("inspections/{id}")
    suspend fun deleteInspection(
        @Path("id") inspectionId: Int
    ): Response<Unit>

    //get detail inspection
    @GET("inspections/{id}")
    suspend fun getDetailInspection(
        @Path("id") id: Int,
        @Query("relations[]") relationsCriteria: String = "criteria",
        @Query("relations[]") relationsFindings: String = "findings",
    ): Response<DetailInspeksiResponse>
    //end of inspection


    @GET("documents")
    suspend fun getDocuments(
        @Query("relations[]") relations: List<String> = listOf("user"),
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1,
        @Query("document_type") documentType: String
    ): Response<ListDocumentResponse>

    @GET("documents/{id}")
    suspend fun downloadDocument(
        @Path("id") id: Int,
        @Query("download") download: Int = 1
    ): Response<ResponseBody>

    @GET("dashboards")
    suspend fun getNotifyDashboard(): Response<DashboardNotifyResponse>

    @POST("safety-patrol-recaps")
    suspend fun downloadSafetyPatrolRecap(
        @Query("download") download: Int = 1,
        @Body dateRange: DateRangeRequest
    ): Response<ResponseBody>

    @POST("inspection-recaps")
    suspend fun downloadInspectionRecap(
        @Query("download") download: Int = 1,
        @Body dateRange: DateRangeRequest
    ): Response<ResponseBody>

    @Multipart
    @POST("documents")
    suspend fun uploadMemos(
        @Part file: MultipartBody.Part,
        @Part("type") type: RequestBody
    ): Response<UploadMemosResponse>

}