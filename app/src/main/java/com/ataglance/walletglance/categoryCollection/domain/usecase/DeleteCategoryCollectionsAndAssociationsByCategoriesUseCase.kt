package com.ataglance.walletglance.categoryCollection.domain.usecase

interface DeleteCategoryCollectionsAndAssociationsByCategoriesUseCase {
    suspend fun execute(categoryIds: List<Int>)
}