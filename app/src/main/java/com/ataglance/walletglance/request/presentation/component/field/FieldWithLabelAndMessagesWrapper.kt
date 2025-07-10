package com.ataglance.walletglance.request.presentation.component.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.component.field.FieldLabel
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState

@Composable
fun FieldWithLabelAndMessagesWrapper(
    state: ValidatedFieldState,
    labelText: String? = null,
    field: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        labelText?.let { FieldLabel(text = it) }
        field()
        FieldValidationMessages(validationStates = state.validationStates)
    }
}