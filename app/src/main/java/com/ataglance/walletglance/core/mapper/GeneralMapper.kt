package com.ataglance.walletglance.core.mapper

import com.ataglance.walletglance.billing.domain.model.AppSubscription
import com.ataglance.walletglance.auth.data.model.UserRemotePreferences

fun UserRemotePreferences.toMap(): Map<String, Any?> {
    return mapOf(
        "language" to language,
        "subscription" to subscription.name
    )
}

fun Map<String, Any?>.toUserRemotePreferences(userId: String): UserRemotePreferences {
    return UserRemotePreferences(
        userId = userId,
        language = this["language"] as String,
        subscription = this["subscription"] as AppSubscription
    )
}
