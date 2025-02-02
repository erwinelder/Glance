package com.ataglance.walletglance.budget.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.presentation.components.AccountNameWithCurrencyComposable
import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.model.BudgetEntity
import com.ataglance.walletglance.budget.domain.model.EditingBudgetUiState
import com.ataglance.walletglance.budget.mapper.toBudget
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategory
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.components.CategoryField
import com.ataglance.walletglance.category.presentation.components.CategoryPicker
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.checkboxes.TwoStateCheckbox
import com.ataglance.walletglance.core.presentation.components.fields.FieldLabel
import com.ataglance.walletglance.core.presentation.components.fields.FieldWithLabel
import com.ataglance.walletglance.core.presentation.components.fields.TextFieldWithLabel
import com.ataglance.walletglance.core.presentation.components.pickers.PopupFloatingPicker
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.utils.asStringRes
import com.ataglance.walletglance.core.utils.letIfNoneIsNull
import com.ataglance.walletglance.core.utils.takeComposableIf

@Composable
fun EditBudgetScreen(
    scaffoldPadding: PaddingValues,
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
        GlassSurfaceScreenContainer(
            topPadding = scaffoldPadding.calculateTopPadding(),
            fillGlassSurface = false,
            topButton = takeComposableIf(!budget.isNew) {
                SecondaryButton(
                    text = stringResource(R.string.delete),
                    onClick = onDeleteButton
                )
            },
            glassSurfaceContent = {
                GlassSurfaceContent(
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
            allowChoosingParentCategory = true,
            onDismissRequest = { showCategoryPicker = false },
            onCategoryChoose = { onCategoryChange(it) }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
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

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
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
        }
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
        budget.category?.let { category ->
            item {
                FieldWithLabel(stringResource(R.string.category)) {
                    AnimatedContent(
                        targetState = category,
                        label = "category field at the edit budget screen"
                    ) { targetCategory ->
                        CategoryField(
                            category = targetCategory,
                            onClick = onCategoryFieldClick
                        )
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
        item {
            TextFieldWithLabel(
                text = budget.name,
                onValueChange = onNameChange,
                labelText = stringResource(R.string.budget_name),
                placeholderText = stringResource(R.string.name)
            )
        }
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
        item {
            TextFieldWithLabel(
                text = budget.amountLimit,
                onValueChange = onAmountLimitChange,
                keyboardType = KeyboardType.Number,
                labelText = stringResource(R.string.budget_limit),
                placeholderText = "0.00"
            )
        }
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
        accountCheckedList(
            budget = budget,
            accountList = accountList,
            onAccountCheck = onLinkAccount,
            onAccountUncheck = onUnlinkAccount
        )
    }
}

private fun LazyListScope.accountCheckedList(
    budget: EditingBudgetUiState,
    accountList: List<Account>,
    onAccountCheck: (Account) -> Unit,
    onAccountUncheck: (Account) -> Unit,
) {
    item {
        FieldLabel(text = stringResource(R.string.accounts))
    }
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
                fontSize = 19.sp,
                enabled = enabled
            )
        }
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditBudgetScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    categoriesWithSubcategories: CategoriesWithSubcategories = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    accountList: List<Account> = listOf(
        Account(id = 1, color = AccountColors.Pink, name = "Main account"),
        Account(id = 2, color = AccountColors.Blue),
        Account(id = 3, color = AccountColors.Default, currency = "CZK"),
    ),
    budgetEntity: BudgetEntity? = null,
    budgetAccountAssociationList: List<BudgetAccountAssociation>? = null,
    budgetUiState: EditingBudgetUiState = (budgetEntity to budgetAccountAssociationList)
        .letIfNoneIsNull { (entity, associations) ->
            entity.toBudget(
                categoryWithSubcategory = categoriesWithSubcategories.expense[0].getWithFirstSubcategory(),
                linkedAccountsIds = associations
                    .filter { it.budgetId == entity.id }
                    .map { it.accountId },
                accountList = accountList
            )?.toBudgetUiState(accountList)
        } ?: EditingBudgetUiState(
            isNew = true,
            amountLimit = "4000",
            category = categoriesWithSubcategories.expense[0].category,
            name = "Food & drinks",
            linkedAccounts = accountList.subList(0, 1)
        )
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        EditBudgetScreen(
            scaffoldPadding = scaffoldPadding,
            budget = budgetUiState,
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