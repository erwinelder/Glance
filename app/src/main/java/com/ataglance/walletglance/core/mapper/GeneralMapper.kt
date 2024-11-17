package com.ataglance.walletglance.core.mapper

import com.ataglance.walletglance.auth.domain.model.AppSubscription
import com.ataglance.walletglance.core.data.model.UserRemotePreferences

fun UserRemotePreferences.toMap(): Map<String, Any?> {
    return mapOf(
        "language" to language,
        "subscription" to subscription.name
    )
}

fun Map<String, Any?>.toUserRemotePreferences(): UserRemotePreferences {
    return UserRemotePreferences(
        language = this["language"] as String,
        subscription = this["subscription"] as AppSubscription
    )
}
