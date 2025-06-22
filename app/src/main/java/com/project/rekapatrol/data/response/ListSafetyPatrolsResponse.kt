package com.project.rekapatrol.data.response

import com.google.gson.annotations.SerializedName

data class ListSafetyPatrolsResponse(

	@field:SerializedName("data")
	val data: List<DataItemSafetyPatrols?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null,

	@field:SerializedName("links")
	val links: Links? = null
)

data class LinksItem(

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("label")
	val label: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class Pic(

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

data class Meta(

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("per_page")
	val perPage: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("last_page")
	val lastPage: Int? = null,

	@field:SerializedName("from")
	val from: Int? = null,

	@field:SerializedName("links")
	val links: List<LinksItem?>? = null,

	@field:SerializedName("to")
	val to: Int? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
)

data class Links(

	@field:SerializedName("next")
	val next: String? = null,

	@field:SerializedName("last")
	val last: String? = null,

	@field:SerializedName("prev")
	val prev: String? = null,

	@field:SerializedName("first")
	val first: String? = null
)

data class DataItemSafetyPatrols(

	@field:SerializedName("checkup_date")
	val checkupDate: String? = null,

	@field:SerializedName("pic_id")
	val picId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("action_path")
	val actionPath: String? = null,

	@field:SerializedName("pic")
	val pic: Pic? = null,

	@field:SerializedName("worker_id")
	val workerId: Int? = null,

	@field:SerializedName("action_description")
	val actionDescription: String? = null,

	@field:SerializedName("is_valid_entry")
	val isValidEntry: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("risk")
	val risk: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("has_memo")
	val hasMemo: Boolean? = null,

	@field:SerializedName("findings_description")
	val findingsDescription: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)
