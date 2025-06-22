package com.ataglance.walletglance.budget.presentation.screen

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.component.AccountNameWithCurrencyComposable
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.mapper.budget.toDomainModel
import com.ataglance.walletglance.budget.mapper.budget.toDraft
import com.ataglance.walletglance.budget.presentation.model.BudgetDraft
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetsViewModel
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.presentation.component.CategoryFieldWithLabelAnimated
import com.ataglance.walletglance.category.presentation.component.CategoryPicker
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.component.button.TertiaryButton
import com.ataglance.walletglance.core.presentation.component.checkbox.TwoStateCheckbox
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.field.FieldLabel
import com.ataglance.walletglance.core.presentation.component.field.FieldWithLabelWrapper
import com.ataglance.walletglance.core.presentation.component.field.SmallTextFieldWithLabel
import com.ataglance.walletglance.core.presentation.component.picker.PopupFloatingPicker
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButtonAndPrimaryButton
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.core.domain.date.asStringRes
import com.ataglance.walletglance.core.utils.letIfNoneIsNull
import com.ataglance.walletglance.core.utils.takeRowComposableIf
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory

@Composable
fun EditBudgetScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    backStack: NavBackStackEntry,
    navController: NavHostController
) {
    val budgetsViewModel = backStack.sharedKoinNavViewModel<EditBudgetsViewModel>(navController)
    val budgetViewModel = backStack.sharedKoinNavViewModel<EditBudgetViewModel>(navController)

    val budget by budgetViewModel.budget.collectAsStateWithLifecycle()

    EditBudgetScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        budget = budget,
        accountList = budgetViewModel.accounts,
        groupedCategoriesByType = budgetViewModel.groupedCategoriesByType,
        onNameChange = budgetViewModel::changeName,
        onCategoryChange = budgetViewModel::changeCategory,
        onAmountLimitChange = budgetViewModel::changeAmountLimit,
        onRepeatingPeriodChange = budgetViewModel::changeRepeatingPeriod,
        onLinkAccount = budgetViewModel::linkWithAccount,
        onUnlinkAccount = budgetViewModel::unlinkWithAccount,
        onDeleteButton = {
            budgetsViewModel.deleteBudget(
                id = budget.id, repeatingPeriod = budget.currRepeatingPeriod
            )
            navController.popBackStack()
        },
        onSaveButton = {
            budgetsViewModel.applyBudget(budgetDraft = budgetViewModel.getBudgetDraft())
            navController.popBackStack()
        }
    )
}

@Composable
fun EditBudgetScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    budget: BudgetDraft,
    accountList: List<Account>,
    groupedCategoriesByType: GroupedCategoriesByType,
    onNameChange: (String) -> Unit,
    onCategoryChange: (CategoryWithSub) -> Unit,
    onAmountLimitChange: (String) -> Unit,
    onRepeatingPeriodChange: (RepeatingPeriod) -> Unit,
    onLinkAccount: (Account) -> Unit,
    onUnlinkAccount: (Account) -> Unit,
    onDeleteButton: () -> Unit,
    onSaveButton: () -> Unit
) {
    val settingsCategory = SettingsCategory.Budgets(appTheme = CurrAppTheme)
    val backNavButtonText = budget.name.takeIf { it.isNotBlank() }
        ?: stringResource(R.string.budget)

    var showCategoryPicker by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenContainerWithTopBackNavButtonAndPrimaryButton(
            screenPadding = screenPadding,
            backNavButtonText = backNavButtonText,
            backNavButtonImageRes = settingsCategory.iconRes,
            onBackNavButtonClick = onNavigateBack,
            backNavButtonCompanionComponent = takeRowComposableIf(!budget.isNew) {
                TertiaryButton(
                    text = stringResource(R.string.delete),
                    iconRes = R.drawable.trash_icon,
                    onClick = onDeleteButton
                )
            },
            primaryButtonText = stringResource(if (budget.isNew) R.string.create else R.string.save),
            primaryButtonEnabled = budget.allowSaving(),
            onPrimaryButtonClick = onSaveButton
        ) {
            GlassSurface {
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
            }
        }
        CategoryPicker(
            visible = showCategoryPicker,
            groupedCategoriesByType = groupedCategoriesByType,
            type = CategoryType.Expense,
            allowChoosingParentCategory = true,
            onDismissRequest = { showCategoryPicker = false },
            onCategoryChoose = { onCategoryChange(it) }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    budget: BudgetDraft,
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
            FieldWithLabelWrapper(stringResource(R.string.repeating_period)) {
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
                CategoryFieldWithLabelAnimated(
                    category = category,
                    onClick = onCategoryFieldClick
                )
            }
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
        item {
            SmallTextFieldWithLabel(
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
            SmallTextFieldWithLabel(
                text = budget.amountLimit,
                onValueChange = onAmountLimitChange,
                labelText = stringResource(R.string.budget_limit),
                placeholderText = "0.00",
                keyboardType = KeyboardType.Number
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
    budget: BudgetDraft,
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
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    accountList: List<Account> = listOf(
        Account(id = 1, color = AccountColors.Pink, name = "Main account"),
        Account(id = 2, color = AccountColors.Blue),
        Account(id = 3, color = AccountColors.Default, currency = "CZK"),
    ),
    budgetEntity: BudgetEntity? = null,
    budgetAccountAssociationList: List<BudgetAccountAssociation>? = null,
    budgetUiState: BudgetDraft = (budgetEntity to budgetAccountAssociationList)
        .letIfNoneIsNull { (budget, associations) ->
            budget.toDomainModel(
                groupedCategoriesList = groupedCategoriesByType.expense,
                associations = associations,
                accounts = accountList
            )?.toDraft(accounts = accountList)
        } ?: BudgetDraft(
            isNew = true,
            amountLimit = "4000",
            category = groupedCategoriesByType.expense[0].category,
            name = "Food & drinks",
            linkedAccounts = accountList.subList(0, 1)
        )
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        EditBudgetScreen(
            screenPadding = scaffoldPadding,
            onNavigateBack = {},
            budget = budgetUiState,
            accountList = accountList,
            groupedCategoriesByType = groupedCategoriesByType,
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