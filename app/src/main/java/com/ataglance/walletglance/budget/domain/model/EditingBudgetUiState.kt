package com.ataglance.walletglance.budget.domain.model

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.utils.getCurrentTimeAsGraphPercentageInThisRange
import com.ataglance.walletglance.core.utils.getLongDateRangeWithTime

@Stable
data class EditingBudgetUiState(
    val isNew: Boolean = true,
    val id: Int = 0,
    val priorityNum: Double = 0.0,
    val amountLimit: String = "",
    val category: Category? = null,
    val name: String = "",
    val currRepeatingPeriod: RepeatingPeriod = RepeatingPeriod.Monthly,
    val newRepeatingPeriod: RepeatingPeriod = RepeatingPeriod.Monthly,
    val linkedAccounts: List<Account> = emptyList()
) {

    fun allowSaving(): Boolean {
        val newAmountLimit = amountLimit.toDoubleOrNull() ?: return false

        return name.isNotBlank()
                && newAmountLimit > 0.0
                && category != null
                && linkedAccounts.isNotEmpty()
    }

    fun toNewBudget(): Budget? {
        val newAmountLimit = amountLimit.toDoubleOrNull() ?: return null
        val dateRange = newRepeatingPeriod.getLongDateRangeWithTime()

        return Budget(
            id = id,
            priorityNum = priorityNum,
            amountLimit = newAmountLimit,
            usedAmount = 0.0,
            usedPercentage = 0F,
            category = category,
            name = name,
            repeatingPeriod = newRepeatingPeriod,
            dateRange = dateRange,
            currentTimeWithinRangeGraphPercentage = dateRange
                .getCurrentTimeAsGraphPercentageInThisRange(),
            currency = linkedAccounts.firstOrNull()?.currency ?: "",
            linkedAccountsIds = linkedAccounts.map { it.id }
        )
    }

    fun copyDataToBudget(budget: Budget): Budget {
        val newAmountLimit = amountLimit.toDoubleOrNull() ?: return budget

        return budget.copy(
            amountLimit = newAmountLimit,
            category = category,
            name = name,
            repeatingPeriod = newRepeatingPeriod,
            currency = linkedAccounts.firstOrNull()?.currency ?: "",
            linkedAccountsIds = linkedAccounts.map { it.id }
        )
    }

}
