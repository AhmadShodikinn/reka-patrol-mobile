package com.project.rekapatrol.data.response

import com.google.gson.annotations.SerializedName

data class DetailSafetyPatrolResponse(

	@field:SerializedName("data")
	val data: DataDetailSafetyPatrol? = null
)

data class DataDetailSafetyPatrol(

	@field:SerializedName("checkup_date")
	val checkupDate: String? = null,

	@field:SerializedName("pic_id")
	val picId: Int? = null,

	@field:SerializedName("findings")
	val findings: List<Any?>? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("action_path")
	val actionPath: String? = null,

	@field:SerializedName("pic")
	val pic: PicDetailSafetyPatrol? = null,

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

	@field:SerializedName("worker")
	val worker: WorkerDetailSafetyPatrol? = null,

	@field:SerializedName("category")
	val category: String? = null
)

data class PicDetailSafetyPatrol(

	@field:SerializedName("nip")
	val nip: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("position_id")
	val positionId: Int? = null
)

data class WorkerDetailSafetyPatrol(

	@field:SerializedName("nip")
	val nip: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("position_id")
	val positionId: Int? = null
)
