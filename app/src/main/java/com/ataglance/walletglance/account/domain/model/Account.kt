package com.ataglance.walletglance.account.domain.model

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.core.utils.roundToTwoDecimals

@Stable
data class Account(
    val id: Int = 0,
    val orderNum: Int = 0,
    val name: String = "USD",
    val currency: String = "USD",
    val balance: Double = 0.0,
    val color: AccountColors = AccountColors.Default,
    val hide: Boolean = false,
    val hideBalance: Boolean = false,
    val withoutBalance: Boolean = false,
    val isActive: Boolean = false
) {

    private fun getHiddenBalance(): String? {
        return if (hideBalance) {
            "***"
        } else if (withoutBalance) {
            ""
        } else {
            null
        }
    }

    fun getFormattedBalance(): String {
        return getHiddenBalance() ?: balance.formatWithSpaces()
    }

    fun getFormattedBalanceWithCurrency(): String {
        return getHiddenBalance() ?: (getFormattedBalance() + " $currency")
    }

    fun getFormattedBalanceBeforeDecimalSeparator(): String {
        return getHiddenBalance() ?: getFormattedBalance().let {
            it.substring(0, it.length - 3)
        }
    }

    fun getFormattedBalanceAfterDecimalSeparator(): String {
        return if (hideBalance || withoutBalance) ""
        else getFormattedBalance().let {
            it.substring(startIndex = it.length - 3)
        }
    }

    fun addToBalance(amount: Double): Account = copy(
        balance = (balance + amount).roundToTwoDecimals()
    )

    fun subtractFromBalance(amount: Double): Account = copy(
        balance = (balance - amount).roundToTwoDecimals()
    )

    fun applyTransaction(amount: Double, type: CategoryType): Account {
        return when (type) {
            CategoryType.Expense -> subtractFromBalance(amount)
            CategoryType.Income -> addToBalance(amount)
        }
    }

    fun rollbackTransaction(amount: Double, type: CategoryType): Account {
        return when (type) {
            CategoryType.Expense -> addToBalance(amount)
            CategoryType.Income -> subtractFromBalance(amount)
        }
    }

    fun reapplyTransaction(
        prevAmount: Double,
        newAmount: Double,
        type: CategoryType
    ): Account {
        val balance = balance +
                (if (type == CategoryType.Expense) prevAmount else -prevAmount) +
                if (type == CategoryType.Expense) -newAmount else newAmount

        return copy(balance = balance.roundToTwoDecimals())
    }

}
