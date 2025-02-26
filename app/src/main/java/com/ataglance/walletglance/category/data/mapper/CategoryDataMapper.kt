package com.ataglance.walletglance.category.data.mapper

import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.category.data.remote.model.CategoryRemoteEntity
import com.ataglance.walletglance.core.utils.convertToCharOrNull
import com.ataglance.walletglance.core.utils.convertToIntOrNull
import com.ataglance.walletglance.core.utils.convertToIntOrZero


fun CategoryEntity.toRemoteEntity(updateTime: Long, deleted: Boolean): CategoryRemoteEntity {
    return CategoryRemoteEntity(
        updateTime = updateTime,
        deleted = deleted,
        id = id,
        type = type,
        orderNum = orderNum,
        parentCategoryId = parentCategoryId,
        name = name,
        iconName = iconName,
        colorName = colorName
    )
}

fun CategoryRemoteEntity.toLocalEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        type = type,
        orderNum = orderNum,
        parentCategoryId = parentCategoryId,
        name = name,
        iconName = iconName,
        colorName = colorName
    )
}


fun CategoryRemoteEntity.toMap(): HashMap<String, Any?> {
    return hashMapOf(
        "updateTime" to updateTime,
        "deleted" to deleted,
        "id" to id,
        "type" to type.toString(),
        "orderNum" to orderNum,
        "parentCategoryId" to parentCategoryId,
        "name" to name,
        "iconName" to iconName,
        "colorName" to colorName
    )
}

fun Map<String, Any?>.toCategoryRemoteEntity(): CategoryRemoteEntity {
    return CategoryRemoteEntity(
        updateTime = this["updateTime"] as Long,
        deleted = this["deleted"] as Boolean,
        id = this["id"].convertToIntOrZero(),
        type = this["type"]?.convertToCharOrNull() ?: '?',
        orderNum = this["orderNum"].convertToIntOrZero(),
        parentCategoryId = this["parentCategoryId"]?.convertToIntOrNull(),
        name = this["name"] as String,
        iconName = this["iconName"] as String,
        colorName = this["colorName"] as String
    )
}
