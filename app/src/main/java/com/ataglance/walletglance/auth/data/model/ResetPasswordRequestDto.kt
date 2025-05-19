package com.ataglance.walletglance.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequestDto(
    val oobCode: String,
    val newPassword: String
)
