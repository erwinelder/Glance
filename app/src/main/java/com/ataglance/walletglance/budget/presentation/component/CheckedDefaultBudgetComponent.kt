package com.ataglance.walletglance.budget.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreenPreview
import com.ataglance.walletglance.core.presentation.component.checkbox.TwoStateCheckbox

@Composable
fun CheckedDefaultBudgetComponent(
    budget: Budget,
    modifier: Modifier = Modifier,
    checked: Boolean,
    checkedEnabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    BasicDefaultBudgetComponent(
        budget = budget,
        onClick = {
            onCheckedChange(!checked)
        },
        modifier = modifier,
        clickEnabled = checkedEnabled
    ) {
        TwoStateCheckbox(
            checked = checked,
            enabled = checkedEnabled,
            onClick = onCheckedChange
        )
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun CheckedDefaultBudgetComponentPreview() {
    EditBudgetsScreenPreview()
}