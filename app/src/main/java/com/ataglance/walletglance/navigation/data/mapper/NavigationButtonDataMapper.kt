package com.ataglance.walletglance.navigation.data.mapper

import com.ataglance.walletglance.core.utils.convertToIntOrZero
import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import com.ataglance.walletglance.navigation.data.remote.model.NavigationButtonRemoteEntity


fun NavigationButtonEntity.toRemoteEntity(
    updateTime: Long,
    deleted: Boolean
): NavigationButtonRemoteEntity {
    return NavigationButtonRemoteEntity(
        updateTime = updateTime,
        deleted = deleted,
        screenName = screenName,
        orderNum = orderNum
    )
}

fun NavigationButtonRemoteEntity.toLocalEntity(): NavigationButtonEntity {
    return NavigationButtonEntity(
        screenName = screenName,
        orderNum = orderNum
    )
}


fun NavigationButtonRemoteEntity.toMap(): HashMap<String, Any> {
    return hashMapOf(
        "updateTime" to updateTime,
        "deleted" to deleted,
        "screenName" to screenName,
        "orderNum" to orderNum
    )
}

fun Map<String, Any?>.toNavigationButtonRemoteEntity(): NavigationButtonRemoteEntity {
    return NavigationButtonRemoteEntity(
        updateTime = get("updateTime") as Long,
        deleted = get("deleted") as Boolean,
        screenName = get("screenName") as String,
        orderNum = get("orderNum").convertToIntOrZero()
    )
}