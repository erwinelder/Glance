package com.ataglance.walletglance.data.utils

import java.util.Locale
import kotlin.math.pow


fun <A, B> Pair<A?, B?>.takeIfNoneIsNull(): Pair<A, B>? {
    val (first, second) = this
    return if (first != null && second != null) {
        first to second
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


fun String.addZeroIfDotIsAtTheBeginning(): String {
    return this.let { it.takeUnless { it.firstOrNull() == '.' } ?: ("0$it") }
}


fun Double.formatWithSpaces(): String {
    val numberString = "%.2f".format(Locale.US, this)
    var formattedNumber = numberString.takeLast(3)
    val decimalPart = numberString.dropLast(3)

    for ((index, char) in decimalPart.reversed().withIndex()) {

        formattedNumber = char + formattedNumber

        if (index % 3 == 2 && index != decimalPart.lastIndex) {
            formattedNumber = " $formattedNumber"
        }

    }

    return formattedNumber
}


fun Double.getColumnChartHorizontalLinesNames(horizontalLinesCount: Int): List<String> {
    val stepDecimal = this.toInt() / 4
    val stepDigitCountHalf = stepDecimal.toString().length / 2
    val multiplicationStep = 10.0.pow(stepDigitCountHalf.toDouble()).toInt()
    val roundedStep = stepDecimal / multiplicationStep * multiplicationStep + multiplicationStep

    val list = mutableListOf("0")
    for (i in 1..<horizontalLinesCount) {
        list.add((roundedStep * i).toString())
    }

    return list
}
