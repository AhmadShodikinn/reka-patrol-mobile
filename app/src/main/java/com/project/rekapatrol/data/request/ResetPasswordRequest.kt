package com.project.rekapatrol.data.request

data class ResetPasswordRequest(
    val nip: String,
    val password: String,
    val password_confirmation: String
)
