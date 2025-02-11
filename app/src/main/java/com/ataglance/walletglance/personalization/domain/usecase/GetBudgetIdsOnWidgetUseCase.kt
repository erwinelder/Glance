package com.ataglance.walletglance.personalization.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetBudgetIdsOnWidgetUseCase {
    fun getAsFlow(): Flow<List<Int>>
}