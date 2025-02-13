package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepository
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.mapper.groupByType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCategoryCollectionsUseCaseImpl(
    private val categoryCollectionRepository: CategoryCollectionRepository
) : GetCategoryCollectionsUseCase {

    override fun getAsFlow(): Flow<CategoryCollectionsWithIdsByType> {
        return categoryCollectionRepository
            .getAllCollectionsAndAssociationsAsFlow()
            .map { it.groupByType() }
    }

    override suspend fun get(): CategoryCollectionsWithIdsByType {
        return categoryCollectionRepository.getAllCollectionsAndAssociations().groupByType()
    }

}