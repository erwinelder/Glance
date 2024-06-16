package com.ataglance.walletglance.domain.repositories

import com.ataglance.walletglance.domain.dao.CategoryCollectionDao

class CategoryCollectionRepository(
    private val dao: CategoryCollectionDao
) {

    suspend fun deleteAllCollections() {
        dao.deleteAllCollections()
    }

}