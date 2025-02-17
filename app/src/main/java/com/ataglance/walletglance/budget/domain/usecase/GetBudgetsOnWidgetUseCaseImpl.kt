package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.utils.fillUsedAmountsByRecords
import com.ataglance.walletglance.budget.domain.utils.getMaxDateRange
import com.ataglance.walletglance.budget.mapper.budget.toDomainModels
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordsInDateRangeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class GetBudgetsOnWidgetUseCaseImpl(
    private val budgetRepository: BudgetRepository,
    private val getBudgetIdsOnWidgetUseCase: GetBudgetIdsOnWidgetUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getRecordsInDateRangeUseCase: GetRecordsInDateRangeUseCase
) : GetBudgetsOnWidgetUseCase {

    override fun getFlow(): Flow<List<Budget>> = flow {
        val (entities, associations) = budgetRepository.getAllBudgetsAndAssociations()
        val categoryWithSubcategoriesList = getCategoriesUseCase.getOfExpenseType()
        val accounts = getAccountsUseCase.getAll()

        val budgets = entities.toDomainModels(
            groupedCategoriesList = categoryWithSubcategoriesList,
            associations = associations,
            accounts = accounts
        )
        val budgetsMaxDateRange = budgets.getMaxDateRange() ?: run {
            emit(emptyList())
            return@flow
        }

        combine(
            getBudgetIdsOnWidgetUseCase.getFlow(),
            getRecordsInDateRangeUseCase.getFlow(range = budgetsMaxDateRange)
        ) { budgetIds, records ->
            budgets.filter { it.id in budgetIds }.fillUsedAmountsByRecords(records)
        }.collect(::emit)
    }

}