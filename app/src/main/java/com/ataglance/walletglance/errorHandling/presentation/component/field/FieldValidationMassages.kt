package com.ataglance.walletglance.errorHandling.presentation.component.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.errorHandling.presentation.model.ValidationState

@Composable
fun FieldValidationMessages(validationStates: List<ValidationState>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        validationStates.forEach { validationState ->
            FieldValidationMessage(state = validationState)
        }
    }
}