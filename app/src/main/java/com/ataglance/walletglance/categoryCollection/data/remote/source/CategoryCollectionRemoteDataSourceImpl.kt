package com.ataglance.walletglance.categoryCollection.data.remote.source

import com.ataglance.walletglance.categoryCollection.data.remote.dao.CategoryCollectionRemoteDao
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionCategoryRemoteAssociation
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao

class CategoryCollectionRemoteDataSourceImpl(
    private val categoryCollectionDao: CategoryCollectionRemoteDao,
    private val updateTimeDao: RemoteUpdateTimeDao
) : CategoryCollectionRemoteDataSource {

    override suspend fun getCategoryCollectionUpdateTime(userId: String): Long? {
        return updateTimeDao.getUpdateTime(
            tableName = TableName.CategoryCollection.name, userId = userId
        )
    }

    override suspend fun saveCategoryCollectionUpdateTime(timestamp: Long, userId: String) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.CategoryCollection.name, timestamp = timestamp, userId = userId
        )
    }

    override suspend fun getCategoryCollectionsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<CategoryCollectionRemoteEntity> {
        return categoryCollectionDao.getCollectionsAfterTimestamp(
            timestamp = timestamp, userId = userId
        )
    }


    override suspend fun getCollectionCategoryAssociationUpdateTime(userId: String): Long? {
        return updateTimeDao.getUpdateTime(
            tableName = TableName.CategoryCollectionCategoryAssociation.name, userId = userId
        )
    }

    override suspend fun saveCollectionCategoryAssociationUpdateTime(
        timestamp: Long,
        userId: String
    ) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.CategoryCollectionCategoryAssociation.name,
            timestamp = timestamp,
            userId = userId
        )
    }

    override suspend fun getCollectionCategoryAssociationsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<CategoryCollectionCategoryRemoteAssociation> {
        return categoryCollectionDao.getCollectionCategoryAssociationsAfterTimestamp(
            timestamp = timestamp, userId = userId
        )
    }


    override suspend fun upsertCollectionsAndAssociations(
        collections: List<CategoryCollectionRemoteEntity>,
        associations: List<CategoryCollectionCategoryRemoteAssociation>,
        timestamp: Long,
        userId: String
    ) {
        categoryCollectionDao.upsertCollectionsAndAssociations(
            collections = collections, associations = associations, userId = userId
        )
        saveCategoryCollectionUpdateTime(timestamp = timestamp, userId = userId)
        saveCollectionCategoryAssociationUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun synchroniseCollectionsAndAssociations(
        collectionsToSync: EntitiesToSync<CategoryCollectionRemoteEntity>,
        associationsToSync: EntitiesToSync<CategoryCollectionCategoryRemoteAssociation>,
        timestamp: Long,
        userId: String
    ) {
        categoryCollectionDao.synchroniseCollectionsAndAssociations(
            collectionsToSync = collectionsToSync,
            associationsToSync = associationsToSync,
            userId = userId
        )
        saveCategoryCollectionUpdateTime(timestamp = timestamp, userId = userId)
        saveCollectionCategoryAssociationUpdateTime(timestamp = timestamp, userId = userId)
    }

}