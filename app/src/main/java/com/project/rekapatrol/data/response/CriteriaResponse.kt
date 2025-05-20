package com.project.rekapatrol.data.response

import com.google.gson.annotations.SerializedName

data class CriteriaResponse(

	@field:SerializedName("data")
	val data: List<DataItemCriterias?>? = null,

	@field:SerializedName("meta")
	val meta: MetaCriterias? = null,

	@field:SerializedName("links")
	val links: LinksItemCriterias? = null
)

data class DataItemCriterias(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("location")
	val location: Location? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("criteria_type")
	val criteriaType: String? = null,

	@field:SerializedName("location_id")
	val locationId: Int? = null,

	@field:SerializedName("criteria_name")
	val criteriaName: String? = null
)

data class Location(

	@field:SerializedName("location_name")
	val locationName: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class LinksItemCriterias(

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("label")
	val label: String? = null,

	@field:SerializedName("url")
	val url: Any? = null
)

data class MetaCriterias(

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

data class LinksCriterias(

	@field:SerializedName("next")
	val next: String? = null,

	@field:SerializedName("last")
	val last: String? = null,

	@field:SerializedName("prev")
	val prev: Any? = null,

	@field:SerializedName("first")
	val first: String? = null
)
