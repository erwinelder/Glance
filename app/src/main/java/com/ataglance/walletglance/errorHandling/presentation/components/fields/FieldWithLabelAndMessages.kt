package com.ataglance.walletglance.errorHandling.presentation.components.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.components.fields.FieldLabel
import com.ataglance.walletglance.errorHandling.domain.model.FieldWithValidationState

@Composable
fun FieldWithLabelAndMessages(
    labelText: String,
    state: FieldWithValidationState,
    field: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FieldLabel(labelText)
        field()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            state.validationStates.forEach { validationState ->
                FieldMsg(validationState)
            }
        }
    }
}