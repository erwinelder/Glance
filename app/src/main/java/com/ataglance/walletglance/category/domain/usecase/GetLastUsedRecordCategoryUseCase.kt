package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSub

interface GetLastUsedRecordCategoryUseCase {
    suspend fun get(type: CategoryType): CategoryWithSub?
}