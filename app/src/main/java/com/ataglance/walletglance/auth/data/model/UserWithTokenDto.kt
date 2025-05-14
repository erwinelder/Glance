package com.ataglance.walletglance.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserWithTokenDto(
    val id: Int = 0,
    val email: String,
    val role: UserRoleDto,
    val name: String,
    val langCode: String,
    val subscription: String,
    val token: String
)
