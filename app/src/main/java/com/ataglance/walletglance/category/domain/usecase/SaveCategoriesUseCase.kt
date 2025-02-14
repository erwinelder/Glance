package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.Category

interface SaveCategoriesUseCase {

    suspend fun upsert(categories: List<Category>)

    suspend fun save(categories: List<Category>)

}