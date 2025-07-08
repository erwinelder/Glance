package com.ataglance.walletglance.personalization.data.mapper

import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import com.ataglance.walletglance.personalization.data.model.WidgetDataModel
import com.ataglance.walletglance.personalization.data.remote.model.WidgetDto


fun WidgetDataModel.toEntity(timestamp: Long, deleted: Boolean): WidgetEntity {
    return WidgetEntity(
        name = name,
        orderNum = orderNum,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun WidgetEntity.toDataModel(): WidgetDataModel {
    return WidgetDataModel(
        name = name,
        orderNum = orderNum
    )
}

fun WidgetDataModel.toDto(timestamp: Long, deleted: Boolean): WidgetDto {
    return WidgetDto(
        name = name,
        orderNum = orderNum,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun WidgetEntity.toDto(): WidgetDto {
    return WidgetDto(
        name = name,
        orderNum = orderNum,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun WidgetDto.toEntity(): WidgetEntity {
    return WidgetEntity(
        name = name,
        orderNum = orderNum,
        timestamp = timestamp,
        deleted = deleted
    )
}
