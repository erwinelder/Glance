package com.ataglance.walletglance.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.data.local.dao.CategoryCollectionCategoryAssociationDao
import com.ataglance.walletglance.data.local.dao.CategoryCollectionDao
import com.ataglance.walletglance.data.local.entities.CategoryCollection
import com.ataglance.walletglance.data.local.entities.CategoryCollectionCategoryAssociation

class CategoryCollectionAndCollectionCategoryAssociationRepository(
    private val categoryCollectionDao: CategoryCollectionDao,
    private val categoryCollectionCategoryAssociationDao: CategoryCollectionCategoryAssociationDao
) {

    @Transaction
    suspend fun getCategoryCollectionsAndCollectionCategoryAssociations():
            Pair<List<CategoryCollection>, List<CategoryCollectionCategoryAssociation>>
    {
        val collections = categoryCollectionDao.getAllCollections()
        val collectionCategoryAssociations =
            categoryCollectionCategoryAssociationDao.getAllCategoryCollectionCategoryAssociations()
        return collections to collectionCategoryAssociations
    }

    @Transaction
    suspend fun deleteAndUpsertCollectionsAndDeleteAndUpsertAssociations(
        collectionsIdsToDelete: List<Int>,
        collectionListToUpsert: List<CategoryCollection>,
        associationsToDelete: List<CategoryCollectionCategoryAssociation>,
        associationsToUpsert: List<CategoryCollectionCategoryAssociation>
    ) {
        if (collectionsIdsToDelete.isNotEmpty())
            categoryCollectionDao.deleteCollectionsByIds(collectionsIdsToDelete)
        categoryCollectionDao.upsertCollections(collectionListToUpsert)

        if (associationsToDelete.isNotEmpty())
            categoryCollectionCategoryAssociationDao.deleteAssociations(associationsToDelete)
        categoryCollectionCategoryAssociationDao
            .upsertCategoryCollectionCategoryAssociations(associationsToUpsert)
    }

}