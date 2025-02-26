package com.ataglance.walletglance.personalization.data.mapper

import com.ataglance.walletglance.core.utils.convertToIntOrZero
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import com.ataglance.walletglance.personalization.data.remote.model.WidgetRemoteEntity


fun WidgetEntity.toRemoteEntity(updateTime: Long, deleted: Boolean): WidgetRemoteEntity {
    return WidgetRemoteEntity(
        updateTime = updateTime,
        deleted = deleted,
        name = name,
        orderNum = orderNum
    )
}

fun WidgetRemoteEntity.toLocalEntity(): WidgetEntity {
    return WidgetEntity(
        name = name,
        orderNum = orderNum
    )
}


fun WidgetRemoteEntity.toMap(): HashMap<String, Any> {
    return hashMapOf(
        "updateTime" to updateTime,
        "deleted" to deleted,
        "name" to name,
        "orderNum" to orderNum
    )
}

fun Map<String, Any?>.toWidgetRemoteEntity(): WidgetRemoteEntity {
    return WidgetRemoteEntity(
        updateTime = this["updateTime"] as Long,
        deleted = this["deleted"] as Boolean,
        name = this["name"] as String,
        orderNum = this["orderNum"].convertToIntOrZero()
    )
}