package com.ataglance.walletglance.core.mapper

import com.ataglance.walletglance.auth.data.model.UserRemotePreferences
import com.ataglance.walletglance.billing.domain.model.AppSubscription

fun UserRemotePreferences.toMap(): Map<String, Any?> {
    return mapOf(
        "language" to language,
        "subscription" to subscription.name
    )
}

fun Map<String, Any?>.toUserRemotePreferences(userId: String): UserRemotePreferences {
    val subscriptionString = this["subscription"] as String
    val subscription = AppSubscription.entries.find { it.name == subscriptionString }
        ?: AppSubscription.Free

    return UserRemotePreferences(
        userId = userId,
        language = this["language"] as String,
        subscription = subscription
    )
}
