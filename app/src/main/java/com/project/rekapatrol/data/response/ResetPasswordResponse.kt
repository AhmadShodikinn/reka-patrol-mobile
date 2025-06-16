package com.project.rekapatrol.data.response

import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: UserResetPassword
)

data class UserResetPassword(

	@field:SerializedName("nip")
	val nip: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("position")
	val position: PositionResetPassword,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("position_id")
	val positionId: Int
)

data class PositionResetPassword(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("position_name")
	val positionName: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int
)
