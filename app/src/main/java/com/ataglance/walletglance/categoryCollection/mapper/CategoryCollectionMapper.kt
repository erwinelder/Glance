package com.ataglance.walletglance.categoryCollection.mapper

import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociationDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionDataModelWithAssociations
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.domain.utils.asCategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.utils.asChar


fun CategoryCollectionWithIds.toDataModel(): CategoryCollectionDataModel {
    return CategoryCollectionDataModel(
        id = id,
        orderNum = orderNum,
        type = type.asChar(),
        name = name
    )
}

fun CategoryCollectionWithIds.toDataModelWithAssociations(
): CategoryCollectionDataModelWithAssociations? {
    val categoryIds = categoryIds ?: return null

    return CategoryCollectionDataModelWithAssociations(
        collection = toDataModel(),
        associations = categoryIds.map { categoryId ->
            CategoryCollectionCategoryAssociationDataModel(
                categoryCollectionId = id, categoryId = categoryId
            )
        }
    )
}

fun CategoryCollectionWithCategories.toDataModelWithAssociations(
): CategoryCollectionDataModelWithAssociations? {
    return toCollectionWithIds().toDataModelWithAssociations()
}


fun CategoryCollectionDataModelWithAssociations.toDomainModelWithIds(): CategoryCollectionWithIds {
    return CategoryCollectionWithIds(
        id = collection.id,
        orderNum = collection.orderNum,
        type = collection.type.asCategoryCollectionType(),
        name = collection.name,
        categoryIds = associations.map { it.categoryId }
    )
}

fun List<CategoryCollectionDataModelWithAssociations>.groupByType(
): CategoryCollectionsWithIdsByType {
    return CategoryCollectionsWithIdsByType.fromCollections(
        collections = map { it.toDomainModelWithIds() }
    )
}
