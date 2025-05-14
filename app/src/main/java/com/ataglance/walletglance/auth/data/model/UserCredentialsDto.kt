package com.ataglance.walletglance.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentialsDto(
    val email: String,
    val password: String
)