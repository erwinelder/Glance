package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.utils.translateCategories

class TranslateCategoriesUseCaseImpl(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val saveCategoriesUseCase: SaveCategoriesUseCase
) : TranslateCategoriesUseCase {
    override suspend fun execute(
        defaultCategoriesInCurrLocale: List<Category>,
        defaultCategoriesInNewLocale: List<Category>
    ) {
        val currCategories = getCategoriesUseCase.getAsList()
        val translatedCategories = translateCategories(
            defaultCategoriesInCurrLocale = defaultCategoriesInCurrLocale,
            defaultCategoriesInNewLocale = defaultCategoriesInNewLocale,
            currCategoryList = currCategories
        )
        saveCategoriesUseCase.upsert(categories = translatedCategories)
    }
}