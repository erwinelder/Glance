package com.ataglance.walletglance.core.domain.componentState

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
