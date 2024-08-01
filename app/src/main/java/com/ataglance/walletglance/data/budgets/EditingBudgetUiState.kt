package com.ataglance.walletglance.data.budgets

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.utils.getTodayDateLong

@Stable
data class EditingBudgetUiState(
    val id: Int = 0,
    val usedAmount: String = "",
    val amountLimit: String = "",
    val usedPercentage: Float = 0f,
    val category: Category? = null,
    val name: String = "",
    val repeatingPeriod: BudgetRepeatingPeriod = BudgetRepeatingPeriod.OneTime,
    val lastResetDay: Long = getTodayDateLong(),
    val linkedAccountsIds: List<Int> = emptyList()
) {

    fun toBudget(): Budget? {
        val newCurrentAmount = usedAmount.toDoubleOrNull() ?: return null
        val newAmountLimit = amountLimit.toDoubleOrNull() ?: return null

        return Budget(
            id = id,
            usedAmount = newCurrentAmount,
            amountLimit = newAmountLimit,
            usedPercentage = ".2f".format(100 / newAmountLimit * newCurrentAmount).toFloat(),
            category = category,
            name = name.trim(),
            repeatingPeriod = repeatingPeriod,
            lastResetDate = lastResetDay,
            linkedAccountsIds = linkedAccountsIds
        )
    }

    fun allowSaving(): Boolean {
        val newCurrentAmount = usedAmount.toDoubleOrNull() ?: return false
        val newAmountLimit = amountLimit.toDoubleOrNull() ?: return false

        return name.isNotBlank() && newAmountLimit >= newCurrentAmount
    }

}
