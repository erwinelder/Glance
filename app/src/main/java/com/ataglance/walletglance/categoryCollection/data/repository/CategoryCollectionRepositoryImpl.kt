package com.ataglance.walletglance.categoryCollection.data.repository

import com.ataglance.walletglance.categoryCollection.data.local.CategoryCollectionLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.remote.CategoryCollectionRemoteDataSource
import com.ataglance.walletglance.core.utils.getNowDateTimeLong

class CategoryCollectionRepositoryImpl(
    override val localSource: CategoryCollectionLocalDataSource,
    override val remoteSource: CategoryCollectionRemoteDataSource?
) : CategoryCollectionRepository {

    override suspend fun deleteAllEntities(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllCollections(timestamp = timestamp)
        remoteSource?.deleteAllEntities(timestamp, onSuccessListener, onFailureListener)
    }

    override suspend fun deleteAllEntitiesLocally() {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllCollections(timestamp = timestamp)
    }

}