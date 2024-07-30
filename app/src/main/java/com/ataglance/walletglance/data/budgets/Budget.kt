package com.ataglance.walletglance.data.budgets

import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.utils.extractYearMonthDay
import com.ataglance.walletglance.domain.entities.BudgetEntity

data class Budget(
    val id: Int,
    val usedAmount: Double,
    val amountLimit: Double,
    val usedPercentage: Float,
    val category: Category?,
    val name: String,
    val repeatingPeriod: BudgetRepeatingPeriod,
    val lastResetDate: Long,
    val linkedAccountsIds: List<Int>
) {

    fun toBudgetEntity(): BudgetEntity {
        return BudgetEntity(
            id = id,
            usedAmount = usedAmount,
            amountLimit = amountLimit,
            categoryId = category?.id ?: 0,
            name = name,
            repeatingPeriod = repeatingPeriod.name,
            lastResetDate = lastResetDate
        )
    }

    fun toBudgetUiState(): EditingBudgetUiState {
        return EditingBudgetUiState(
            id = id,
            usedAmount = usedAmount.toString(),
            amountLimit = amountLimit.toString(),
            usedPercentage = usedPercentage,
            category = category,
            name = name,
            repeatingPeriod = repeatingPeriod,
            lastResetDay = lastResetDate,
            linkedAccountsIds = linkedAccountsIds
        )
    }

    fun addToUsedAmount(amount: Double): Budget {
        return this.copy(usedAmount = usedAmount + amount)
    }

    fun subtractFromUsedAmount(amount: Double): Budget {
        return this.copy(usedAmount = usedAmount - amount)
    }

    fun getNextResetDate(): Long {
        return when (repeatingPeriod) {
            BudgetRepeatingPeriod.Daily -> lastResetDate.extractYearMonthDay().addDays(1)
            BudgetRepeatingPeriod.Weekly -> lastResetDate.extractYearMonthDay().addDays(7)
            BudgetRepeatingPeriod.Monthly -> lastResetDate.extractYearMonthDay().addMonths(1)
            BudgetRepeatingPeriod.Yearly -> lastResetDate.extractYearMonthDay().addYears(1)
        }.concatenate()
    }

}
