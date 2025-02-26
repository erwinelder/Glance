package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface GetBudgetsOnWidgetUseCase {

    fun getFlow(): Flow<List<Budget>>

}