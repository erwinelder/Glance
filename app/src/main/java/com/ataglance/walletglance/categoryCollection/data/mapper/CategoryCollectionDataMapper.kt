package com.ataglance.walletglance.categoryCollection.data.mapper

import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionCategoryRemoteAssociation
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionRemoteEntity
import com.ataglance.walletglance.core.utils.convertToCharOrNull
import com.ataglance.walletglance.core.utils.convertToIntOrZero


fun CategoryCollectionEntity.toRemoteEntity(
    updateTime: Long,
    deleted: Boolean
): CategoryCollectionRemoteEntity {
    return CategoryCollectionRemoteEntity(
        updateTime = updateTime,
        deleted = deleted,
        id = id,
        orderNum = orderNum,
        type = type,
        name = name
    )
}

fun CategoryCollectionRemoteEntity.toLocalEntity(): CategoryCollectionEntity {
    return CategoryCollectionEntity(
        id = id,
        orderNum = orderNum,
        type = type,
        name = name
    )
}


fun CategoryCollectionCategoryAssociation.toRemoteAssociation(
    updateTime: Long,
    deleted: Boolean
): CategoryCollectionCategoryRemoteAssociation {
    return CategoryCollectionCategoryRemoteAssociation(
        updateTime = updateTime,
        deleted = deleted,
        categoryCollectionId = categoryCollectionId,
        categoryId = categoryId
    )
}

fun CategoryCollectionCategoryRemoteAssociation.toLocalAssociation():
        CategoryCollectionCategoryAssociation
{
    return CategoryCollectionCategoryAssociation(
        categoryCollectionId = categoryCollectionId,
        categoryId = categoryId
    )
}


fun CategoryCollectionRemoteEntity.toMap(): HashMap<String, Any> {
    return hashMapOf(
        "updateTime" to updateTime,
        "deleted" to deleted,
        "id" to id,
        "orderNum" to orderNum,
        "type" to type,
        "name" to name
    )
}

fun Map<String, Any?>.toCategoryCollectionRemoteEntity(): CategoryCollectionRemoteEntity {
    return CategoryCollectionRemoteEntity(
        updateTime = this["updateTime"] as Long,
        deleted = this["deleted"] as Boolean,
        id = this["id"].convertToIntOrZero(),
        orderNum = this["orderNum"].convertToIntOrZero(),
        type = this["type"]?.convertToCharOrNull() ?: ' ',
        name = this["name"] as String
    )
}


fun CategoryCollectionCategoryRemoteAssociation.toMap(): HashMap<String, Any> {
    return hashMapOf(
        "updateTime" to updateTime,
        "deleted" to deleted,
        "categoryCollectionId" to categoryCollectionId,
        "categoryId" to categoryId
    )
}

fun Map<String, Any?>.toCategoryCollectionCategoryRemoteAssociation():
        CategoryCollectionCategoryRemoteAssociation
{
    return CategoryCollectionCategoryRemoteAssociation(
        updateTime = this["updateTime"] as Long,
        deleted = this["deleted"] as Boolean,
        categoryCollectionId = this["categoryCollectionId"].convertToIntOrZero(),
        categoryId = this["categoryId"].convertToIntOrZero()
    )
}
