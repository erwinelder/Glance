package com.ataglance.walletglance.categoryCollection.data.remote.source

import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionCategoryRemoteAssociation
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync

interface CategoryCollectionRemoteDataSource {

    suspend fun getCategoryCollectionUpdateTime(userId: String): Long?

    suspend fun saveCategoryCollectionUpdateTime(timestamp: Long, userId: String)

    suspend fun getCategoryCollectionsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<CategoryCollectionRemoteEntity>


    suspend fun getCollectionCategoryAssociationUpdateTime(userId: String): Long?

    suspend fun saveCollectionCategoryAssociationUpdateTime(timestamp: Long, userId: String)

    suspend fun getCollectionCategoryAssociationsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<CategoryCollectionCategoryRemoteAssociation>


    suspend fun synchroniseCollectionsAndAssociations(
        collectionsToSync: EntitiesToSync<CategoryCollectionRemoteEntity>,
        associationsToSync: EntitiesToSync<CategoryCollectionCategoryRemoteAssociation>,
        timestamp: Long,
        userId: String
    )

}