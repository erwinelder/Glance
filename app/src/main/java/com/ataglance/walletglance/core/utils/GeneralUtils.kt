package com.ataglance.walletglance.core.utils

import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.ataglance.walletglance.R
import java.util.Locale
import kotlin.enums.enumEntries


fun takeActionIf(condition: Boolean, action: () -> Unit): (() -> Unit)? {
    return if (condition) {
        action
    } else {
        null
    }
}

fun <T, R> takeActionIf(condition: Boolean, action: (T) -> R): ((T) -> R)? {
    return if (condition) {
        action
    } else {
        null
    }
}

fun takeComposableIf(
    condition: Boolean,
    composable: @Composable () -> Unit
): (@Composable () -> Unit)? {
    return if (condition) {
        composable
    } else {
        null
    }
}

fun <T> takeComposableIfNotNull(
    nullableItem: T?,
    composableGetter: @Composable (T) -> Unit
): (@Composable () -> Unit)? {
    return if (nullableItem != null) {
        {
            composableGetter(nullableItem)
        }
    } else {
        null
    }
}


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


fun <T> List<T>.moveItems(fromIndex: Int, toIndex: Int): List<T> {
    if (fromIndex == toIndex) return this

    return this.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
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


inline fun <reified T : Enum<T>> enumValueOrNull(name: String): T? {
    return enumEntries<T>().find { it.name == name }
}



operator fun PaddingValues.plus(paddingValues: PaddingValues): PaddingValues {
    return PaddingValues(
        start = this.start + paddingValues.start,
        top = this.top + paddingValues.top,
        end = this.end + paddingValues.end,
        bottom = this.bottom + paddingValues.bottom
    )
}

val PaddingValues.start: Dp
    get() = calculateStartPadding(LayoutDirection.Ltr)

val PaddingValues.end: Dp
    get() = calculateEndPadding(LayoutDirection.Ltr)

val PaddingValues.top: Dp
    get() = calculateTopPadding()

val PaddingValues.bottom: Dp
    get() = calculateBottomPadding()



fun Uri.extractOobCode(): String? {
    return getQueryParameter("oobCode")?.takeIf { it.isNotEmpty() }
}
