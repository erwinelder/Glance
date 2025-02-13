package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategories
import kotlinx.coroutines.flow.Flow

interface GetCategoriesUseCase {

    fun getGroupedAsFlow(): Flow<CategoriesWithSubcategories>

    suspend fun getGrouped(): CategoriesWithSubcategories

    suspend fun getOfExpenseType(): List<CategoryWithSubcategories>

    suspend fun getSimple(): List<Category>

}