package com.ataglance.walletglance.data.budgets

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.date.RepeatingPeriod

@Stable
data class EditingBudgetUiState(
    val isNew: Boolean = true,
    val id: Int = 0,
    val amountLimit: String = "",
    val category: Category? = null,
    val name: String = "",
    val repeatingPeriod: RepeatingPeriod = RepeatingPeriod.Monthly,
    val linkedAccounts: List<Account> = emptyList()
) {

    fun allowSaving(): Boolean {
        val newAmountLimit = amountLimit.toDoubleOrNull() ?: return false

        return name.isNotBlank() && newAmountLimit > 0.0
    }

    fun toBudget(): Budget? {
        val newAmountLimit = amountLimit.toDoubleOrNull() ?: return null

        return Budget(
            id = id,
            amountLimit = newAmountLimit,
            usedAmount = ,
            usedPercentage = usedPercentage,
            category = category,
            name = name,
            repeatingPeriod = repeatingPeriod,
            dateRange = validityDateRange,
            currency = currency,
            linkedAccountsIds = linkedAccountsIds
        )

        return Budget(
            id = id,
            usedAmount = newCurrentAmount,
            amountLimit = newAmountLimit,
            usedPercentage = ".2f".format(100 / newAmountLimit * newCurrentAmount).toFloat(),
            category = category,
            name = name.trim(),
            repeatingPeriod = repeatingPeriod,
            currency = linkedAccounts.firstOrNull()?.currency ?: "",
            linkedAccountsIds = linkedAccounts.map { it.id }
        )
    }

}
