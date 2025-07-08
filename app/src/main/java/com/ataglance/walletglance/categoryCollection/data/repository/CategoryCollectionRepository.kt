package com.ataglance.walletglance.categoryCollection.data.repository

import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionDataModelWithAssociations
import kotlinx.coroutines.flow.Flow

interface CategoryCollectionRepository {

    suspend fun deleteAllCollectionsLocally()

    suspend fun deleteAndUpsertCollectionsWithAssociations(
        toDelete: List<CategoryCollectionDataModel>,
        toUpsert: List<CategoryCollectionDataModelWithAssociations>
    )

    suspend fun getAllCollections(): List<CategoryCollectionDataModel>

    fun getAllCollectionsWithAssociationsAsFlow(
    ): Flow<List<CategoryCollectionDataModelWithAssociations>>

    suspend fun getAllCollectionsWithAssociations(
    ): List<CategoryCollectionDataModelWithAssociations>

}