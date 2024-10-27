package com.ataglance.walletglance.categoryCollection.data.repository

import com.ataglance.walletglance.categoryCollection.data.local.CategoryCollectionDao

class CategoryCollectionRepository(
    private val dao: CategoryCollectionDao
) {

    suspend fun deleteAllCollections() {
        dao.deleteAllCollections()
    }

}