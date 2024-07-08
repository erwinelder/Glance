package com.ataglance.walletglance.domain.repositories

import com.ataglance.walletglance.domain.dao.CategoryDao
import com.ataglance.walletglance.domain.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
    private val dao: CategoryDao
) {

    suspend fun upsertCategories(categoryList: List<CategoryEntity>) {
        dao.upsertCategories(categoryList)
    }

    suspend fun deleteAllCategories() {
        dao.deleteAllCategories()
    }

    suspend fun deleteAndUpsertCategories(
        idListToDelete: List<Int>,
        categoryListToUpsert: List<CategoryEntity>
    ) {
        dao.deleteAndUpsertCategories(idListToDelete, categoryListToUpsert)
    }

    fun getCategories(): Flow<List<CategoryEntity>> {
        return dao.getAllCategories()
    }

}