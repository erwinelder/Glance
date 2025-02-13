package com.ataglance.walletglance.budget.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetBudgetIdsOnWidgetUseCase {
    fun getAsFlow(): Flow<List<Int>>
}