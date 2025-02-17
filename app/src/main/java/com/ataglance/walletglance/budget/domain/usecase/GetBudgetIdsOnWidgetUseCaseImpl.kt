package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetOnWidgetRepository
import com.ataglance.walletglance.budget.mapper.budgetOnWidget.toIntList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBudgetIdsOnWidgetUseCaseImpl(
    private val budgetOnWidgetRepository: BudgetOnWidgetRepository
) : GetBudgetIdsOnWidgetUseCase {
    override fun getFlow(): Flow<List<Int>> {
        return budgetOnWidgetRepository.getAllBudgetsOnWidgetFlow().map { it.toIntList() }
    }
}