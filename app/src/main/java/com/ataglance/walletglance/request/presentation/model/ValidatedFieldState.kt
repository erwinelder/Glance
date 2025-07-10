package com.ataglance.walletglance.request.presentation.model

import androidx.compose.runtime.Stable

@Stable
data class ValidatedFieldState(
    val fieldText: String = "",
    val validationStates: List<ValidationState> = emptyList()
) {

    val trimmedText: String
        get() = fieldText.trim()


    fun isValid(): Boolean {
        return validationStates.all { it.isValid }
    }

    fun isInvalid(): Boolean {
        return validationStates.any { !it.isValid }
    }

}