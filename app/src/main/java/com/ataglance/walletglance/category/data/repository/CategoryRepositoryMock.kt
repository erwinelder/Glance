package com.ataglance.walletglance.category.data.repository

import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.category.mapper.toDataModel
import com.ataglance.walletglance.category.presentation.model.DefaultCategoriesPackage
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepositoryMock(
    resourceManager: ResourceManager
) : CategoryRepository {

    var categories = DefaultCategoriesPackage(resourceManager = resourceManager)
        .getAsList()
        .map { it.toDataModel() }


    override suspend fun upsertCategories(categories: List<CategoryEntity>) {}

    override suspend fun deleteAndUpsertCategories(
        toDelete: List<CategoryEntity>,
        toUpsert: List<CategoryEntity>
    ) {}

    override suspend fun deleteAllCategoriesLocally() {}

    override fun getAllCategoriesFlow(): Flow<List<CategoryEntity>> = flow {
        emit(categories)
    }

    override suspend fun getAllCategories(): List<CategoryEntity> {
        return categories
    }

    override suspend fun getCategoriesByType(type: Char): List<CategoryEntity> {
        return categories.filter { it.type == type }
    }

}