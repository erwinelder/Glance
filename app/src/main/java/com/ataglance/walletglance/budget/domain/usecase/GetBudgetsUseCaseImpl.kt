package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.domain.utils.groupByType
import com.ataglance.walletglance.budget.mapper.budget.toDomainModel
import com.ataglance.walletglance.budget.mapper.budget.toDomainModels
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordsInDateRangeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class GetBudgetsUseCaseImpl(
    private val budgetRepository: BudgetRepository,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getRecordsInDateRangeUseCase: GetRecordsInDateRangeUseCase
) : GetBudgetsUseCase {

    override fun getGroupedByTypeAsFlow(): Flow<BudgetsByType> = flow {
        val (budgets, associations) = budgetRepository.getAllBudgetsAndAssociations()
        val categoryWithSubcategoriesList = getCategoriesUseCase.getOfExpenseType()
        val accounts = getAccountsUseCase.getAll()

        val budgetsByType = budgets
            .toDomainModels(
                groupedCategoriesList = categoryWithSubcategoriesList,
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

    override suspend fun getGroupedByType(): BudgetsByType {
        return getGroupedByTypeAsFlow().firstOrNull() ?: BudgetsByType()
    }

    override suspend fun get(id: Int, accounts: List<Account>): Budget? {
        val (budget, associations) = budgetRepository.getBudgetAndAssociations(budgetId = id)
            ?: return null
        val categoryWithSubcategoriesList = getCategoriesUseCase.getOfExpenseType()

        return budget.toDomainModel(
            groupedCategoriesList = categoryWithSubcategoriesList,
            associations = associations,
            accounts = accounts
        )
    }

}