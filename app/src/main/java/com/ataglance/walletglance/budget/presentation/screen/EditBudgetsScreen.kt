package com.ataglance.walletglance.budget.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.domain.navigation.BudgetsSettingsScreens
import com.ataglance.walletglance.budget.domain.utils.groupByType
import com.ataglance.walletglance.budget.mapper.budget.toDomainModels
import com.ataglance.walletglance.budget.presentation.component.BudgetListsByPeriodComponent
import com.ataglance.walletglance.budget.presentation.component.DefaultBudgetComponent
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetsViewModel
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.component.button.SmallSecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.MessageContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButtonAndPrimaryButton
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.core.utils.getLongDateRangeWithTime
import com.ataglance.walletglance.core.utils.letIfNoneIsNull
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import kotlinx.coroutines.launch

@Composable
fun EditBudgetsScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    backStack: NavBackStackEntry,
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    isAppSetUp: Boolean
) {
    val budgetsViewModel = backStack.sharedKoinNavViewModel<EditBudgetsViewModel>(navController)
    val budgetViewModel = backStack.sharedKoinNavViewModel<EditBudgetViewModel>(navController)

    val budgetsByType by budgetsViewModel.budgetsByType.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    EditBudgetsScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        isAppSetUp = isAppSetUp,
        budgetsByType = budgetsByType,
        onNavigateToEditBudgetScreen = { budget: Budget? ->
            budgetViewModel.applyBudget(budget)
            navViewModel.navigateToScreen(navController, BudgetsSettingsScreens.EditBudget)
        },
        onSaveBudgetsButton = {
            coroutineScope.launch {
                budgetsViewModel.saveBudgets()
                if (isAppSetUp) {
                    navController.popBackStack()
                } else {
                    budgetsViewModel.preFinishSetup()
                }
            }
        }
    )
}

@Composable
fun EditBudgetsScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    isAppSetUp: Boolean,
    budgetsByType: BudgetsByType,
    onNavigateToEditBudgetScreen: (Budget?) -> Unit,
    onSaveBudgetsButton: () -> Unit,
) {
    val settingsCategory = SettingsCategory.Budgets(appTheme = CurrAppTheme)

    ScreenContainerWithTopBackNavButtonAndPrimaryButton(
        screenPadding = screenPadding,
        topBackNavButtonText = stringResource(settingsCategory.stringRes),
        topBackNavButtonImageRes = settingsCategory.iconRes,
        onTopBackNavButtonClick = onNavigateBack,
        primaryButtonText = stringResource(
            if (isAppSetUp) R.string.save
            else if (budgetsByType.areEmpty()) R.string.finish
            else R.string.save_and_finish
        ),
        onPrimaryButtonClick = onSaveBudgetsButton
    ) {

        GlassSurface(
            modifier = Modifier.weight(1f)
        ) {
            GlassSurfaceContent(
                budgetsByType = budgetsByType,
                onBudgetClick = onNavigateToEditBudgetScreen,
            )
        }

        SmallSecondaryButton(
            text = stringResource(R.string.add_budget),
            iconRes = R.drawable.add_icon,
        ) {
            onNavigateToEditBudgetScreen(null)
        }

    }
}

@Composable
private fun GlassSurfaceContent(
    budgetsByType: BudgetsByType,
    onBudgetClick: (Budget?) -> Unit,
) {
    if (budgetsByType.areEmpty()) {
        MessageContainer(message = stringResource(R.string.you_have_no_budgets_yet))
    } else {
        BudgetListsByPeriodComponent(budgetsByType) { budget ->
            DefaultBudgetComponent(budget = budget, onClick = onBudgetClick)
        }
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditBudgetsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    budgetEntityList: List<BudgetEntity>? = null,
    budgetAccountAssociationList: List<BudgetAccountAssociation>? = null,
    accountList: List<Account> = listOf(
        Account(id = 1, orderNum = 1, isActive = true),
        Account(id = 2, orderNum = 2, isActive = false)
    ),
) {
    val budgetsByType = (budgetEntityList to budgetAccountAssociationList)
        .letIfNoneIsNull { (budgets, associations) ->
            budgets.toDomainModels(
                groupedCategoriesList = groupedCategoriesByType.expense,
                associations = associations,
                accounts = accountList
            )
        }?.groupByType()
        ?: BudgetsByType(
            daily = listOf(
                Budget(
                    id = 1,
                    priorityNum = 1.0,
                    amountLimit = 4000.0,
                    usedAmount = 2500.0,
                    usedPercentage = 62.5F,
                    category = groupedCategoriesByType.expense[0].category,
                    name = "Food & drinks",
                    repeatingPeriod = RepeatingPeriod.Daily,
                    dateRange = RepeatingPeriod.Daily.getLongDateRangeWithTime(),
                    currentTimeWithinRangeGraphPercentage = .5f,
                    currency = "USD",
                    linkedAccountsIds = listOf(1, 2)
                )
            ),
            weekly = listOf(
                Budget(
                    id = 2,
                    priorityNum = 2.0,
                    amountLimit = 4000.0,
                    usedAmount = 1000.0,
                    usedPercentage = 25F,
                    category = groupedCategoriesByType.expense[1].category,
                    name = "Housing",
                    repeatingPeriod = RepeatingPeriod.Weekly,
                    dateRange = RepeatingPeriod.Weekly.getLongDateRangeWithTime(),
                    currentTimeWithinRangeGraphPercentage = .5f,
                    currency = "CZK",
                    linkedAccountsIds = listOf(3, 4)
                )
            ),
            monthly = listOf(
                Budget(
                    id = 1,
                    priorityNum = 1.0,
                    amountLimit = 4000.0,
                    usedAmount = 2500.0,
                    usedPercentage = 62.5F,
                    category = groupedCategoriesByType.expense[0].category,
                    name = "Food & drinks",
                    repeatingPeriod = RepeatingPeriod.Monthly,
                    dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
                    currentTimeWithinRangeGraphPercentage = .5f,
                    currency = "USD",
                    linkedAccountsIds = listOf(1, 2)
                ),
                Budget(
                    id = 3,
                    priorityNum = 3.0,
                    amountLimit = 4000.0,
                    usedAmount = 1000.0,
                    usedPercentage = 25F,
                    category = groupedCategoriesByType.expense[2].category,
                    name = "Shopping",
                    repeatingPeriod = RepeatingPeriod.Monthly,
                    dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
                    currentTimeWithinRangeGraphPercentage = .5f,
                    currency = "CZK",
                    linkedAccountsIds = listOf(3, 4)
                )
            )
        )

    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        EditBudgetsScreen(
            screenPadding = scaffoldPadding,
            onNavigateBack = {},
            isAppSetUp = isAppSetUp,
            budgetsByType = budgetsByType,
            onNavigateToEditBudgetScreen = {},
            onSaveBudgetsButton = {}
        )
    }
}