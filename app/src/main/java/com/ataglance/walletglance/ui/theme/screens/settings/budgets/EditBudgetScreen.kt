package com.ataglance.walletglance.ui.theme.screens.settings.budgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.accounts.color.AccountPossibleColors
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.date.RepeatingPeriod
import com.ataglance.walletglance.data.budgets.EditingBudgetUiState
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.utils.asLocalizedString
import com.ataglance.walletglance.data.utils.toAccountColorWithName
import com.ataglance.walletglance.ui.theme.screencontainers.SetupDataScreenContainer
import com.ataglance.walletglance.ui.theme.uielements.accounts.AccountNameWithCurrencyComposable
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.TwoStateCheckbox
import com.ataglance.walletglance.ui.theme.uielements.categories.CategoryField
import com.ataglance.walletglance.ui.theme.uielements.categories.CategoryPicker
import com.ataglance.walletglance.ui.theme.uielements.containers.PreviewContainer
import com.ataglance.walletglance.ui.theme.uielements.fields.FieldLabel
import com.ataglance.walletglance.ui.theme.uielements.fields.FieldWithLabel
import com.ataglance.walletglance.ui.theme.uielements.fields.TextFieldWithLabel
import com.ataglance.walletglance.ui.theme.uielements.pickers.CustomDatePicker
import com.ataglance.walletglance.ui.theme.uielements.pickers.PopupFloatingPicker
import com.ataglance.walletglance.ui.theme.uielements.switches.SwitchWithLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBudgetScreen(
    scaffoldPadding: PaddingValues,
    appTheme: AppTheme,
    budget: EditingBudgetUiState,
    accountList: List<Account>,
    categoriesWithSubcategories: CategoriesWithSubcategories,
    onNameChange: (String) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onAmountLimitChange: (String) -> Unit,
    onRepeatingPeriodChange: (RepeatingPeriod) -> Unit,
    onIncludeExistingRecordsChange: (Boolean) -> Unit,
    onLinkAccount: () -> Unit,
    onUnlinkAccount: () -> Unit,
    onSaveButton: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showCategoryPicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.dateTimeState.calendar.timeInMillis
    )

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        SetupDataScreenContainer(
            topPadding = scaffoldPadding.calculateTopPadding(),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    appTheme = appTheme,
                    budget = budget,
                    accountList = accountList,
                    onNameChange = onNameChange,
                    onCategoryFieldClick = { showCategoryPicker = true },
                    onAmountLimitChange = onAmountLimitChange,
                    onRepeatingPeriodChange = onRepeatingPeriodChange,
                    onIncludeExistingRecordsChange = onIncludeExistingRecordsChange,
                    onLinkAccount = onLinkAccount,
                    onUnlinkAccount = onUnlinkAccount
                )
            },
            primaryBottomButton = {
                PrimaryButton(
                    text = stringResource(if (budget.isNew) R.string.create else R.string.save),
                    onClick = onSaveButton
                )
            }
        )
        CustomDatePicker(
            openDialog = showDatePicker,
            onOpenDateDialogChange = { showDatePicker = it },
            onConfirmButton = {
                datePickerState.selectedDateMillis?.let { viewModel.selectNewDate(it) }
                showDatePicker = false
            },
            state = datePickerState
        )
        CategoryPicker(
            visible = showCategoryPicker,
            categoriesWithSubcategories = categoriesWithSubcategories,
            type = CategoryType.Expense,
            appTheme = appTheme,
            allowChoosingParentCategory = true,
            onDismissRequest = { showCategoryPicker = false },
            onCategoryChoose = { onCategoryChange(it.getSubcategoryOrCategory()) }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    appTheme: AppTheme,
    budget: EditingBudgetUiState,
    accountList: List<Account>,
    onNameChange: (String) -> Unit,
    onCategoryFieldClick: () -> Unit,
    onAmountLimitChange: (String) -> Unit,
    onRepeatingPeriodChange: (RepeatingPeriod) -> Unit,
    onIncludeExistingRecordsChange: (Boolean) -> Unit,
    onLinkAccount: () -> Unit,
    onUnlinkAccount: () -> Unit
) {
    val context = LocalContext.current
    val scrollStateConnection = rememberNestedScrollInteropConnection()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .nestedScroll(scrollStateConnection)
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        if (budget.isNew) {
            SwitchWithLabel(
                checked = budget.includeExistingRecords,
                labelText = stringResource(R.string.include_existing_records_to_used_amount),
                onCheckedChange = onIncludeExistingRecordsChange
            )
        }
        FieldWithLabel(stringResource(R.string.repeating_period)) {
            PopupFloatingPicker(
                selectedItemText = budget.repeatingPeriod.asLocalizedString(context),
                itemList = listOf(
                    RepeatingPeriod.OneTime,
                    RepeatingPeriod.Daily,
                    RepeatingPeriod.Weekly,
                    RepeatingPeriod.Monthly,
                    RepeatingPeriod.Yearly,
                ),
                itemToString = { it.asLocalizedString(context) },
                onItemSelect = onRepeatingPeriodChange
            )
        }
        budget.category?.let { category ->
            AnimatedContent(
                targetState = category,
                label = "category field at the edit budget screen"
            ) { targetCategory ->
                FieldWithLabel(stringResource(R.string.category)) {
                    CategoryField(
                        category = targetCategory,
                        fontSize = 20.sp,
                        cornerSize = 15.dp,
                        appTheme = appTheme,
                        onClick = onCategoryFieldClick
                    )
                }
            }
        }
        TextFieldWithLabel(
            text = budget.name,
            onValueChange = onNameChange,
            labelText = stringResource(R.string.budget_name),
            placeholderText = stringResource(R.string.name)
        )
        TextFieldWithLabel(
            text = budget.amountLimit,
            placeholderText = "0.0",
            onValueChange = onAmountLimitChange,
            keyboardType = KeyboardType.Number,
            labelText = stringResource(R.string.budget_limit)
        )
        AccountCheckedList(
            budget = budget,
            accountList = accountList,
            appTheme = appTheme,
            onAccountCheck = onLinkAccount,
            onAccountUncheck = onUnlinkAccount
        )
    }
}

