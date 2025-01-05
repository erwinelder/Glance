package com.ataglance.walletglance.categoryCollection.data.repository

import com.ataglance.walletglance.categoryCollection.data.local.CategoryCollectionLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.remote.CategoryCollectionRemoteDataSource
import com.ataglance.walletglance.core.utils.getNowDateTimeLong

class CategoryCollectionRepositoryImpl(
    override val localSource: CategoryCollectionLocalDataSource,
    override val remoteSource: CategoryCollectionRemoteDataSource?
) : CategoryCollectionRepository {

    override suspend fun deleteAllEntities() {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllCollections(timestamp = timestamp)
        remoteSource?.deleteAllEntities(timestamp = timestamp)
    }

    override suspend fun deleteAllEntitiesLocally() {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllCollections(timestamp = timestamp)
    }

}