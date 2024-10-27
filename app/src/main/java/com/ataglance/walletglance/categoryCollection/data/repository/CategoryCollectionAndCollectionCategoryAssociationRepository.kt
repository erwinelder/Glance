package com.ataglance.walletglance.categoryCollection.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.categoryCollection.data.local.CategoryCollectionCategoryAssociationDao
import com.ataglance.walletglance.categoryCollection.data.local.CategoryCollectionDao
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionEntity

class CategoryCollectionAndCollectionCategoryAssociationRepository(
    private val categoryCollectionDao: CategoryCollectionDao,
    private val categoryCollectionCategoryAssociationDao: CategoryCollectionCategoryAssociationDao
) {

    @Transaction
    suspend fun getCategoryCollectionsAndCollectionCategoryAssociations():
            Pair<List<CategoryCollectionEntity>, List<CategoryCollectionCategoryAssociation>>
    {
        val collections = categoryCollectionDao.getAllCollections()
        val collectionCategoryAssociations =
            categoryCollectionCategoryAssociationDao.getAllAssociations()
        return collections to collectionCategoryAssociations
    }

    @Transaction
    suspend fun deleteAndUpsertCollectionsAndDeleteAndUpsertAssociations(
        collectionsIdsToDelete: List<Int>,
        collectionListToUpsert: List<CategoryCollectionEntity>,
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