package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepository
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.mapper.groupByType

class GetCategoryCollectionsUseCaseImpl(
    private val categoryCollectionRepository: CategoryCollectionRepository
) : GetCategoryCollectionsUseCase {
    override suspend fun get(): CategoryCollectionsWithIdsByType {
        return categoryCollectionRepository.getAllCollectionsAndAssociations().groupByType()
    }
}