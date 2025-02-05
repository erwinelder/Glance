package com.ataglance.walletglance.budget.presentation.model

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod

@Stable
data class BudgetDraft(
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

}
