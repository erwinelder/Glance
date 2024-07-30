package com.ataglance.walletglance.data.budgets

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.data.categories.Category

@Stable
data class EditingBudgetUiState(
    val id: Int,
    val usedAmount: String,
    val amountLimit: String,
    val usedPercentage: Float,
    val category: Category?,
    val name: String,
    val repeatingPeriod: BudgetRepeatingPeriod,
    val lastResetDay: Long,
    val linkedAccountsIds: List<Int>
) {

    fun toBudget(): Budget? {
        val newCurrentAmount = usedAmount.toDoubleOrNull() ?: return null
        val newAmountLimit = amountLimit.toDoubleOrNull() ?: return null

        return Budget(
            id = id,
            usedAmount = newCurrentAmount,
            amountLimit = newAmountLimit,
            usedPercentage = usedPercentage,
            category = category,
            name = name.trim(),
            repeatingPeriod = repeatingPeriod,
            lastResetDate = lastResetDay,
            linkedAccountsIds = linkedAccountsIds
        )
    }

    fun allowSaving(): Boolean {
        return name.isNotBlank()
    }

}
