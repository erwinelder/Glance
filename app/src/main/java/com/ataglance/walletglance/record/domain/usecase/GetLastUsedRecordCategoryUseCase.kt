package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType

interface GetLastUsedRecordCategoryUseCase {

    suspend fun execute(
        type: CategoryType,
        accountId: Int? = null,
        categories: GroupedCategoriesByType? = null
    ): CategoryWithSub?

}