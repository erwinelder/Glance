package com.ataglance.walletglance.ui.utils


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