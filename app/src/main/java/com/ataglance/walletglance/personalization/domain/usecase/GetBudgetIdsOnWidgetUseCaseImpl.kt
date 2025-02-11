package com.ataglance.walletglance.personalization.domain.usecase

import com.ataglance.walletglance.personalization.data.repository.BudgetOnWidgetRepository
import com.ataglance.walletglance.personalization.mapper.toIntList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBudgetIdsOnWidgetUseCaseImpl(
    private val budgetOnWidgetRepository: BudgetOnWidgetRepository
) : GetBudgetIdsOnWidgetUseCase {
    override fun getAsFlow(): Flow<List<Int>> {
        return budgetOnWidgetRepository.getAllBudgetsOnWidget().map { it.toIntList() }
    }
}