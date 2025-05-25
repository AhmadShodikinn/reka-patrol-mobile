package com.project.rekapatrol.data.response

import com.google.gson.annotations.SerializedName

data class TindakLanjutInspeksiResponse(

	@field:SerializedName("data")
	val data: DataTindakLanjut? = null
)

data class FindingsItemTindakLanjut(

	@field:SerializedName("findable_id")
	val findableId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("findable_type")
	val findableType: String? = null,

	@field:SerializedName("image_path")
	val imagePath: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class DataTindakLanjut(

	@field:SerializedName("checkup_date")
	val checkupDate: String? = null,

	@field:SerializedName("pic_id")
	val picId: String? = null,

	@field:SerializedName("suitability")
	val suitability: Int? = null,

	@field:SerializedName("criteria")
	val criteria: CriteriaTindakLanjut? = null,

	@field:SerializedName("findings")
	val findings: List<FindingsItemTindakLanjut?>? = null,

	@field:SerializedName("criteria_id")
	val criteriaId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("action_path")
	val actionPath: String? = null,

	@field:SerializedName("worker_id")
	val workerId: Int? = null,

	@field:SerializedName("inspection_location")
	val inspectionLocation: String? = null,

	@field:SerializedName("action_description")
	val actionDescription: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("findings_description")
	val findingsDescription: String? = null,

	@field:SerializedName("value")
	val value: String? = null
)

data class CriteriaTindakLanjut(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("criteria_type")
	val criteriaType: String? = null,

	@field:SerializedName("location_id")
	val locationId: Int? = null,

	@field:SerializedName("criteria_name")
	val criteriaName: String? = null
)
