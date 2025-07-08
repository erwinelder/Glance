package com.ataglance.walletglance.categoryCollection.data.local.source

import com.ataglance.walletglance.categoryCollection.data.local.dao.CategoryCollectionLocalDao
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntityWithAssociations
import com.ataglance.walletglance.categoryCollection.data.utils.divide
import com.ataglance.walletglance.categoryCollection.data.utils.zipWithAssociations
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.utils.excludeItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class CategoryCollectionLocalDataSourceImpl(
    private val categoryCollectionDao: CategoryCollectionLocalDao,
    private val updateTimeDao: LocalUpdateTimeDao
) : CategoryCollectionLocalDataSource {

    override suspend fun getUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.CategoryCollection.name)
    }

    override suspend fun saveUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.CategoryCollection.name, timestamp = timestamp
        )
    }

    override suspend fun deleteUpdateTime() {
        updateTimeDao.deleteUpdateTime(tableName = TableName.CategoryCollection.name)
    }

    override suspend fun deleteAllCategoryCollections() {
        categoryCollectionDao.deleteAllCategoryCollections()
        deleteUpdateTime()
    }

    override suspend fun upsertCollectionsWithAssociations(
        collectionsWithAssociations: List<CategoryCollectionEntityWithAssociations>,
        timestamp: Long
    ) {
        val (collections, associations) = collectionsWithAssociations.divide()

        categoryCollectionDao.upsertCollectionsAndAssociations(
            collections = collections, associations = associations
        )
        updateTimeDao.saveUpdateTime(
            tableName = TableName.CategoryCollection.name, timestamp = timestamp
        )
    }

    override suspend fun deleteCollectionsWithAssociations(
        collectionsWithAssociations: List<CategoryCollectionEntityWithAssociations>
    ) {
        val collections = collectionsWithAssociations.map { it.collection }

        categoryCollectionDao.deleteCollections(collections = collections)
    }

    override suspend fun deleteAndUpsertCollectionsWithAssociations(
        toDelete: List<CategoryCollectionEntityWithAssociations>,
        toUpsert: List<CategoryCollectionEntityWithAssociations>,
        timestamp: Long
    ) {
        val collectionsToDelete = toDelete.map { it.collection }
        val (collectionsToUpsert, associationsToUpsert) = toUpsert.divide()

        val associationsToDelete = categoryCollectionDao
            .getCollectionCategoryAssociations(
                collectionIds = collectionsToUpsert.map { it.id }
            )
            .excludeItems(associationsToUpsert) { it.collectionId to it.categoryId }

        categoryCollectionDao.deleteAndUpsertCollectionsAndAssociations(
            collectionsToDelete = collectionsToDelete,
            collectionsToUpsert = collectionsToUpsert,
            associationsToUpsert = associationsToUpsert,
            associationsToDelete = associationsToDelete
        )
        updateTimeDao.saveUpdateTime(
            tableName = TableName.CategoryCollection.name, timestamp = timestamp
        )
    }

    override suspend fun getAllCollections(): List<CategoryCollectionEntity> {
        return categoryCollectionDao.getAllCollections()
    }

    override suspend fun getCollectionsWithAssociationsAfterTimestamp(
        timestamp: Long
    ): List<CategoryCollectionEntityWithAssociations> {
        val collections = categoryCollectionDao.getCollectionsAfterTimestamp(timestamp = timestamp)
        val ids = collections.filterNot { it.deleted }.map { it.id }
        val associations = categoryCollectionDao.getCollectionCategoryAssociations(
            collectionIds = ids
        )

        return collections.zipWithAssociations(associations = associations)
    }

    override fun getAllCollectionsWithAssociationsAsFlow(
    ): Flow<List<CategoryCollectionEntityWithAssociations>> = combine(
        categoryCollectionDao.getAllCollectionsAsFlow(),
        categoryCollectionDao.getAllCollectionCategoryAssociationsAsFlow()
    ) { collections, associations ->
        collections.zipWithAssociations(associations = associations)
    }

    override suspend fun getAllCollectionsWithAssociations(
    ): List<CategoryCollectionEntityWithAssociations> {
        val collections = categoryCollectionDao.getAllCollections()
        val associations = categoryCollectionDao.getAllCollectionCategoryAssociations()
        return collections.zipWithAssociations(associations = associations)
    }

}

fun getCategoryCollectionLocalDataSource(appDatabase: AppDatabase): CategoryCollectionLocalDataSource {
    return CategoryCollectionLocalDataSourceImpl(
        categoryCollectionDao = appDatabase.categoryCollectionDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}
