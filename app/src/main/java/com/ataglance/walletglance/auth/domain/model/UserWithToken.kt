package com.ataglance.walletglance.auth.domain.model

import com.ataglance.walletglance.billing.domain.model.AppSubscription
import com.ataglance.walletglance.core.domain.app.AppLanguage

data class UserWithToken(
    val id: Int = 0,
    val email: String,
    val role: UserRole,
    val name: String,
    val langCode: AppLanguage,
    val subscription: AppSubscription,
    val token: String
)
