package com.ataglance.walletglance.record.presentation.component

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
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.presentation.component.AccountPopupPicker
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.presentation.component.CategoryTypeToggleButton
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.ButtonWithPopupContent
import com.ataglance.walletglance.core.presentation.component.checkbox.TwoStateCheckboxWithText
import com.ataglance.walletglance.core.presentation.component.field.DateField
import com.ataglance.walletglance.core.presentation.component.field.FieldWithLabelWrapper
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.record.presentation.model.RecordDraft
import com.ataglance.walletglance.record.presentation.model.RecordDraftPreferences

@Composable
fun RecordCreationTopBar(
    recordDraft: RecordDraft,
    accounts: List<Account>,

    onSelectCategoryType: (CategoryType) -> Unit,
    onIncludeInBudgetsChange: (Boolean) -> Unit,
    onDateFieldClick: () -> Unit,
    onToggleAccounts: () -> Unit,
    onSelectAccount: (Account) -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
                if (recordDraft.isNew) {
                    CategoryTypeToggleButton(
                        currentType = recordDraft.type,
                        expenseTextRes = R.string.expense,
                        incomeTextRes = R.string.income_singular,
                        onClick = onSelectCategoryType
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                RecordPreferencesButton(
                    animationTransformOrigin = TransformOrigin(.5f, 0f),
                    showIncludeInBudgetsButton = recordDraft.type == CategoryType.Expense,
                    preferences = recordDraft.preferences,
                    onIncludeInBudgetsChange = onIncludeInBudgetsChange
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
            }
            DateField(
                formattedDate = recordDraft.dateTimeState.dateFormatted,
                onClick = onDateFieldClick
            )
            FieldWithLabelWrapper(labelText = stringResource(R.string.account)) {
                AccountPopupPicker(
                    accountList = accounts,
                    selectedAccount = recordDraft.account,
                    onToggleAccounts = onToggleAccounts,
                    onSelectAccount = onSelectAccount,
                    onDimBackgroundChange = onDimBackgroundChange
                )
            }
        }
    }
}

@Composable
private fun RecordPreferencesButton(
    animationTransformOrigin: TransformOrigin,
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
            showIncludeInBudgetsButton = showIncludeInBudgetsButton,
            preferences = preferences,
            onIncludeInBudgetsChange = onIncludeInBudgetsChange
        )
    }
}

@Composable
private fun RecordPreferencesWindowContent(
    showIncludeInBudgetsButton: Boolean,
    preferences: RecordDraftPreferences,
    onIncludeInBudgetsChange: (Boolean) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showIncludeInBudgetsButton) {
            TwoStateCheckboxWithText(
                text = stringResource(R.string.include_in_budgets),
                checked = preferences.includeInBudgets,
                checkboxSize = 24.dp,
                onClick = onIncludeInBudgetsChange,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        if (!showIncludeInBudgetsButton) {
            Text(
                text = stringResource(R.string.no_preferences),
                color = GlanciColors.onSurface.copy(.6f),
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(100.dp)
        ) {
            RecordCreationTopBar(
                recordDraft = RecordDraft(
                    recordId = 0,
                    type = CategoryType.Expense,
                    account = Account(name = "Account", balance = 1000.0, currency = "USD"),
                ),
                accounts = emptyList(),

                onSelectCategoryType = {},
                onIncludeInBudgetsChange = {},
                onDateFieldClick = {},
                onToggleAccounts = {},
                onSelectAccount = {},
                onDimBackgroundChange = {}
            )
            Box(
                modifier = Modifier
                    .border(1.dp, GlanciColors.onSurface.copy(.6f))
                    .padding(horizontal = 24.dp)
            ) {
                RecordPreferencesWindowContent(
                    showIncludeInBudgetsButton = true,
                    preferences = RecordDraftPreferences(includeInBudgets = true),
                    onIncludeInBudgetsChange = {}
                )
            }
        }
    }
}