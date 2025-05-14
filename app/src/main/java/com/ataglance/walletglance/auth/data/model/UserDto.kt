package com.ataglance.walletglance.auth.data.model

import com.ataglance.walletglance.billing.data.model.AppSubscriptionDto
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int = 0,
    val email: String,
    val role: UserRoleDto,
    val name: String,
    val langCode: String,
    val subscription: AppSubscriptionDto
)
