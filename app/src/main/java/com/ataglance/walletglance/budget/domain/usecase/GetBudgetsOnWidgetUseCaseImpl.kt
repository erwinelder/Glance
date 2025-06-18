package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.utils.fillUsedAmountsByRecords
import com.ataglance.walletglance.budget.domain.utils.getMaxDateRange
import com.ataglance.walletglance.budget.mapper.budget.toDomainModels
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordsInDateRangeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest

class GetBudgetsOnWidgetUseCaseImpl(
    private val budgetRepository: BudgetRepository,
    private val getBudgetIdsOnWidgetUseCase: GetBudgetIdsOnWidgetUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getRecordsInDateRangeUseCase: GetRecordsInDateRangeUseCase
) : GetBudgetsOnWidgetUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAsFlow(): Flow<List<Budget>> = flow {
        val (entities, associations) = budgetRepository.getAllBudgetsAndAssociations()
        val categoryWithSubcategoriesList = getCategoriesUseCase.getOfExpenseType()
        val accounts = getAccountsUseCase.getAll()

        val allBudgets = entities.toDomainModels(
            groupedCategoriesList = categoryWithSubcategoriesList,
            associations = associations,
            accounts = accounts
        )

        val budgetsOnWidget = getBudgetIdsOnWidgetUseCase.getAsFlow().mapLatest { budgetIds ->
            allBudgets.filter { it.id in budgetIds }
        }

        val recordsInDateRange = budgetsOnWidget.flatMapConcat {
            it.getMaxDateRange()
                ?.let { range ->
                    getRecordsInDateRangeUseCase.getFlow(range = range)
                }
                ?: flow { emit(emptyList()) }
        }

        combine(budgetsOnWidget, recordsInDateRange) { budgets, records ->
            budgets.fillUsedAmountsByRecords(records)
        }.collect(::emit)
    }

}