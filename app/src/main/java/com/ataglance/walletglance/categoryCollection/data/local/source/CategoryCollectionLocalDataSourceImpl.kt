package com.ataglance.walletglance.categoryCollection.data.local.source

import com.ataglance.walletglance.categoryCollection.data.local.dao.CategoryCollectionLocalDao
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow

class CategoryCollectionLocalDataSourceImpl(
    private val categoryCollectionDao: CategoryCollectionLocalDao,
    private val updateTimeDao: LocalUpdateTimeDao
) : CategoryCollectionLocalDataSource {

    override suspend fun getCategoryCollectionUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.CategoryCollection.name)
    }

    override suspend fun saveCategoryCollectionUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.CategoryCollection.name, timestamp = timestamp
        )
    }

    override suspend fun deleteAllCategoryCollections(timestamp: Long) {
        categoryCollectionDao.deleteAllCategoryCollections()
        saveCategoryCollectionUpdateTime(timestamp = timestamp)
    }

    override suspend fun synchroniseCategoryCollections(
        collectionsToSync: EntitiesToSync<CategoryCollectionEntity>,
        timestamp: Long
    ) {
        categoryCollectionDao.deleteAndUpsertCollections(
            toDelete = collectionsToSync.toDelete,
            toUpsert = collectionsToSync.toUpsert
        )
        saveCategoryCollectionUpdateTime(timestamp = timestamp)
    }

    override fun getAllCategoryCollectionsFlow(): Flow<List<CategoryCollectionEntity>> {
        return categoryCollectionDao.getAllCollectionsFlow()
    }

    override suspend fun getAllCategoryCollections(): List<CategoryCollectionEntity> {
        return categoryCollectionDao.getAllCollections()
    }


    override suspend fun getCollectionCategoryAssociationUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(
            tableName = TableName.CategoryCollectionCategoryAssociation.name
        )
    }

    override suspend fun saveCollectionCategoryAssociationUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.CategoryCollectionCategoryAssociation.name, timestamp = timestamp
        )
    }

    override suspend fun synchroniseCollectionCategoryAssociations(
        associationsToSync: EntitiesToSync<CategoryCollectionCategoryAssociation>,
        timestamp: Long
    ) {
        categoryCollectionDao.deleteAndUpsertCollectionCategoryAssociations(
            toDelete = associationsToSync.toDelete,
            toUpsert = associationsToSync.toUpsert
        )
        saveCollectionCategoryAssociationUpdateTime(timestamp = timestamp)
    }

    override fun getAllCollectionCategoryAssociationsFlow(
    ): Flow<List<CategoryCollectionCategoryAssociation>> {
        return categoryCollectionDao.getAllCollectionCategoryAssociationsFlow()
    }

    override suspend fun getAllCollectionCategoryAssociations(
    ): List<CategoryCollectionCategoryAssociation> {
        return categoryCollectionDao.getAllCollectionCategoryAssociations()
    }


    override suspend fun deleteCollectionsAndAssociations(
        collections: List<CategoryCollectionEntity>,
        associations: List<CategoryCollectionCategoryAssociation>,
        timestamp: Long
    ) {
        categoryCollectionDao.deleteCollectionCategoryAssociations(associations = associations)
        saveCollectionCategoryAssociationUpdateTime(timestamp = timestamp)
        categoryCollectionDao.deleteCollections(collections = collections)
        saveCategoryCollectionUpdateTime(timestamp = timestamp)
    }

    override suspend fun synchroniseCollectionsAndAssociations(
        collectionsToSync: EntitiesToSync<CategoryCollectionEntity>,
        associationsToSync: EntitiesToSync<CategoryCollectionCategoryAssociation>,
        timestamp: Long
    ) {
        categoryCollectionDao.deleteAndUpsertCollectionsAndAssociations(
            collectionsToDelete = collectionsToSync.toDelete,
            collectionsToUpsert = collectionsToSync.toUpsert,
            associationsToDelete = associationsToSync.toDelete,
            associationsToUpsert = associationsToSync.toUpsert
        )
        saveCategoryCollectionUpdateTime(timestamp = timestamp)
        saveCollectionCategoryAssociationUpdateTime(timestamp = timestamp)
    }

}

fun getCategoryCollectionLocalDataSource(appDatabase: AppDatabase): CategoryCollectionLocalDataSource {
    return CategoryCollectionLocalDataSourceImpl(
        categoryCollectionDao = appDatabase.categoryCollectionDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}