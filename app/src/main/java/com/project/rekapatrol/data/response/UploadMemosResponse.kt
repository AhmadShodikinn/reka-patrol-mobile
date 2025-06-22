package com.project.rekapatrol.data.response

import com.google.gson.annotations.SerializedName

data class UploadMemosResponse(

	@field:SerializedName("data")
	val data: DataMemosResponse? = null
)

data class DataMemosResponse(

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
	val user: UserMemosResponse? = null
)

data class UserMemosResponse(

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
