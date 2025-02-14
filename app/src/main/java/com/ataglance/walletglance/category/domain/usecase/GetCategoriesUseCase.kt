package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import kotlinx.coroutines.flow.Flow

interface GetCategoriesUseCase {

    fun getGroupedAsFlow(): Flow<GroupedCategoriesByType>

    suspend fun getGrouped(): GroupedCategoriesByType

    suspend fun getOfExpenseType(): List<GroupedCategories>

    suspend fun getAsList(): List<Category>

}