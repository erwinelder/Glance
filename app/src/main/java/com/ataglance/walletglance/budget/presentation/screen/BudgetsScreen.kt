package com.ataglance.walletglance.budget.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.model.BudgetEntity
import com.ataglance.walletglance.budget.mapper.toBudgetList
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.presentation.components.BudgetListsByPeriodComponent
import com.ataglance.walletglance.budget.presentation.components.BudgetWithStatsComponent
import com.ataglance.walletglance.budget.domain.utils.groupByType
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface
import com.ataglance.walletglance.core.presentation.components.containers.MessageContainer
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.utils.getLongDateRangeWithTime
import com.ataglance.walletglance.core.utils.letIfNoneIsNull
import com.ataglance.walletglance.navigation.domain.utils.isScreen
import com.ataglance.walletglance.record.data.model.RecordEntity

@Composable
fun BudgetsScreen(
    screenPadding: PaddingValues,
    budgetsByType: BudgetsByType,
    onBudgetClick: (Budget) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 16.dp, bottom = 16.dp + screenPadding.calculateBottomPadding(),
                start = 16.dp, end = 16.dp
            )
    ) {
        GlassSurface(
            modifier = Modifier.weight(1f),
            filledWidths = FilledWidthByScreenType(compact = 1f)
        ) {
            if (budgetsByType.areEmpty()) {
                MessageContainer(
                    message = stringResource(R.string.you_have_no_budgets_yet)
                )
            } else {
                BudgetListsByPeriodComponent(budgetsByType) { budget ->
                    BudgetWithStatsComponent(budget = budget, onClick = onBudgetClick)
                }
            }
        }
    }
}


@Preview(
    device = Devices.PIXEL_7_PRO
)
@Composable
fun BudgetsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isSetupProgressTopBarVisible: Boolean = false,
    isBottomBarVisible: Boolean = true,
    categoriesWithSubcategories: CategoriesWithSubcategories = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    budgetEntityList: List<BudgetEntity>? = null,
    budgetAccountAssociationList: List<BudgetAccountAssociation>? = null,
    accountList: List<Account> = listOf(
        Account(id = 1, orderNum = 1, isActive = true),
        Account(id = 2, orderNum = 2, isActive = false)
    ),
    recordList: List<RecordEntity> = emptyList()
) {
    val budgetsByType = (budgetEntityList to budgetAccountAssociationList)
        .letIfNoneIsNull { (budgets, associations) ->
            budgets.toBudgetList(
                categoryWithSubcategoriesList = categoriesWithSubcategories.expense,
                associationList = associations,
                accountList = accountList
            )
        }?.groupByType()?.fillUsedAmountsByRecords(recordList)
        ?: BudgetsByType(
            daily = listOf(
                Budget(
                    id = 1,
                    priorityNum = 1.0,
                    amountLimit = 4000.0,
                    usedAmount = 2500.0,
                    usedPercentage = 62.5F,
                    category = categoriesWithSubcategories.expense[0].category,
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
                    category = categoriesWithSubcategories.expense[1].category,
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
                    category = categoriesWithSubcategories.expense[0].category,
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
                    category = categoriesWithSubcategories.expense[2].category,
                    name = "Shopping",
                    repeatingPeriod = RepeatingPeriod.Monthly,
                    dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
                    currentTimeWithinRangeGraphPercentage = .5f,
                    currency = "CZK",
                    linkedAccountsIds = listOf(3, 4)
                )
            )
        )

    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        anyScreenInHierarchyIsScreenProvider = { it.isScreen(MainScreens.Budgets) }
    ) { scaffoldPadding ->
        BudgetsScreen(
            screenPadding = scaffoldPadding,
            budgetsByType = budgetsByType,
            onBudgetClick = {}
        )
    }
}