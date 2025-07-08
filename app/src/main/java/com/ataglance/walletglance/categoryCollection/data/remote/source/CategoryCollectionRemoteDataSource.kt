package com.ataglance.walletglance.categoryCollection.data.remote.source

import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionDtoWithAssociations

interface CategoryCollectionRemoteDataSource {

    suspend fun getUpdateTime(userId: Int): Long?

    suspend fun synchronizeCollectionsWithAssociations(
        collections: List<CategoryCollectionDtoWithAssociations>,
        timestamp: Long,
        userId: Int
    ): List<CategoryCollectionDtoWithAssociations>?

    suspend fun synchronizeCollectionsWithAssociationsAndGetAfterTimestamp(
        collections: List<CategoryCollectionDtoWithAssociations>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<CategoryCollectionDtoWithAssociations>?

    suspend fun getCollectionsWithAssociationsAfterTimestamp(
        timestamp: Long,
        userId: Int
    ): List<CategoryCollectionDtoWithAssociations>?

}