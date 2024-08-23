package com.ataglance.walletglance.data.budgets

import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.date.LongDateRange
import com.ataglance.walletglance.data.date.RepeatingPeriod
import com.ataglance.walletglance.data.local.entities.BudgetEntity

data class Budget(
    val id: Int,
    val priorityNum: Double,
    val amountLimit: Double,
    val usedAmount: Double,
    val usedPercentage: Float,
    val category: Category?,
    val name: String,
    val repeatingPeriod: RepeatingPeriod,
    val dateRange: LongDateRange,
    val currency: String,
    val linkedAccountsIds: List<Int>
) {

    fun containsAccountId(id: Int): Boolean {
        return linkedAccountsIds.contains(id)
    }

    fun applyUsedAmount(amount: Double): Budget {
        return this.copy(
            usedAmount = amount,
            usedPercentage = ".2f".format(100 / amountLimit * usedAmount).toFloat()
        )
    }

    fun addToUsedAmount(amount: Double): Budget {
        return applyUsedAmount(usedAmount + amount)
    }

    fun subtractFromUsedAmount(amount: Double): Budget {
        return applyUsedAmount(usedAmount - amount)
    }

    fun toEntity(): BudgetEntity {
        return BudgetEntity(
            id = id,
            amountLimit = amountLimit,
            categoryId = category?.id ?: 0,
            name = name,
            repeatingPeriod = repeatingPeriod.name
        )
    }

    fun toBudgetUiState(accountList: List<Account>): EditingBudgetUiState {
        return EditingBudgetUiState(
            isNew = false,
            id = id,
            amountLimit = amountLimit.toString(),
            category = category,
            name = name,
            currRepeatingPeriod = repeatingPeriod,
            newRepeatingPeriod = repeatingPeriod,
            linkedAccounts = accountList.filter { linkedAccountsIds.contains(it.id) }
        )
    }

    fun getTotalAmountByCurrentDateRange(): TotalAmountByRange {
        return TotalAmountByRange(dateRange = dateRange, totalAmount = usedAmount)
    }

}
