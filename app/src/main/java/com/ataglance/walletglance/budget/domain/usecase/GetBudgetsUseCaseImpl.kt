package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAllAccountsUseCase
import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.domain.utils.groupByType
import com.ataglance.walletglance.budget.mapper.toDomainModels
import com.ataglance.walletglance.category.domain.usecase.GetExpenseCategoriesUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordsInDateRangeUseCase

class GetBudgetsUseCaseImpl(
    private val budgetRepository: BudgetRepository,
    private val getExpenseCategoriesUseCase: GetExpenseCategoriesUseCase,
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val getRecordsInDateRangeUseCase: GetRecordsInDateRangeUseCase
) : GetBudgetsUseCase {
    override suspend fun get(): BudgetsByType {
        val (budgetEntities, associations) = budgetRepository.getAllBudgetsAndAssociations()
        val categoryWithSubcategoriesList = getExpenseCategoriesUseCase.execute()
        val accounts = getAllAccountsUseCase.get()

        val budgetsByType = budgetEntities
            .toDomainModels(
                categoryWithSubcategoriesList = categoryWithSubcategoriesList,
                associations = associations,
                accounts = accounts
            )
            .groupByType()

        val budgetsMaxDateRange = budgetsByType.getMaxDateRange() ?: return BudgetsByType()
        val records = getRecordsInDateRangeUseCase.get(range = budgetsMaxDateRange)

        return budgetsByType.fillUsedAmountsByRecords(records)
    }
}