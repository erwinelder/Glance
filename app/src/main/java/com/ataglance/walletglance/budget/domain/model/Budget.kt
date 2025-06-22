package com.ataglance.walletglance.budget.domain.model

import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.domain.date.TimestampRange
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
    val dateRange: TimestampRange,
    val currentTimeWithinRangeGraphPercentage: Float,
    val currency: String,
    val linkedAccountsIds: List<Int>
) {

    fun containsAccount(id: Int): Boolean {
        return linkedAccountsIds.contains(id)
    }

    fun applyUsedAmount(amount: Double): Budget {
        return this.copy(
            usedAmount = amount,
            usedPercentage = "%.2f".format(
                locale = Locale.US,
                100 / amountLimit * amount
            ).toFloat()
        )
    }

}
