package com.ataglance.walletglance.category.data.repository

import com.ataglance.walletglance.category.data.local.CategoryLocalDataSource
import com.ataglance.walletglance.category.data.remote.CategoryRemoteDataSource
import com.ataglance.walletglance.core.utils.getNowDateTimeLong

class CategoryRepositoryImpl(
    override val localSource: CategoryLocalDataSource,
    override val remoteSource: CategoryRemoteDataSource?
) : CategoryRepository {

    override suspend fun deleteAllEntities() {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllCategories(timestamp = timestamp)
        remoteSource?.deleteAllEntities(timestamp = timestamp)
    }

    override suspend fun deleteAllEntitiesLocally() {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllCategories(timestamp = timestamp)
    }

}