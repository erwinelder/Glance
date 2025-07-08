package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.mapper.budget.toDomainModel
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase

class GetEmptyBudgetsUseCaseImpl(
    private val budgetRepository: BudgetRepository,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
) : GetEmptyBudgetsUseCase {

    override suspend fun get(id: Int): Budget? {
        val accounts = getAccountsUseCase.getAll()
        return get(id = id, accounts = accounts)
    }

    override suspend fun get(id: Int, accounts: List<Account>): Budget? {
        val budgetWithAssociations = budgetRepository.getBudgetWithAssociations(budgetId = id)
            ?: return null
        val groupedCategoriesList = getCategoriesUseCase.getOfExpenseType()

        return budgetWithAssociations.toDomainModel(
            groupedCategoriesList = groupedCategoriesList,
            accounts = accounts
        )
    }

    override suspend fun get(): List<Budget> {
        val groupedCategoriesList = getCategoriesUseCase.getOfExpenseType()
        val accounts = getAccountsUseCase.getAll()

        return budgetRepository.getAllBudgetsWithAssociations().mapNotNull { budget ->
            budget.toDomainModel(groupedCategoriesList = groupedCategoriesList, accounts = accounts)
        }
    }

}