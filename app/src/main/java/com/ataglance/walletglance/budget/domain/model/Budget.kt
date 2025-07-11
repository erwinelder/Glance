package com.ataglance.walletglance.budget.domain.model

import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.core.utils.roundToTwoDecimals
import com.ataglance.walletglance.core.utils.roundToTwoDecimalsAsFloat

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
    val linkedAccountIds: List<Int>
) {

    fun applyUsedAmount(amount: Double): Budget {
        return this.copy(
            usedAmount = amount.roundToTwoDecimals(),
            usedPercentage = (100 / amountLimit * amount).roundToTwoDecimalsAsFloat()
        )
    }

}
