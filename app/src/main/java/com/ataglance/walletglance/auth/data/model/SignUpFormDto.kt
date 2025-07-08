package com.ataglance.walletglance.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpFormDto(
    val name: String,
    val email: String,
    val password: String,
    val langCode: String
)
