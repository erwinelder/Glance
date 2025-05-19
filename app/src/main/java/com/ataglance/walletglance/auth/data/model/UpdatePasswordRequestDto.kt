package com.ataglance.walletglance.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordRequestDto(
    val password: String,
    val newPassword: String
)
