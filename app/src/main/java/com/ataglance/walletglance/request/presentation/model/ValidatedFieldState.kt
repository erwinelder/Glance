package com.ataglance.walletglance.request.presentation.model

import androidx.compose.runtime.Stable

@Stable
data class ValidatedFieldState(
    val fieldText: String = "",
    val validationStates: List<ValidationState> = emptyList()
) {

    fun isValid(): Boolean {
        return validationStates.all { it.isValid }
    }

    fun getTrimmedText(): String {
        return fieldText.trim()
    }

}