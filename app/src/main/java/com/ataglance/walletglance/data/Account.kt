package com.ataglance.walletglance.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ataglance.walletglance.model.AccountColorName

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

        var numberString = "%.2f".format(balance)
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

    fun getFormattedBalanceBeforeDecimalSeparatorOrHiddenBalance(): String {
        if (hideBalance || withoutBalance) {
            return "***"
        }

        return getFormattedBalanceWithSpaces().let {
            it.substring(0, it.length - 3)
        }
    }

    fun getFormattedBalanceAfterDecimalSeparatorOrEmptyString(): String {
        if (hideBalance || withoutBalance) {
            return ""
        }

        return getFormattedBalanceWithSpaces().let {
            it.substring(startIndex = it.length - 3)
        }
    }

}