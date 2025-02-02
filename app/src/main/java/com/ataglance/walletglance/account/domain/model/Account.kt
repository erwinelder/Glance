package com.ataglance.walletglance.account.domain.model

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.record.domain.RecordType
import java.util.Locale

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
    val isActive: Boolean = true
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

    fun cloneAndAddToBalance(amount: Double): Account {
        return this.copy(
            balance = "%.2f".format(Locale.US, balance + amount).toDouble()
        )
    }

    fun cloneAndSubtractFromBalance(amount: Double): Account {
        return this.copy(
            balance = "%.2f".format(Locale.US, balance - amount).toDouble()
        )
    }

    fun cloneAndAddToOrSubtractFromBalance(amount: Double, recordType: RecordType): Account {
        return this.copy(
            balance = "%.2f".format(
                Locale.US,
                balance + if (recordType == RecordType.Expense) -amount else amount
            ).toDouble()
        )
    }

    fun cloneAndReapplyAmountToBalance(
        prevAmount: Double,
        newAmount: Double,
        recordType: RecordType
    ): Account {
        return this.copy(
            balance = "%.2f".format(
                Locale.US,
                balance +
                        (if (recordType == RecordType.Expense) prevAmount else -prevAmount) +
                        if (recordType == RecordType.Expense) -newAmount else newAmount
            ).toDouble()
        )
    }

    fun toRecordAccount(): RecordAccount {
        return RecordAccount(
            id = id,
            name = name,
            currency = currency,
            color = color
        )
    }

    fun toEditAccountUiState(): EditAccountUiState {
        return EditAccountUiState(
            id = id,
            orderNum = orderNum,
            name = name,
            currency = currency,
            balance = "%.2f".format(Locale.US, balance),
            color = color,
            hide = hide,
            hideBalance = hideBalance,
            withoutBalance = withoutBalance,
            isActive = isActive
        )
    }

}
