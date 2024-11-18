package com.ataglance.walletglance.errorHandling.domain.model

import androidx.compose.runtime.Stable

@Stable
data class FieldWithValidationState(
    val fieldText: String = "",
    val validationStates: List<FieldValidationState> = emptyList()
) {

    fun isValid(): Boolean {
        return validationStates.none { !it.isValid }
    }

}
