package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.Category

interface SaveCategoriesUseCase {
    suspend fun execute(categories: List<Category>)
    suspend fun execute(categoriesToSave: List<Category>, currentCategories: List<Category>)
}