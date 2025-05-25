package com.project.rekapatrol.data.response

import com.google.gson.annotations.SerializedName

data class UpdateSafetyPatrolResponse(

	@field:SerializedName("data")
	val data: Data? = null
)

data class FindingsItem(

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

data class Data(

	@field:SerializedName("checkup_date")
	val checkupDate: String? = null,

	@field:SerializedName("pic_id")
	val picId: Any? = null,

	@field:SerializedName("findings")
	val findings: List<FindingsItem?>? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("action_path")
	val actionPath: String? = null,

	@field:SerializedName("worker_id")
	val workerId: Int? = null,

	@field:SerializedName("action_description")
	val actionDescription: String? = null,

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
