package com.ataglance.walletglance.model

import java.util.Locale

data class RecordStack(
    val recordNum: Int,
    val date: Long,
    val type: Char,
    val accountId: Int,
    val totalAmount: Double,
    val stack: List<RecordStackUnit>
) {
    fun getFormattedAmountWithSpaces(currency: String?): String {
        var numberString = "%.2f".format(Locale.US, totalAmount)
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

        val sign = if (RecordController().recordTypeCharToRecordType(type) == RecordType.Expense)
            '-' else '+'
        return "%c %s %s".format(sign, formattedNumber, currency)
    }
}

data class RecordStackUnit(
    val id: Int = 0,
    val amount: Double,
    val quantity: Int?,
    val categoryId: Int,
    val subcategoryId: Int?,
    val note: String?
)