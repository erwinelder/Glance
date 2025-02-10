package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType

interface GetCategoryCollectionsUseCase {
    suspend fun get(): CategoryCollectionsWithIdsByType
}