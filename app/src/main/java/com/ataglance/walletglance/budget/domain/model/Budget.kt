package com.ataglance.walletglance.budget.domain.model

import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import java.util.Locale

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
    val currentTimeWithinRangeGraphPercentage: Float,
    val currency: String,
    val linkedAccountsIds: List<Int>
) {

    fun containsAccountId(id: Int): Boolean {
        return linkedAccountsIds.contains(id)
    }

    fun applyUsedAmount(amount: Double): Budget {
        return this.copy(
            usedAmount = amount,
            usedPercentage = "%.2f".format(
                Locale.US,
                100 / amountLimit * amount
            ).toFloat()
        )
    }

    fun addToUsedAmount(amount: Double): Budget {
        return applyUsedAmount(usedAmount + amount)
    }

    fun subtractFromUsedAmount(amount: Double): Budget {
        return applyUsedAmount(usedAmount - amount)
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
