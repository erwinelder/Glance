package com.ataglance.walletglance.budget.domain.mapper

import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.domain.model.CheckedBudget
import com.ataglance.walletglance.budget.domain.model.CheckedBudgetsByType
import com.ataglance.walletglance.budget.utils.findById
import com.ataglance.walletglance.category.domain.CategoryWithSubcategories
import com.ataglance.walletglance.category.domain.CategoryWithSubcategory
import com.ataglance.walletglance.category.utils.getCategoryWithSubcategoryById
import com.ataglance.walletglance.core.utils.getCurrentTimeAsGraphPercentageInThisRange
import com.ataglance.walletglance.core.utils.getLongDateRangeWithTime
import com.ataglance.walletglance.core.utils.getRepeatingPeriodByString


fun BudgetEntity.toBudget(
    categoryWithSubcategory: CategoryWithSubcategory?,
    linkedAccountsIds: List<Int>,
    accountList: List<Account>
): Budget? {
    val repeatingPeriodEnum = getRepeatingPeriodByString(repeatingPeriod) ?: return null
    val linkedAccounts = accountList.filter { linkedAccountsIds.contains(it.id) }
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
        currentTimeWithinRangeGraphPercentage = dateRange
            .getCurrentTimeAsGraphPercentageInThisRange(),
        currency = linkedAccounts.firstOrNull()?.currency ?: "",
        linkedAccountsIds = linkedAccounts.map { it.id }
    )
}

fun List<BudgetEntity>.toBudgetList(
    categoryWithSubcategoriesList: List<CategoryWithSubcategories>,
    associationList: List<BudgetAccountAssociation>,
    accountList: List<Account>
): List<Budget> {
    return this.mapNotNull { budgetEntity ->
        budgetEntity.toBudget(
            categoryWithSubcategory = categoryWithSubcategoriesList
                .getCategoryWithSubcategoryById(budgetEntity.categoryId),
            linkedAccountsIds = associationList
                .filter { it.budgetId == budgetEntity.id }
                .map { it.accountId },
            accountList = accountList
        )
    }
}



fun Budget.toBudgetEntity(): BudgetEntity {
    return BudgetEntity(
        id = id,
        amountLimit = amountLimit,
        categoryId = category?.id ?: 0,
        name = name,
        repeatingPeriod = repeatingPeriod.name
    )
}

fun List<Budget>.toBudgetEntityList(): List<BudgetEntity> {
    return this.map { it.toBudgetEntity() }
}

fun List<Budget>.divideIntoBudgetsAndAssociations():
        Pair<List<BudgetEntity>, List<BudgetAccountAssociation>>
{
    val budgetList = this.toBudgetEntityList()
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



fun BudgetsByType.toCheckedBudgetsByType(checkedBudgets: List<Budget>): CheckedBudgetsByType {
    return CheckedBudgetsByType(
        daily = daily.toCheckedBudgets(checkedBudgets),
        weekly = weekly.toCheckedBudgets(checkedBudgets),
        monthly = monthly.toCheckedBudgets(checkedBudgets),
        yearly = yearly.toCheckedBudgets(checkedBudgets)
    )
}

fun List<Budget>.toCheckedBudgets(checkedBudgets: List<Budget>): List<CheckedBudget> {
    return this.map { budget ->
        CheckedBudget(
            checked = checkedBudgets.findById(budget.id) != null,
            budget = budget
        )
    }
}
