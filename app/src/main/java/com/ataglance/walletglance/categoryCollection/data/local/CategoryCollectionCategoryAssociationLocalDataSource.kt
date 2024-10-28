package com.ataglance.walletglance.categoryCollection.data.local

import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName

class CategoryCollectionCategoryAssociationLocalDataSource(
    categoryCollectionCategoryAssociationDao: CategoryCollectionCategoryAssociationDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource<CategoryCollectionCategoryAssociation>(
    dao = categoryCollectionCategoryAssociationDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.CategoryCollectionCategoryAssociation
)