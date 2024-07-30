package com.ataglance.walletglance.data.utils

import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.budgets.BudgetRepeatingPeriod
import com.ataglance.walletglance.data.makingRecord.MakeRecordUnitUiState
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.domain.entities.BudgetAccountAssociation
import com.ataglance.walletglance.domain.entities.BudgetEntity


fun getRepeatingPeriodByString(periodValue: String): BudgetRepeatingPeriod? {
    return when (periodValue) {
        BudgetRepeatingPeriod.Daily.name -> BudgetRepeatingPeriod.Daily
        BudgetRepeatingPeriod.Weekly.name -> BudgetRepeatingPeriod.Weekly
        BudgetRepeatingPeriod.Monthly.name -> BudgetRepeatingPeriod.Monthly
        BudgetRepeatingPeriod.Yearly.name -> BudgetRepeatingPeriod.Yearly
        else -> null
    }
}


fun List<Budget>.toEntityList(): List<BudgetEntity> {
    return this.map { it.toBudgetEntity() }
}


fun List<Budget>.breakOnBudgetsAndAssociations():
        Pair<List<BudgetEntity>, List<BudgetAccountAssociation>>
{
    val budgetList = this.map { budget ->
        budget.toBudgetEntity()
    }
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


fun List<Budget>.findById(id: Int): Budget? {
    return this.find { it.id == id }
}


fun List<BudgetEntity>.getIdsThatAreNotInList(
    list: List<BudgetEntity>
): List<Int> {
    return this
        .filter { budget ->
            list.find { it.id == budget.id } == null
        }
        .map { it.id }
}

fun List<BudgetAccountAssociation>.getAssociationsThatAreNotInList(
    list: List<BudgetAccountAssociation>
): List<BudgetAccountAssociation> {
    return this
        .filter { thisAssociation ->
            list.find {
                it.budgetId == thisAssociation.budgetId &&
                        it.accountId == thisAssociation.accountId
            } == null
        }
}


fun List<Budget>.addAmountsOfRecordUnitListWithAccountId(
    accountId: Int,
    recordUnitList: List<MakeRecordUnitUiState>,
    recordDate: Long
): List<Budget> {
    return this
        .getBudgetsLinkedWithAccount(accountId)
        .mapNotNull { budget ->
            budget
                .takeIf { it.lastResetDate < recordDate && it.category != null }
                ?.addToUsedAmount(
                    amount = recordUnitList.getTotalAmountByCategory(budget.category!!.id)
                )
        }
}


fun List<Budget>.addAmountWhereAccountIdAndCategoryIdAre(
    amount: Double,
    accountId: Int,
    categoryIds: List<Int>,
    recordDate: Long
): List<Budget> {
    return this
        .getBudgetsLinkedWithAccount(accountId)
        .mapNotNull { budget ->
            budget
                .takeIf {
                    it.lastResetDate < recordDate && it.category != null &&
                            categoryIds.contains(it.category.id)
                }
                ?.addToUsedAmount(amount)
        }
}


fun List<Budget>.subtractAmountsOfRecordStack(
    recordStack: RecordStack,
    recordDate: Long
): List<Budget> {
    return this
        .getBudgetsLinkedWithAccount(recordStack.account.id)
        .mapNotNull { budget ->
            budget
                .takeIf { it.lastResetDate < recordDate && it.category != null }
                ?.subtractFromUsedAmount(
                    amount = recordStack.getTotalAmountByCategory(budget.category!!.id)
                )
        }
}


fun List<Budget>.subtractAmountWhereAccountIdAndCategoryIdAre(
    amount: Double,
    accountId: Int,
    categoryIds: List<Int>,
    recordDate: Long
): List<Budget> {
    return this
        .getBudgetsLinkedWithAccount(accountId)
        .mapNotNull { budget ->
            budget.takeIf {
                it.lastResetDate < recordDate && it.category != null &&
                        categoryIds.contains(it.category.id)
            }
                ?.subtractFromUsedAmount(amount)
        }
}


fun List<Budget>.getBudgetsLinkedWithAccount(accountId: Int): List<Budget> {
    return this.filter { it.linkedAccountsIds.contains(accountId) }
}


fun List<Budget>.mergeWith(list: List<Budget>): List<Budget> {
    val mergedList = this.toMutableList()

    list.forEach { budget ->
        budget
            .takeIf { mergedList.findById(it.id) == null }
            ?.let { mergedList.add(it) }
    }

    return mergedList
}


fun List<Budget>.resetIfNeeded(): List<Budget> {
    val currentDate = getTodayDateLong()

    return this.mapNotNull { budget ->
        budget.takeIf { it.getNextResetDate() <= currentDate }?.copy(
            lastResetDate = currentDate
        )
    }
}