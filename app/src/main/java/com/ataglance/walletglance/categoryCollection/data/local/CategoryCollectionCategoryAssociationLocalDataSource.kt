package com.ataglance.walletglance.categoryCollection.data.local

import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.core.data.local.source.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName

class CategoryCollectionCategoryAssociationLocalDataSource(
    categoryCollectionCategoryAssociationDao: CategoryCollectionCategoryAssociationDao,
    updateTimeDao: LocalUpdateTimeDao
) : BaseLocalDataSource<CategoryCollectionCategoryAssociation>(
    dao = categoryCollectionCategoryAssociationDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.CategoryCollectionCategoryAssociation
)