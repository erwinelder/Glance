package com.ataglance.walletglance.categoryCollection.data.repository

import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import kotlinx.coroutines.flow.Flow

interface CategoryCollectionRepository {

    suspend fun deleteAndUpsertCollectionsAndAssociations(
        collectionsToDelete: List<CategoryCollectionEntity>,
        collectionsToUpsert: List<CategoryCollectionEntity>,
        associationsToDelete: List<CategoryCollectionCategoryAssociation>,
        associationsToUpsert: List<CategoryCollectionCategoryAssociation>
    )

    suspend fun deleteAllCategoryCollectionsLocally()

    fun getAllCollectionsAndAssociationsFlow(
    ): Flow<Pair<List<CategoryCollectionEntity>, List<CategoryCollectionCategoryAssociation>>>

    suspend fun getAllCollectionsAndAssociations(
    ): Pair<List<CategoryCollectionEntity>, List<CategoryCollectionCategoryAssociation>>

}