package com.project.rekapatrol.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class Position(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("position_name")
	val positionName: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class User(

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

	@field:SerializedName("position")
	val position: Position? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("position_id")
	val positionId: Int? = null
)
