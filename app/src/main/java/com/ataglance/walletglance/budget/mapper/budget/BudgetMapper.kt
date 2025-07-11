package com.ataglance.walletglance.budget.mapper.budget

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.utils.filterByBudgetAccounts
import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociationDataModel
import com.ataglance.walletglance.budget.data.model.BudgetDataModel
import com.ataglance.walletglance.budget.data.model.BudgetDataModelWithAssociations
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.presentation.model.BudgetDraft
import com.ataglance.walletglance.budget.presentation.model.CheckedBudget
import com.ataglance.walletglance.budget.presentation.model.CheckedBudgetsByType
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.utils.getCategoryWithSubcategoryById
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.utils.enumValueOrNull
import com.ataglance.walletglance.core.utils.roundToTwoDecimals
import com.ataglance.walletglance.core.utils.roundToTwoDecimalsAsString
import com.ataglance.walletglance.core.utils.toTimestampRange


fun Budget.toDataModel(): BudgetDataModel {
    return BudgetDataModel(
        id = id,
        amountLimit = amountLimit,
        categoryId = category?.id ?: 0,
        name = name,
        repeatingPeriod = repeatingPeriod.name
    )
}

fun Budget.toDataModelWithAssociations(): BudgetDataModelWithAssociations {
    return BudgetDataModelWithAssociations(
        budget = toDataModel(),
        associations = linkedAccountIds.map { accountId ->
            BudgetAccountAssociationDataModel(budgetId = id, accountId = accountId)
        }
    )
}

fun BudgetDataModelWithAssociations.toDomainModel(
    groupedCategoriesList: List<GroupedCategories>,
    accounts: List<Account>
): Budget? {
    val categoryWithSubcategory = groupedCategoriesList.getCategoryWithSubcategoryById(
        id = budget.categoryId
    )
    val linkedAccountsIds = associations.filter { it.budgetId == budget.id }.map { it.accountId }
    val linkedAccounts = accounts.filter { linkedAccountsIds.contains(it.id) }

    val repeatingPeriodEnum = enumValueOrNull<RepeatingPeriod>(budget.repeatingPeriod) ?: return null
    val dateRange = repeatingPeriodEnum.toTimestampRange()

    return Budget(
        id = budget.id,
        priorityNum = categoryWithSubcategory?.groupParentAndSubcategoryOrderNums() ?: 0.0,
        usedAmount = 0.0,
        amountLimit = budget.amountLimit.roundToTwoDecimals(),
        usedPercentage = 0F,
        category = categoryWithSubcategory?.getSubcategoryOrCategory(),
        name = budget.name,
        repeatingPeriod = repeatingPeriodEnum,
        dateRange = dateRange,
        currentTimeWithinRangeGraphPercentage = dateRange.getCurrentDateAsGraphPercentage(),
        currency = linkedAccounts.firstOrNull()?.currency ?: "",
        linkedAccountIds = linkedAccounts.map { it.id }
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

fun BudgetsByType.toCheckedBudgetsByType(checkedBudgetsIds: List<Int>): CheckedBudgetsByType {
    return CheckedBudgetsByType(
        daily = daily.toCheckedBudgets(checkedBudgetsIds),
        weekly = weekly.toCheckedBudgets(checkedBudgetsIds),
        monthly = monthly.toCheckedBudgets(checkedBudgetsIds),
        yearly = yearly.toCheckedBudgets(checkedBudgetsIds)
    )
}


fun Budget.toDraft(accounts: List<Account>): BudgetDraft {
    return BudgetDraft(
        isNew = false,
        id = id,
        amountLimit = amountLimit.roundToTwoDecimalsAsString(),
        category = category,
        name = name,
        currRepeatingPeriod = repeatingPeriod,
        newRepeatingPeriod = repeatingPeriod,
        linkedAccounts = accounts.filterByBudgetAccounts(this)
    )
}

fun BudgetDraft.toNewBudget(): Budget? {
    val newAmountLimit = amountLimit.toDoubleOrNull() ?: return null
    val dateRange = newRepeatingPeriod.toTimestampRange()

    return Budget(
        id = id,
        priorityNum = priorityNum,
        amountLimit = newAmountLimit.roundToTwoDecimals(),
        usedAmount = 0.0,
        usedPercentage = 0F,
        category = category,
        name = name,
        repeatingPeriod = newRepeatingPeriod,
        dateRange = dateRange,
        currentTimeWithinRangeGraphPercentage = dateRange.getCurrentDateAsGraphPercentage(),
        currency = linkedAccounts.firstOrNull()?.currency ?: "",
        linkedAccountIds = linkedAccounts.map { it.id }
    )
}

fun BudgetDraft.copyDataToBudget(budget: Budget): Budget {
    val newAmountLimit = amountLimit.toDoubleOrNull() ?: return budget

    return budget.copy(
        amountLimit = newAmountLimit.roundToTwoDecimals(),
        category = category,
        name = name,
        repeatingPeriod = newRepeatingPeriod,
        currency = linkedAccounts.firstOrNull()?.currency ?: "",
        linkedAccountIds = linkedAccounts.map { it.id }
    )
}
