package com.project.rekapatrol.data.response

import com.google.gson.annotations.SerializedName

data class UpdateSafetyMemoResponse(

	@field:SerializedName("data")
	val data: DataUpdateSafety? = null
)

data class MemoUpdateSafety(

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
	val type: String? = null
)

data class DataUpdateSafety(

	@field:SerializedName("checkup_date")
	val checkupDate: String? = null,

	@field:SerializedName("pic_id")
	val picId: Int? = null,

	@field:SerializedName("findings")
	val findings: List<Any?>? = null,

	@field:SerializedName("memo")
	val memo: MemoUpdateSafety? = null,

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
	val category: String? = null,

	@field:SerializedName("is_valid_entry")
	val isValidEntry: Any? = null,

	@field:SerializedName("has_memo")
	val hasMemo: Boolean? = null
)
