package com.ataglance.walletglance.data.repository

import com.ataglance.walletglance.data.local.dao.CategoryCollectionDao

class CategoryCollectionRepository(
    private val dao: CategoryCollectionDao
) {

    suspend fun deleteAllCollections() {
        dao.deleteAllCollections()
    }

}