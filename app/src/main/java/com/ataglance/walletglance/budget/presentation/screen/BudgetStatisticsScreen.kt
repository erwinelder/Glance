package com.ataglance.walletglance.budget.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.component.AccountsFlowRow
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.presentation.model.BudgetStatisticsScreenUiState
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetStatisticsViewModel
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.presentation.component.CategoryBigIconComponent
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.DrawableResByTheme
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.utils.getPrevDateRanges
import com.ataglance.walletglance.core.domain.date.getSpendingInRecentStringRes
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.domain.statistics.ColumnChartUiState
import com.ataglance.walletglance.core.domain.statistics.TotalAmountInRange
import com.ataglance.walletglance.core.presentation.component.chart.ColumnChartComponent
import com.ataglance.walletglance.core.presentation.component.chart.SingleValuePieChartComponent
import com.ataglance.walletglance.core.presentation.component.container.MessageContainer
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButton
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.core.utils.toTimestampRange
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BudgetStatisticsScreenWrapper(
    screenPadding: PaddingValues,
    backStack: NavBackStackEntry,
    navController: NavHostController,
    appConfiguration: AppConfiguration
) {
    val budgetId = backStack.toRoute<MainScreens.BudgetStatistics>().id

    val viewModel = koinViewModel<BudgetStatisticsViewModel> {
        parametersOf(budgetId, appConfiguration.langCode)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BudgetStatisticsScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        uiState = uiState
    )
}

@Composable
fun BudgetStatisticsScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    uiState: BudgetStatisticsScreenUiState
) {
    val backNavButtonImageRes = DrawableResByTheme(
        lightDefault = R.drawable.budgets_light_default_icon,
        darkDefault = R.drawable.budgets_dark_default_icon,
    ).get(CurrAppTheme)

    ScreenContainerWithTopBackNavButton(
        screenPadding = screenPadding,
        backNavButtonText = stringResource(R.string.budget_statistics_title),
        backNavButtonImageRes = backNavButtonImageRes,
        onBackNavButtonClick = onNavigateBack
    ) {
        if (uiState.budget != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .weight(1f)
            ) {
                BudgetStatisticsScreenContent(
                    budget = uiState.budget,
                    columnChartUiState = uiState.columnChartUiState,
                    budgetAccounts = uiState.accounts
                )
            }
        } else {
            MessageContainer(message = stringResource(R.string.budget_not_found))
        }
    }
}

@Composable
private fun BudgetStatisticsScreenContent(
    budget: Budget,
    columnChartUiState: ColumnChartUiState,
    budgetAccounts: List<Account>
) {
    val nestedScrollInterop = rememberNestedScrollInteropConnection()
    val verticalGap = 16.dp

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        contentPadding = PaddingValues(bottom = 8.dp),
        modifier = Modifier
            .nestedScroll(nestedScrollInterop)
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.screen_horizontal_padding))
    ) {
        item { Spacer(modifier = Modifier.height(verticalGap)) }
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                budget.category?.let {
                    CategoryBigIconComponent(category = it)
                }
                Text(
                    text = budget.name,
                    color = GlanciColors.onSurface,
                    fontSize = 26.sp,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.W600,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(
                        R.string.amount_currency_spending_limit,
                        budget.amountLimit.formatWithSpaces(), budget.currency
                    ),
                    color = GlanciColors.onSurface,
                    fontSize = 20.sp,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AccountsFlowRow(accountList = budgetAccounts, maxLines = 5)
            }
        }
        item { Spacer(modifier = Modifier.height(verticalGap)) }
        item {
            ColumnChartComponent(
                uiState = columnChartUiState,
                columnsColor = budget.category?.getColorByTheme(CurrAppTheme)?.lighter,
                title = stringResource(
                    id = budget.repeatingPeriod.getSpendingInRecentStringRes(), budget.currency
                ),
                bottomNote = "Average spending: " +
                        columnChartUiState.averageValue.formatWithSpaces(budget.currency)
            ) { totalAmountByPeriod ->
                StatisticByPeriodDetailsPopupContent(budget, totalAmountByPeriod)
            }
        }
        item { Spacer(modifier = Modifier.height(verticalGap)) }
    }
}

