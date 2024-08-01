package com.ataglance.walletglance.domain.repositories

import com.ataglance.walletglance.domain.dao.BudgetDao
import com.ataglance.walletglance.domain.entities.BudgetEntity

class BudgetRepository(
    private val dao: BudgetDao
) {

    suspend fun upsertBudgets(budgetList: List<BudgetEntity>) {
        dao.upsertBudgets(budgetList)
    }

}