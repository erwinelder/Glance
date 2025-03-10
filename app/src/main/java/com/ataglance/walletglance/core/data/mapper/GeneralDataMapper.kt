package com.ataglance.walletglance.core.data.mapper

import com.ataglance.walletglance.auth.data.model.UserData
import com.ataglance.walletglance.billing.domain.model.AppSubscription
import com.ataglance.walletglance.core.data.remote.model.RemoteUpdateTime
import com.ataglance.walletglance.core.utils.enumValueOrNull


fun RemoteUpdateTime.toRemoteUpdateTimeMap(): Map<String, Any> {
    return mapOf(
        "tableName" to tableName,
        "timestamp" to timestamp
    )
}


fun UserData.toUserDataMap(): Map<String, Any?> {
    return mapOf(
        "language" to language,
        "subscription" to subscription.name
    )
}

fun Map<String, Any?>.toUserData(userId: String): UserData {
    val subscriptionString = this["subscription"] as String
    val subscription = enumValueOrNull<AppSubscription>(subscriptionString) ?: AppSubscription.Free

    return UserData(
        userId = userId,
        language = this["language"] as String,
        subscription = subscription
    )
}
