package com.ataglance.walletglance.data.accounts

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.data.accounts.color.AccountColorWithName
import com.ataglance.walletglance.data.accounts.color.AccountColors
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.domain.utils.formatWithSpaces
import com.ataglance.walletglance.domain.utils.toAccountColorWithName
import java.util.Locale

@Stable
data class Account(
    val id: Int = 0,
    val orderNum: Int = 0,
    val name: String = "USD",
    val currency: String = "USD",
    val balance: Double = 0.0,
    val color: AccountColorWithName = AccountColors.Default.toAccountColorWithName(),
    val hide: Boolean = false,
    val hideBalance: Boolean = false,
    val withoutBalance: Boolean = false,
    val isActive: Boolean = true
) {

    fun toRecordAccount(): RecordAccount {
        return RecordAccount(
            id = id,
            name = name,
            currency = currency,
            color = color
        )
    }

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

}