@Composable
private fun AccountCheckedList(
    budget: EditingBudgetUiState,
    accountList: List<Account>,
    appTheme: AppTheme?,
    onAccountCheck: () -> Unit,
    onAccountUncheck: () -> Unit,
) {
    val lazyListState = rememberLazyListState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FieldLabel(text = stringResource(R.string.accounts))
        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            userScrollEnabled = false
        ) {
            items(items = accountList, key = { it.id }) { account ->
                val enabled = budget.linkedAccounts.isEmpty() ||
                        account.currency == budget.linkedAccounts[0].currency

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TwoStateCheckbox(
                        checked = budget.linkedAccounts.find { it.id == account.id } != null,
                        enabled = enabled,
                        onClick = { isChecked ->
                            if (isChecked) onAccountCheck() else onAccountUncheck()
                        }
                    )
                    AccountNameWithCurrencyComposable(
                        account = account,
                        appTheme = appTheme,
                        enabled = enabled
                    )
                }
            }
        }
    }
}


@Preview(locale = "en")
@Composable
private fun EditBudgetScreenPreview() {
    val appTheme = AppTheme.LightDefault
    val accountList = listOf(
        Account(id = 1, color = AccountPossibleColors().pink.toAccountColorWithName(), name = "Main account"),
        Account(id = 2, color = AccountPossibleColors().blue.toAccountColorWithName()),
        Account(id = 3, color = AccountPossibleColors().default.toAccountColorWithName(), currency = "CZK"),
    )
    val categoriesWithSubcategories = DefaultCategoriesPackage(LocalContext.current)
        .getDefaultCategories()
    val budget = EditingBudgetUiState(
        isNew = true,
        usedAmount = "2500",
        amountLimit = "4000",
        usedPercentage = 0f,
        category = categoriesWithSubcategories.expense[0].category,
        linkedAccounts = accountList.subList(0, 1)
    )

    PreviewContainer(appTheme = appTheme) {
        EditBudgetScreen(
            scaffoldPadding = PaddingValues(0.dp),
            appTheme = appTheme,
            budget = budget,
            accountList = accountList,
            categoriesWithSubcategories = categoriesWithSubcategories,
            onNameChange = {},
            onCategoryChange = {},
            onAmountLimitChange = {},
            onRepeatingPeriodChange = {},
            onIncludeExistingRecordsChange = {},
            onLinkAccount = {},
            onUnlinkAccount = {},
            onSaveButton = {}
        )
    }
}