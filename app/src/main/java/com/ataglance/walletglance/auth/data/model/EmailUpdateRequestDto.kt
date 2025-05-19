package com.ataglance.walletglance.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailUpdateRequestDto(
    val password: String,
    val newEmail: String
)