@Composable
private fun StatisticByPeriodDetailsPopupContent(budget: Budget, totalAmount: Double) {
    val usedPercentage by remember(budget, totalAmount) {
        derivedStateOf { 100 / budget.amountLimit * totalAmount }
    }
    val pieChartPercentage by remember(usedPercentage) {
        derivedStateOf { (3.6 * usedPercentage).toFloat() }
    }
    val pieChartBrush = if (usedPercentage < 50.0) {
        GlanciColors.pieChartGreenGradient.reversed()
    } else if (usedPercentage >= 50.0 && usedPercentage < 100.0) {
        GlanciColors.pieChartYellowGradient.reversed()
    } else {
        GlanciColors.pieChartRedGradient.reversed()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                SingleValuePieChartComponent(
                    percentage = pieChartPercentage,
                    brush = pieChartBrush,
                    size = 70.dp
                )
                Text(
                    text = "${usedPercentage.toInt()}%",
                    color = GlanciColors.onSurface,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.Medium
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = stringResource(R.string.of_limit_used),
                    color = GlanciColors.onSurface,
                    fontFamily = Manrope,
                )
                Text(
                    text = "(${totalAmount.formatWithSpaces(budget.currency)})",
                    color = GlanciColors.onSurface,
                    fontSize = 18.sp,
                    fontFamily = Manrope,
                )
            }
        }
    }
}


@Preview(
    locale = "en",
    device = Devices.PIXEL_7_PRO,
)
@Composable
fun BudgetStatisticsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    accountList: List<Account> = listOf(
        Account(
            id = 1,
            orderNum = 1,
            name = "Main USD",
            currency = "USD",
            balance = 112.13,
            color = AccountColors.Default,
            isActive = false
        ),
        Account(
            id = 2,
            orderNum = 2,
            name = "Local Card CZK",
            currency = "CZK",
            balance = 1412.13,
            color = AccountColors.Pink,
            isActive = false
        ),
    ),
    budget: Budget = Budget(
        id = 1,
        priorityNum = 1.0,
        amountLimit = 4000.0,
        usedAmount = 2500.0,
        usedPercentage = 62.5F,
        category = groupedCategoriesByType.expense[0].category,
        name = groupedCategoriesByType.expense[0].category.name,
        repeatingPeriod = RepeatingPeriod.Monthly,
        dateRange = RepeatingPeriod.Monthly.toTimestampRange(),
        currentTimeWithinRangeGraphPercentage = .5f,
        currency = "USD",
        linkedAccountIds = listOf(1, 2)
    ),
    totalAmounts: List<Double> = (0..5).map { 5000.0 / (it + 1) }
) {
    val context = LocalContext.current
    val resourceManager = ResourceManagerImpl(context)
    val totalAmountsByRanges = budget.repeatingPeriod.getPrevDateRanges()
        .mapIndexed { index, dateRange ->
            TotalAmountInRange(
                dateRange = dateRange,
                totalAmount = totalAmounts.getOrNull(index) ?: 0.0
            )
        }
        .reversed()
    val columnChartUiState = ColumnChartUiState.asAmountsByDateRanges(
        totalAmountsByRanges = totalAmountsByRanges,
        rowsCount = 5,
        repeatingPeriod = budget.repeatingPeriod,
        resourceManager = resourceManager
    )

    PreviewContainer(appTheme = appTheme) {
        BudgetStatisticsScreen(
            onNavigateBack = {},
            uiState = BudgetStatisticsScreenUiState(
                budget = budget,
                columnChartUiState = columnChartUiState,
                accounts = accountList
            )
        )
    }
}