package com.project.rekapatrol.data.response

data class ResetPasswordResult(
    val success: Boolean,
    val message: String,
    val data: ResetPasswordResponse? = null
)
