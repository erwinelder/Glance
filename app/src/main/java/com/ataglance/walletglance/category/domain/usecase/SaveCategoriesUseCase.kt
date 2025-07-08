package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.Category

interface SaveCategoriesUseCase {

    suspend fun saveAndDeleteRest(categories: List<Category>)

    suspend fun save(categories: List<Category>)

}