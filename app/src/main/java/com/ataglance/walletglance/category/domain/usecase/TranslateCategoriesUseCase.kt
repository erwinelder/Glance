package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.Category

interface TranslateCategoriesUseCase {
    suspend fun execute(
        defaultCategoriesInCurrLocale: List<Category>,
        defaultCategoriesInNewLocale: List<Category>
    )
}