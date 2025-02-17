package com.ataglance.walletglance.budget.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetBudgetIdsOnWidgetUseCase {
    fun getFlow(): Flow<List<Int>>
}