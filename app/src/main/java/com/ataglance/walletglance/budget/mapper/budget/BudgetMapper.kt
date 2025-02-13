package com.ataglance.walletglance.budget.mapper.budget

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.presentation.model.BudgetDraft
import com.ataglance.walletglance.budget.presentation.model.CheckedBudget
import com.ataglance.walletglance.budget.presentation.model.CheckedBudgetsByType
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategories
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategory
import com.ataglance.walletglance.category.domain.utils.getCategoryWithSubcategoryById
import com.ataglance.walletglance.core.utils.getCurrentTimeAsGraphPercentageInThisRange
import com.ataglance.walletglance.core.utils.getLongDateRangeWithTime
import com.ataglance.walletglance.core.utils.getRepeatingPeriodByString


fun List<BudgetEntity>.toDomainModels(
    categoryWithSubcategoriesList: List<CategoryWithSubcategories>,
    associations: List<BudgetAccountAssociation>,
    accounts: List<Account>
): List<Budget> {
    return this.mapNotNull { budget ->
        budget.toDomainModel(
            categoryWithSubcategoriesList = categoryWithSubcategoriesList,
            associations = associations,
            accounts = accounts
        )
    }
}

fun BudgetEntity.toDomainModel(
    categoryWithSubcategoriesList: List<CategoryWithSubcategories>,
    associations: List<BudgetAccountAssociation>,
    accounts: List<Account>
): Budget? {
    val categoryWithSubcategory = categoryWithSubcategoriesList.getCategoryWithSubcategoryById(
        id = this.categoryId
    )
    val linkedAccountsIds = associations.filter { it.budgetId == this.id }.map { it.accountId }
    val linkedAccounts = accounts.filter { linkedAccountsIds.contains(it.id) }

    return this.toDomainModel(
        categoryWithSubcategory = categoryWithSubcategory, linkedAccounts = linkedAccounts
    )
}

fun BudgetEntity.toDomainModel(
    categoryWithSubcategory: CategoryWithSubcategory?,
    linkedAccounts: List<Account>
): Budget? {
    val repeatingPeriodEnum = getRepeatingPeriodByString(repeatingPeriod) ?: return null
    val dateRange = repeatingPeriodEnum.getLongDateRangeWithTime()

    return Budget(
        id = id,
        priorityNum = categoryWithSubcategory?.groupParentAndSubcategoryOrderNums() ?: 0.0,
        usedAmount = 0.0,
        amountLimit = amountLimit,
        usedPercentage = 0F,
        category = categoryWithSubcategory?.getSubcategoryOrCategory(),
        name = name,
        repeatingPeriod = repeatingPeriodEnum,
        dateRange = dateRange,
        currentTimeWithinRangeGraphPercentage = dateRange.getCurrentTimeAsGraphPercentageInThisRange(),
        currency = linkedAccounts.firstOrNull()?.currency ?: "",
        linkedAccountsIds = linkedAccounts.map { it.id }
    )
}


fun List<Budget>.toDataModels(): List<BudgetEntity> {
    return this.map { it.toDataModel() }
}

fun Budget.toDataModel(): BudgetEntity {
    return BudgetEntity(
        id = id,
        amountLimit = amountLimit,
        categoryId = category?.id ?: 0,
        name = name,
        repeatingPeriod = repeatingPeriod.name
    )
}


fun List<Budget>.divideIntoBudgetsAndAssociations(): Pair<List<BudgetEntity>, List<BudgetAccountAssociation>> {
    val budgetList = this.toDataModels()
    val associationList = this.flatMap { budget ->
        budget.linkedAccountsIds.map { accountId ->
            BudgetAccountAssociation(
                budgetId = budget.id,
                accountId = accountId
            )
        }
    }
    return budgetList to associationList
}


fun BudgetsByType.toCheckedBudgetsByType(checkedBudgetsIds: List<Int>): CheckedBudgetsByType {
    return CheckedBudgetsByType(
        daily = daily.toCheckedBudgets(checkedBudgetsIds),
        weekly = weekly.toCheckedBudgets(checkedBudgetsIds),
        monthly = monthly.toCheckedBudgets(checkedBudgetsIds),
        yearly = yearly.toCheckedBudgets(checkedBudgetsIds)
    )
}

fun List<Budget>.toCheckedBudgets(checkedBudgetsIds: List<Int>): List<CheckedBudget> {
    return this.map { budget ->
        CheckedBudget(
            checked = checkedBudgetsIds.contains(budget.id),
            budget = budget
        )
    }
}


fun Budget.toDraft(accounts: List<Account>): BudgetDraft {
    return BudgetDraft(
        isNew = false,
        id = id,
        amountLimit = amountLimit.toString(),
        category = category,
        name = name,
        currRepeatingPeriod = repeatingPeriod,
        newRepeatingPeriod = repeatingPeriod,
        linkedAccounts = accounts.filter { linkedAccountsIds.contains(it.id) }
    )
}

fun BudgetDraft.toNewBudget(): Budget? {
    val newAmountLimit = amountLimit.toDoubleOrNull() ?: return null
    val dateRange = newRepeatingPeriod.getLongDateRangeWithTime()

    return Budget(
        id = id,
        priorityNum = priorityNum,
        amountLimit = newAmountLimit,
        usedAmount = 0.0,
        usedPercentage = 0F,
        category = category,
        name = name,
        repeatingPeriod = newRepeatingPeriod,
        dateRange = dateRange,
        currentTimeWithinRangeGraphPercentage = dateRange.getCurrentTimeAsGraphPercentageInThisRange(),
        currency = linkedAccounts.firstOrNull()?.currency ?: "",
        linkedAccountsIds = linkedAccounts.map { it.id }
    )
}

fun BudgetDraft.copyDataToBudget(budget: Budget): Budget {
    val newAmountLimit = amountLimit.toDoubleOrNull() ?: return budget

    return budget.copy(
        amountLimit = newAmountLimit,
        category = category,
        name = name,
        repeatingPeriod = newRepeatingPeriod,
        currency = linkedAccounts.firstOrNull()?.currency ?: "",
        linkedAccountsIds = linkedAccounts.map { it.id }
    )
}
