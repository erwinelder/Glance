package com.ataglance.walletglance.ui.utils

import java.util.Locale


inline fun <T> List<T>.deleteItemAndMoveOrderNum(
    predicate: (T) -> Boolean,
    transform: (T) -> T
): List<T> {
    val index = this.indexOfFirst(predicate)
    if (index == -1) return this

    return this.take(index) + this.drop(index + 1).map(transform)
}


fun String.addZeroIfDotIsAtTheBeginning(): String {
    return this.let { it.takeUnless { it.firstOrNull() == '.' } ?: ("0$it") }
}


fun Double.formatWithSpaces(): String {
    var numberString = "%.2f".format(Locale.US, this)
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
