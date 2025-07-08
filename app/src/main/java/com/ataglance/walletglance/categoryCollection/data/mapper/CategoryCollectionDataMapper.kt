package com.ataglance.walletglance.categoryCollection.data.mapper

import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociationEntity
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntityWithAssociations
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociationDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionDataModelWithAssociations
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionCategoryAssociationDto
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionDto
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionDtoWithAssociations


fun CategoryCollectionDataModel.withAssociations(
    associations: List<CategoryCollectionCategoryAssociationDataModel> = emptyList()
): CategoryCollectionDataModelWithAssociations {
    return CategoryCollectionDataModelWithAssociations(
        collection = this,
        associations = associations
    )
}


fun CategoryCollectionDataModel.toEntity(
    timestamp: Long,
    deleted: Boolean
): CategoryCollectionEntity {
    return CategoryCollectionEntity(
        id = id,
        orderNum = orderNum,
        type = type,
        name = name,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun CategoryCollectionCategoryAssociationDataModel.toEntity(
): CategoryCollectionCategoryAssociationEntity {
    return CategoryCollectionCategoryAssociationEntity(
        collectionId = categoryCollectionId,
        categoryId = categoryId
    )
}

fun CategoryCollectionDataModelWithAssociations.toEntityWithAssociations(
    timestamp: Long,
    deleted: Boolean
): CategoryCollectionEntityWithAssociations {
    return CategoryCollectionEntityWithAssociations(
        collection = collection.toEntity(timestamp = timestamp, deleted = deleted),
        associations = associations.map { it.toEntity() }
    )
}


fun CategoryCollectionEntity.toDataModel(): CategoryCollectionDataModel {
    return CategoryCollectionDataModel(
        id = id,
        orderNum = orderNum,
        type = type,
        name = name
    )
}

fun CategoryCollectionCategoryAssociationEntity.toDataModel(
): CategoryCollectionCategoryAssociationDataModel {
    return CategoryCollectionCategoryAssociationDataModel(
        categoryCollectionId = collectionId,
        categoryId = categoryId
    )
}

fun CategoryCollectionEntityWithAssociations.toDataModelWithAssociations(
): CategoryCollectionDataModelWithAssociations {
    return CategoryCollectionDataModelWithAssociations(
        collection = collection.toDataModel(),
        associations = associations.map { it.toDataModel() }
    )
}


fun CategoryCollectionDataModel.toDto(timestamp: Long, deleted: Boolean): CategoryCollectionDto {
    return CategoryCollectionDto(
        id = id,
        orderNum = orderNum,
        type = type,
        name = name,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun CategoryCollectionCategoryAssociationDataModel.toDto(
): CategoryCollectionCategoryAssociationDto {
    return CategoryCollectionCategoryAssociationDto(
        categoryCollectionId = categoryCollectionId,
        categoryId = categoryId
    )
}

fun CategoryCollectionDataModelWithAssociations.toDtoWithAssociations(
    timestamp: Long,
    deleted: Boolean
): CategoryCollectionDtoWithAssociations {
    return CategoryCollectionDtoWithAssociations(
        collection = collection.toDto(timestamp = timestamp, deleted = deleted),
        associations = associations.map { it.toDto() }
    )
}


fun CategoryCollectionEntity.toDto(): CategoryCollectionDto {
    return CategoryCollectionDto(
        id = id,
        orderNum = orderNum,
        type = type,
        name = name,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun CategoryCollectionCategoryAssociationEntity.toDto(
): CategoryCollectionCategoryAssociationDto {
    return CategoryCollectionCategoryAssociationDto(
        categoryCollectionId = collectionId,
        categoryId = categoryId
    )
}

fun CategoryCollectionEntityWithAssociations.toDtoWithAssociations(
): CategoryCollectionDtoWithAssociations {
    return CategoryCollectionDtoWithAssociations(
        collection = collection.toDto(),
        associations = associations.map { it.toDto() }
    )
}


fun CategoryCollectionDto.toEntity(): CategoryCollectionEntity {
    return CategoryCollectionEntity(
        id = id,
        orderNum = orderNum,
        type = type,
        name = name,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun CategoryCollectionCategoryAssociationDto.toEntity(
): CategoryCollectionCategoryAssociationEntity {
    return CategoryCollectionCategoryAssociationEntity(
        collectionId = categoryCollectionId,
        categoryId = categoryId
    )
}

fun CategoryCollectionDtoWithAssociations.toEntityWithAssociations(
): CategoryCollectionEntityWithAssociations {
    return CategoryCollectionEntityWithAssociations(
        collection = collection.toEntity(),
        associations = associations.map { it.toEntity() }
    )
}
