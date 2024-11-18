package com.ataglance.walletglance.core.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.ataglance.walletglance.R
import com.ataglance.walletglance.errorHandling.domain.model.FieldValidationState
import java.util.Locale
import kotlin.enums.enumEntries


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


fun String.isValidEmail(): Boolean {
    return Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(this)
}

fun String.validateEmail(): List<FieldValidationState> {
    val validationStates = mutableListOf<FieldValidationState>()

    if (this.isValidEmail()) {
        validationStates.add(
            FieldValidationState(isValid = true, messageRes = R.string.is_valid)
        )
    } else {
        validationStates.add(
            FieldValidationState(isValid = false, messageRes = R.string.not_valid_email)
        )
    }

    return validationStates
}

fun String.isValidPassword(): Boolean {
    return this.atLeastEightChars() &&
            this.atLeastOneUppercaseLetter() &&
            this.atLeastOneLowercaseLetter() &&
            this.atLeastOneDigit() &&
            this.atLeastOneSpecChar()
}

fun String.validatePassword(): List<FieldValidationState> {
    val validationStates = mutableListOf<FieldValidationState>()

    validationStates.add(
        FieldValidationState(
            isValid = this.atLeastEightChars(),
            messageRes = R.string.at_least_8_chars
        )
    )
    validationStates.add(
        FieldValidationState(
            isValid = this.atLeastOneUppercaseLetter(),
            messageRes = R.string.at_least_1_uppercase_letter
        )
    )
    validationStates.add(
        FieldValidationState(
            isValid = this.atLeastOneLowercaseLetter(),
            messageRes = R.string.at_least_1_lowercase_letter
        )
    )
    validationStates.add(
        FieldValidationState(
            isValid = this.atLeastOneDigit(),
            messageRes = R.string.at_least_1_digit
        )
    )
    validationStates.add(
        FieldValidationState(
            isValid = this.atLeastOneSpecChar(),
            messageRes = R.string.at_least_1_spec_char
        )
    )

    return validationStates
}

fun String.validateConfirmationPassword(password: String): List<FieldValidationState> {
    val passwordsMatch = this == password

    return listOf(
        FieldValidationState(
            isValid = passwordsMatch,
            messageRes = if (passwordsMatch) R.string.passwords_do_match else
                R.string.passwords_do_not_match
        )
    )
}

fun String.atLeastEightChars(): Boolean {
    return Regex("^.{8,}$").matches(this)
}

fun String.atLeastOneUppercaseLetter(): Boolean {
    return Regex(".*[A-Z].*").matches(this)
}

fun String.atLeastOneLowercaseLetter(): Boolean {
    return Regex(".*[a-z].*").matches(this)
}

fun String.atLeastOneDigit(): Boolean {
    return Regex(".*\\d.*").matches(this)
}

fun String.atLeastOneSpecChar(): Boolean {
    val specChars = "@\$!%*?&#_+-"
    return Regex(".*[$specChars].*").matches(this)
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
