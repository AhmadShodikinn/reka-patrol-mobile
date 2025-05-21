package com.project.rekapatrol.data.response

import com.google.gson.annotations.SerializedName

data class ListDocumentResponse(

	@field:SerializedName("data")
	val data: List<DataItemDocuments?>? = null,

	@field:SerializedName("meta")
	val meta: MetaDocuments? = null,

	@field:SerializedName("links")
	val links: LinksDocumentsDocuments? = null
)

data class UserDocuments(

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

data class LinksDocumentsDocuments(

	@field:SerializedName("next")
	val next: Any? = null,

	@field:SerializedName("last")
	val last: String? = null,

	@field:SerializedName("prev")
	val prev: Any? = null,

	@field:SerializedName("first")
	val first: String? = null
)

data class DataItemDocuments(

	@field:SerializedName("file_path")
	val filePath: String? = null,

	@field:SerializedName("file_url")
	val fileUrl: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("file_name")
	val fileName: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class LinksItemDocuments(

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("label")
	val label: String? = null,

	@field:SerializedName("url")
	val url: Any? = null
)

data class MetaDocuments(

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
	val links: List<LinksItemDocuments?>? = null,

	@field:SerializedName("to")
	val to: Int? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
)
