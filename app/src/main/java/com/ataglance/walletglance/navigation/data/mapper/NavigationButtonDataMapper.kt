package com.ataglance.walletglance.navigation.data.mapper

import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import com.ataglance.walletglance.navigation.data.model.NavigationButtonDataModel
import com.ataglance.walletglance.navigation.data.remote.model.NavigationButtonDto


fun NavigationButtonDataModel.toEntity(timestamp: Long, deleted: Boolean): NavigationButtonEntity {
    return NavigationButtonEntity(
        screenName = screenName,
        orderNum = orderNum,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun NavigationButtonEntity.toDataModel(): NavigationButtonDataModel {
    return NavigationButtonDataModel(
        screenName = screenName,
        orderNum = orderNum
    )
}

fun NavigationButtonDataModel.toDto(timestamp: Long, deleted: Boolean): NavigationButtonDto {
    return NavigationButtonDto(
        screenName = screenName,
        orderNum = orderNum,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun NavigationButtonEntity.toDto(): NavigationButtonDto {
    return NavigationButtonDto(
        screenName = screenName,
        orderNum = orderNum,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun NavigationButtonDto.toEntity(): NavigationButtonEntity {
    return NavigationButtonEntity(
        screenName = screenName,
        orderNum = orderNum,
        timestamp = timestamp,
        deleted = deleted
    )
}
