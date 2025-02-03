package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import kotlinx.coroutines.flow.Flow

interface GetAllCategoriesUseCase {
    fun execute(): Flow<CategoriesWithSubcategories>
}