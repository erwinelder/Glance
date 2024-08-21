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
import com.ataglance.walletglance.data.budgets.EditingBudgetUiState
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.CategoryWithSubcategory
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.date.RepeatingPeriod
import com.ataglance.walletglance.data.utils.asStringRes
import com.ataglance.walletglance.data.utils.toAccountColorWithName
import com.ataglance.walletglance.ui.theme.screencontainers.SetupDataScreenContainer
import com.ataglance.walletglance.ui.theme.uielements.accounts.AccountNameWithCurrencyComposable
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.SecondaryButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.TwoStateCheckbox
import com.ataglance.walletglance.ui.theme.uielements.categories.CategoryField
import com.ataglance.walletglance.ui.theme.uielements.categories.CategoryPicker
import com.ataglance.walletglance.ui.theme.uielements.containers.PreviewContainer
import com.ataglance.walletglance.ui.theme.uielements.fields.FieldLabel
import com.ataglance.walletglance.ui.theme.uielements.fields.FieldWithLabel
import com.ataglance.walletglance.ui.theme.uielements.fields.TextFieldWithLabel
import com.ataglance.walletglance.ui.theme.uielements.pickers.PopupFloatingPicker

@Composable
fun EditBudgetScreen(
    scaffoldPadding: PaddingValues,
    appTheme: AppTheme?,
    budget: EditingBudgetUiState,
    accountList: List<Account>,
    categoriesWithSubcategories: CategoriesWithSubcategories,
    onNameChange: (String) -> Unit,
    onCategoryChange: (CategoryWithSubcategory) -> Unit,
    onAmountLimitChange: (String) -> Unit,
    onRepeatingPeriodChange: (RepeatingPeriod) -> Unit,
    onLinkAccount: (Account) -> Unit,
    onUnlinkAccount: (Account) -> Unit,
    onDeleteButton: () -> Unit,
    onSaveButton: () -> Unit
) {
    var showCategoryPicker by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        SetupDataScreenContainer(
            topPadding = scaffoldPadding.calculateTopPadding(),
            fillGlassSurface = false,
            topButton = if (!budget.isNew) { {
                SecondaryButton(
                    text = stringResource(R.string.delete),
                    onClick = onDeleteButton
                )
            } } else null,
            glassSurfaceContent = {
                GlassSurfaceContent(
                    appTheme = appTheme,
                    budget = budget,
                    accountList = accountList,
                    onNameChange = onNameChange,
                    onCategoryFieldClick = { showCategoryPicker = true },
                    onAmountLimitChange = onAmountLimitChange,
                    onRepeatingPeriodChange = onRepeatingPeriodChange,
                    onLinkAccount = onLinkAccount,
                    onUnlinkAccount = onUnlinkAccount
                )
            },
            primaryBottomButton = {
                PrimaryButton(
                    text = stringResource(if (budget.isNew) R.string.create else R.string.save),
                    enabled = budget.allowSaving(),
                    onClick = onSaveButton
                )
            }
        )
        CategoryPicker(
            visible = showCategoryPicker,
            categoriesWithSubcategories = categoriesWithSubcategories,
            type = CategoryType.Expense,
            appTheme = appTheme,
            allowChoosingParentCategory = true,
            onDismissRequest = { showCategoryPicker = false },
            onCategoryChoose = { onCategoryChange(it) }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    appTheme: AppTheme?,
    budget: EditingBudgetUiState,
    accountList: List<Account>,
    onNameChange: (String) -> Unit,
    onCategoryFieldClick: () -> Unit,
    onAmountLimitChange: (String) -> Unit,
    onRepeatingPeriodChange: (RepeatingPeriod) -> Unit,
    onLinkAccount: (Account) -> Unit,
    onUnlinkAccount: (Account) -> Unit
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
        FieldWithLabel(stringResource(R.string.repeating_period)) {
            PopupFloatingPicker(
                selectedItemText = stringResource(budget.newRepeatingPeriod.asStringRes()),
                itemList = listOf(
                    RepeatingPeriod.Daily,
                    RepeatingPeriod.Weekly,
                    RepeatingPeriod.Monthly,
                    RepeatingPeriod.Yearly,
                ),
                itemToString = { context.getString(it.asStringRes()) },
                onItemSelect = onRepeatingPeriodChange
            )
        }
        budget.category?.let { category ->
            FieldWithLabel(stringResource(R.string.category)) {
                AnimatedContent(
                    targetState = category,
                    label = "category field at the edit budget screen"
                ) { targetCategory ->
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
            onValueChange = onAmountLimitChange,
            keyboardType = KeyboardType.Number,
            labelText = stringResource(R.string.budget_limit),
            placeholderText = "0.0"
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
    onAccountCheck: (Account) -> Unit,
    onAccountUncheck: (Account) -> Unit,
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
                            if (isChecked) onAccountCheck(account) else onAccountUncheck(account)
                        }
                    )
                    AccountNameWithCurrencyComposable(
                        account = account,
                        appTheme = appTheme,
                        fontSize = 19.sp,
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
    val categoriesWithSubcategories = DefaultCategoriesPackage(LocalContext.current).getDefaultCategories()
    val budget = EditingBudgetUiState(
        isNew = true,
        amountLimit = "4000",
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
            onLinkAccount = {},
            onUnlinkAccount = {},
            onDeleteButton = {},
            onSaveButton = {}
        )
    }
}