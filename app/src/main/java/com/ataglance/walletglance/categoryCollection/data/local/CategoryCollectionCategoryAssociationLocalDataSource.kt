package com.ataglance.walletglance.categoryCollection.data.local

import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow

class CategoryCollectionCategoryAssociationLocalDataSource(
    private val categoryCollectionCategoryAssociationDao: CategoryCollectionCategoryAssociationDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource<CategoryCollectionCategoryAssociation>(
    dao = categoryCollectionCategoryAssociationDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.CategoryCollectionCategoryAssociation
) {

    fun getAllAssociations(): Flow<List<CategoryCollectionCategoryAssociation>> =
        categoryCollectionCategoryAssociationDao.getAllAssociations()

}