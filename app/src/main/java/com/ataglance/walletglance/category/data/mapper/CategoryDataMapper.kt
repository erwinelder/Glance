package com.ataglance.walletglance.category.data.mapper

import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.category.data.model.CategoryDataModel
import com.ataglance.walletglance.category.data.remote.model.CategoryCommandDto
import com.ataglance.walletglance.category.data.remote.model.CategoryQueryDto


fun CategoryDataModel.toEntity(timestamp: Long, deleted: Boolean): CategoryEntity {
    return CategoryEntity(
        id = id,
        type = type,
        orderNum = orderNum,
        parentCategoryId = parentCategoryId,
        name = name,
        iconName = iconName,
        colorName = colorName,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun CategoryEntity.toDataModel(): CategoryDataModel {
    return CategoryDataModel(
        id = id,
        type = type,
        orderNum = orderNum,
        parentCategoryId = parentCategoryId,
        name = name,
        iconName = iconName,
        colorName = colorName
    )
}

fun CategoryDataModel.toCommandDto(timestamp: Long, deleted: Boolean): CategoryCommandDto {
    return CategoryCommandDto(
        id = id,
        type = type,
        orderNum = orderNum,
        parentCategoryId = parentCategoryId,
        name = name,
        iconName = iconName,
        colorName = colorName,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun CategoryEntity.toCommandDto(): CategoryCommandDto {
    return CategoryCommandDto(
        id = id,
        type = type,
        orderNum = orderNum,
        parentCategoryId = parentCategoryId,
        name = name,
        iconName = iconName,
        colorName = colorName,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun CategoryQueryDto.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        type = type,
        orderNum = orderNum,
        parentCategoryId = parentCategoryId,
        name = name,
        iconName = iconName,
        colorName = colorName,
        timestamp = timestamp,
        deleted = deleted
    )
}
