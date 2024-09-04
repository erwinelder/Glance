package com.ataglance.walletglance.recordCreation.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.presentation.components.CategoryTypeToggleButton
import com.ataglance.walletglance.core.presentation.components.buttons.ButtonWithPopupContent
import com.ataglance.walletglance.core.presentation.components.buttons.NavigationTextArrowButton
import com.ataglance.walletglance.core.presentation.components.buttons.TwoStateCheckboxWithText
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraftPreferences

@Composable
fun RecordCreationTopBar(
    showCategoryTypeButton: Boolean,
    currentCategoryType: CategoryType,
    onSelectCategoryType: (CategoryType) -> Unit,
    showTransferButton: Boolean,
    onNavigateToTransferCreationScreen: () -> Unit,
    preferences: RecordDraftPreferences,
    onIncludeInBudgetsChange: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
            if (showCategoryTypeButton) {
                CategoryTypeToggleButton(
                    currentType = currentCategoryType,
                    expenseTextRes = R.string.expense,
                    incomeTextRes = R.string.income_singular,
                    onClick = onSelectCategoryType
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            PreferencesButton(
                animationTransformOrigin = if (showCategoryTypeButton) {
                    TransformOrigin(.7f, 0f)
                } else {
                    TransformOrigin(.5f, 0f)
                },
                showTransferButton = showTransferButton,
                onNavigateToTransferCreationScreen = onNavigateToTransferCreationScreen,
                preferences = preferences,
                onIncludeInBudgetsChange = onIncludeInBudgetsChange
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
        }
    }
}

@Composable
private fun PreferencesButton(
    animationTransformOrigin: TransformOrigin,
    showTransferButton: Boolean,
    onNavigateToTransferCreationScreen: () -> Unit,
    preferences: RecordDraftPreferences,
    onIncludeInBudgetsChange: (Boolean) -> Unit,
) {
    ButtonWithPopupContent(
        buttonText = stringResource(R.string.preferences),
        contentPadding = PaddingValues(
            start = 24.dp, end = 24.dp,
            top = if (showTransferButton) 4.dp else 20.dp, bottom = 20.dp
        ),
        animationTransformOrigin = animationTransformOrigin
    ) { onDismissRequest ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (showTransferButton) {
                NavigationTextArrowButton(
                    text = stringResource(R.string.make_transfer),
                    fontSize = 18.sp,
                    onClick = {
                        onDismissRequest()
                        onNavigateToTransferCreationScreen()
                    }
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TwoStateCheckboxWithText(
                    text = stringResource(R.string.include_in_budgets),
                    checked = preferences.includeInBudgets,
                    size = 24.dp,
                    onClick = onIncludeInBudgetsChange
                )
            }
        }
    }
}
