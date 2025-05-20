package com.project.rekapatrol.data.response

import com.google.gson.annotations.SerializedName

data class InputSafetyPatrolsResponse(

	@field:SerializedName("data")
	val data: DataSafetyPatrols? = null
)

data class FindingsItemSafetyPatrol(

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

data class DataSafetyPatrols(

	@field:SerializedName("checkup_date")
	val checkupDate: String? = null,

	@field:SerializedName("pic_id")
	val picId: Any? = null,

	@field:SerializedName("findings")
	val findings: List<FindingsItemSafetyPatrol?>? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("action_path")
	val actionPath: Any? = null,

	@field:SerializedName("worker_id")
	val workerId: Int? = null,

	@field:SerializedName("action_description")
	val actionDescription: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("risk")
	val risk: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("findings_description")
	val findingsDescription: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)
