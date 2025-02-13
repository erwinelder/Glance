package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import kotlinx.coroutines.flow.Flow

interface GetCategoryCollectionsUseCase {

    fun getAsFlow(): Flow<CategoryCollectionsWithIdsByType>

    suspend fun get(): CategoryCollectionsWithIdsByType

}