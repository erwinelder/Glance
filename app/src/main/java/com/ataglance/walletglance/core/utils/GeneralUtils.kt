package com.ataglance.walletglance.core.utils

import java.util.Locale


fun <A, B> Pair<A?, B?>.takeIfNoneIsNull(): Pair<A, B>? {
    val (first, second) = this
    return if (first != null && second != null) {
        first to second
    } else {
        null
    }
}


inline fun <A, B, R> Pair<A?, B?>.letIfNoneIsNull(block: (Pair<A, B>) -> R): R? {
    val (first, second) = this
    return if (first != null && second != null) {
        block(first to second)
    } else {
        null
    }
}


inline fun <T> List<T>.deleteItemAndMoveOrderNum(
    predicate: (T) -> Boolean,
    transform: (T) -> T
): List<T> {
    val index = this.indexOfFirst(predicate)
    if (index == -1) return this

    return this.take(index) + this.drop(index + 1).map(transform)
}


fun Int.formatWithSpaces(): String {
    val numberString = this.toString()
    var formattedNumber = ""

    for ((index, char) in numberString.reversed().withIndex()) {

        formattedNumber = char + formattedNumber

        if (index % 3 == 2 && index != numberString.lastIndex) {
            formattedNumber = " $formattedNumber"
        }

    }

    return formattedNumber
}


fun Double.formatWithSpaces(additionToEnd: String? = null): String {
    val numberString = "%.2f".format(Locale.US, this)
    var formattedNumber = numberString.takeLast(3)
    val decimalPart = numberString.dropLast(3)

    for ((index, char) in decimalPart.reversed().withIndex()) {

        formattedNumber = char + formattedNumber

        if (index % 3 == 2 && index != decimalPart.lastIndex) {
            formattedNumber = " $formattedNumber"
        }

    }

    return formattedNumber + (additionToEnd?.let { " $it" } ?: "")
}


fun String.addZeroIfDotIsAtTheBeginning(): String {
    return this.takeUnless { it.firstOrNull() == '.' } ?: "0$this"
}


fun String.isNumberWithDecimalOptionalNegative(): Boolean {
    return Regex("^-?(?:\\d{1,10}(?:\\.\\d{0,2})?)?\$").matches(this)
}
fun String.isPositiveNumberWithDecimal(): Boolean {
    return Regex("^(?:[0-9]\\d{0,9}(?:[.]\\d{0,2})?)?\$").matches(this)
}
fun String.isNumberWithDecimalOptionalDot(): Boolean {
    return Regex("^(?:\\d{1,10}(?:\\.\\d{0,2})?|\\.(?:\\d{1,2})?)?\$").matches(this)
}
