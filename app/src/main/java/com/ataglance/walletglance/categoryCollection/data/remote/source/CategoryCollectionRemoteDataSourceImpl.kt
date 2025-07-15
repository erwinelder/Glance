package com.ataglance.walletglance.categoryCollection.data.remote.source

import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionDtoWithAssociations

class CategoryCollectionRemoteDataSourceImpl() : CategoryCollectionRemoteDataSource {

    override suspend fun getUpdateTime(userId: Int): Long? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeCollectionsWithAssociations(
        collections: List<CategoryCollectionDtoWithAssociations>,
        timestamp: Long,
        userId: Int
    ): Boolean {
        // TODO("Not yet implemented")
        return false
    }

    override suspend fun synchronizeCollectionsWithAssociationsAndGetAfterTimestamp(
        collections: List<CategoryCollectionDtoWithAssociations>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<CategoryCollectionDtoWithAssociations>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun getCollectionsWithAssociationsAfterTimestamp(
        timestamp: Long,
        userId: Int
    ): List<CategoryCollectionDtoWithAssociations>? {
        // TODO("Not yet implemented")
        return null
    }

}