package com.ataglance.walletglance.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ataglance.walletglance.data.accounts.AccountColorName
import com.ataglance.walletglance.ui.viewmodels.accounts.EditAccountUiState
import com.ataglance.walletglance.data.records.RecordType
import java.util.Locale

@Entity(tableName = "Account")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val orderNum: Int = 0,
    val name: String = "USD",
    val currency: String = "USD",
    val balance: Double = 0.0,
    val color: String = AccountColorName.Default.name,
    val hide: Boolean = false,
    val hideBalance: Boolean = false,
    val withoutBalance: Boolean = false,
    val isActive: Boolean = true
) {

    fun getFormattedBalanceWithSpaces(): String {
        if (hideBalance || withoutBalance) {
            return "***"
        }

        var numberString = "%.2f".format(Locale.US, balance)
        var formattedNumber = numberString.let {
            it.substring(startIndex = it.length - 3)
        }
        numberString = numberString.let {
            it.substring(0, it.length - 3)
        }
        var digitCount = 0

        for (i in numberString.length - 1 downTo 0) {
            formattedNumber = numberString[i] + formattedNumber
            digitCount++
            if (digitCount % 3 == 0 && i != 0) {
                formattedNumber = " $formattedNumber"
            }
        }

        return formattedNumber
    }

    fun getFormattedBalanceWithSpacesWithCurrency(): String {
        if (hideBalance || withoutBalance) {
            return "***"
        }

        return getFormattedBalanceWithSpaces() + " $currency"
    }

    fun getFormattedBalanceBeforeDecimalSeparator(): String {
        if (hideBalance) {
            return "***"
        } else if (withoutBalance) {
            return ""
        }

        return getFormattedBalanceWithSpaces().let {
            it.substring(0, it.length - 3)
        }
    }

    fun getFormattedBalanceAfterDecimalSeparator(): String {
        if (hideBalance || withoutBalance) {
            return ""
        }

        return getFormattedBalanceWithSpaces().let {
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

    fun toEditAccountUiState(): EditAccountUiState {
        return EditAccountUiState(
            id = id,
            orderNum = orderNum,
            name = name,
            currency = currency,
            balance = balance.toString(),
            colorName = color,
            hide = hide,
            hideBalance = hideBalance,
            withoutBalance = withoutBalance,
            isActive = isActive
        )
    }

}