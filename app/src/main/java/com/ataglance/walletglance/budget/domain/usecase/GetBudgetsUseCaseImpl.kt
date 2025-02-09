package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.domain.utils.groupByType
import com.ataglance.walletglance.budget.mapper.toDomainModels
import com.ataglance.walletglance.category.domain.usecase.GetExpenseCategoriesUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordsInDateRangeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetBudgetsUseCaseImpl(
    private val budgetRepository: BudgetRepository,
    private val getExpenseCategoriesUseCase: GetExpenseCategoriesUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getRecordsInDateRangeUseCase: GetRecordsInDateRangeUseCase
) : GetBudgetsUseCase {
    override fun getAsFlow(): Flow<BudgetsByType> = flow {
        val (budgetEntities, associations) = budgetRepository.getAllBudgetsAndAssociations()
        val categoryWithSubcategoriesList = getExpenseCategoriesUseCase.execute()
        val accounts = getAccountsUseCase.getAll()

        val budgetsByType = budgetEntities
            .toDomainModels(
                categoryWithSubcategoriesList = categoryWithSubcategoriesList,
                associations = associations,
                accounts = accounts
            )
            .groupByType()

        val budgetsMaxDateRange = budgetsByType.getMaxDateRange() ?: run {
            emit(BudgetsByType())
            return@flow
        }
        getRecordsInDateRangeUseCase.getAsFlow(range = budgetsMaxDateRange).collect { records ->
            emit(budgetsByType.fillUsedAmountsByRecords(records))
        }
    }
}