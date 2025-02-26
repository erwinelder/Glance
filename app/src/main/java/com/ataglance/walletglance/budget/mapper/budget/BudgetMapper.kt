package com.ataglance.walletglance.budget.mapper.budget

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.utils.filterByBudgetAccounts
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.presentation.model.BudgetDraft
import com.ataglance.walletglance.budget.presentation.model.CheckedBudget
import com.ataglance.walletglance.budget.presentation.model.CheckedBudgetsByType
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.utils.getCategoryWithSubcategoryById
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.utils.enumValueOrNull
import com.ataglance.walletglance.core.utils.getLongDateRangeWithTime


fun List<BudgetEntity>.toDomainModels(
    groupedCategoriesList: List<GroupedCategories>,
    associations: List<BudgetAccountAssociation>,
    accounts: List<Account>
): List<Budget> {
    return this.mapNotNull { budget ->
        budget.toDomainModel(
            groupedCategoriesList = groupedCategoriesList,
            associations = associations,
            accounts = accounts
        )
    }
}

fun BudgetEntity.toDomainModel(
    groupedCategoriesList: List<GroupedCategories>,
    associations: List<BudgetAccountAssociation>,
    accounts: List<Account>
): Budget? {
    val categoryWithSubcategory = groupedCategoriesList.getCategoryWithSubcategoryById(
        id = this.categoryId
    )
    val linkedAccountsIds = associations.filter { it.budgetId == this.id }.map { it.accountId }
    val linkedAccounts = accounts.filter { linkedAccountsIds.contains(it.id) }

    return this.toDomainModel(
        categoryWithSub = categoryWithSubcategory, linkedAccounts = linkedAccounts
    )
}

fun BudgetEntity.toDomainModel(
    categoryWithSub: CategoryWithSub?,
    linkedAccounts: List<Account>
): Budget? {
    val repeatingPeriodEnum = enumValueOrNull<RepeatingPeriod>(repeatingPeriod) ?: return null
    val dateRange = repeatingPeriodEnum.getLongDateRangeWithTime()

    return Budget(
        id = id,
        priorityNum = categoryWithSub?.groupParentAndSubcategoryOrderNums() ?: 0.0,
        usedAmount = 0.0,
        amountLimit = amountLimit,
        usedPercentage = 0F,
        category = categoryWithSub?.getSubcategoryOrCategory(),
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
            BudgetAccountAssociation(budgetId = budget.id, accountId = accountId)
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
        linkedAccounts = accounts.filterByBudgetAccounts(this)
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
