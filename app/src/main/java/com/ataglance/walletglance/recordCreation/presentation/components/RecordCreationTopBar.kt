package com.ataglance.walletglance.recordCreation.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.presentation.components.CategoryTypeToggleButton
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.buttons.ButtonWithPopupContent
import com.ataglance.walletglance.core.presentation.components.buttons.NavigationTextArrowButton
import com.ataglance.walletglance.core.presentation.components.checkboxes.TwoStateCheckboxWithText
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
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
            RecordPreferencesButton(
                animationTransformOrigin = TransformOrigin(.5f, 0f),
                showTransferButton = showTransferButton,
                onNavigateToTransferCreationScreen = onNavigateToTransferCreationScreen,
                showIncludeInBudgetsButton = currentCategoryType == CategoryType.Expense,
                preferences = preferences,
                onIncludeInBudgetsChange = onIncludeInBudgetsChange
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
        }
    }
}

@Composable
private fun RecordPreferencesButton(
    animationTransformOrigin: TransformOrigin,
    showTransferButton: Boolean,
    onNavigateToTransferCreationScreen: () -> Unit,
    showIncludeInBudgetsButton: Boolean,
    preferences: RecordDraftPreferences,
    onIncludeInBudgetsChange: (Boolean) -> Unit
) {
    ButtonWithPopupContent(
        buttonText = stringResource(R.string.preferences),
        contentPadding = PaddingValues(horizontal = 24.dp),
        animationTransformOrigin = animationTransformOrigin
    ) { onDismissRequest ->
        RecordPreferencesWindowContent(
            onDismissRequest = onDismissRequest,
            showTransferButton = showTransferButton,
            onNavigateToTransferCreationScreen = onNavigateToTransferCreationScreen,
            showIncludeInBudgetsButton = showIncludeInBudgetsButton,
            preferences = preferences,
            onIncludeInBudgetsChange = onIncludeInBudgetsChange
        )
    }
}

@Composable
private fun RecordPreferencesWindowContent(
    onDismissRequest: () -> Unit,
    showTransferButton: Boolean,
    onNavigateToTransferCreationScreen: () -> Unit,
    showIncludeInBudgetsButton: Boolean,
    preferences: RecordDraftPreferences,
    onIncludeInBudgetsChange: (Boolean) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showTransferButton) {
            NavigationTextArrowButton(
                text = stringResource(R.string.make_transfer),
                onClick = {
                    onDismissRequest()
                    onNavigateToTransferCreationScreen()
                }
            )
        }
        if (showIncludeInBudgetsButton) {
            TwoStateCheckboxWithText(
                text = stringResource(R.string.include_in_budgets),
                checked = preferences.includeInBudgets,
                checkboxSize = 24.dp,
                onClick = onIncludeInBudgetsChange,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        if (!showTransferButton && !showIncludeInBudgetsButton) {
            Text(
                text = stringResource(R.string.no_preferences),
                color = GlanceColors.onSurface.copy(.6f),
                fontSize = 16.sp,
                fontFamily = Manrope,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}


@Preview
@Composable
private fun RecordPreferencesWindowContentPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        Box(
            modifier = Modifier
                .border(1.dp, GlanceColors.onSurface.copy(.6f))
                .padding(horizontal = 24.dp)
        ) {
            RecordPreferencesWindowContent(
                onDismissRequest = {},
                showTransferButton = true,
                onNavigateToTransferCreationScreen = {},
                showIncludeInBudgetsButton = false,
                preferences = RecordDraftPreferences(includeInBudgets = true),
                onIncludeInBudgetsChange = {}
            )
        }
    }
}