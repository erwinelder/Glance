package com.ataglance.walletglance.core.data.remote.mapper

import com.ataglance.walletglance.core.data.remote.model.RemoteUpdateTime

fun RemoteUpdateTime.toMap(): Map<String, Any> {
    return mapOf(
        "tableName" to tableName,
        "timestamp" to timestamp
    )
}