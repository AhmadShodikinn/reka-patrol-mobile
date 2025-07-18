package com.project.rekapatrol.data.response

import com.google.gson.annotations.SerializedName

data class UpdateValidEntryInspectionResponse(

	@field:SerializedName("data")
	val data: DataValidEntryInspection? = null
)

data class DataValidEntryInspection(

	@field:SerializedName("checkup_date")
	val checkupDate: String? = null,

	@field:SerializedName("pic_id")
	val picId: Int? = null,

	@field:SerializedName("suitability")
	val suitability: Int? = null,

	@field:SerializedName("criteria")
	val criteria: CriteriaValidEntryInspection? = null,

	@field:SerializedName("findings")
	val findings: List<Any?>? = null,

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
	val actionDescription: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("findings_description")
	val findingsDescription: String? = null,

	@field:SerializedName("is_valid_entry")
	val isValidEntry: String? = null,

	@field:SerializedName("value")
	val value: String? = null
)

data class CriteriaValidEntryInspection(

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
