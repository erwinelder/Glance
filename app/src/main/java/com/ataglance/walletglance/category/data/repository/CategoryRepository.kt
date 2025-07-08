package com.ataglance.walletglance.category.data.repository

import com.ataglance.walletglance.category.data.model.CategoryDataModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun upsertCategories(categories: List<CategoryDataModel>)

    suspend fun deleteAndUpsertCategories(
        toDelete: List<CategoryDataModel>,
        toUpsert: List<CategoryDataModel>
    )

    suspend fun deleteAllCategoriesLocally()

    fun getAllCategoriesAsFlow(): Flow<List<CategoryDataModel>>

    suspend fun getAllCategories(): List<CategoryDataModel>

    suspend fun getCategoriesByType(type: Char): List<CategoryDataModel>

}