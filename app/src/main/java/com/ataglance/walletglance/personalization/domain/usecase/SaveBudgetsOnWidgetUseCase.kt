package com.ataglance.walletglance.personalization.domain.usecase

interface SaveBudgetsOnWidgetUseCase {
    suspend fun execute(budgetsIds: List<Int>)
}