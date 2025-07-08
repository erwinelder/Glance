package com.ataglance.walletglance.budget.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.budget.data.model.BudgetDataModelWithAssociations
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.domain.utils.groupByType
import com.ataglance.walletglance.budget.mapper.budget.toDomainModel
import com.ataglance.walletglance.budget.presentation.component.BudgetListsByPeriodComponent
import com.ataglance.walletglance.budget.presentation.component.BudgetWithStatsGlassComponent
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsViewModel
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.component.container.MessageContainer
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.utils.plus
import com.ataglance.walletglance.core.utils.toTimestamp
import com.ataglance.walletglance.core.utils.toTimestampRange
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.transaction.domain.model.Record
import com.ataglance.walletglance.transaction.domain.model.RecordItem
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import com.ataglance.walletglance.transaction.domain.model.Transaction
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transaction.domain.model.TransferItem
import kotlinx.datetime.LocalDateTime
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BudgetsScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    val resourceManager = koinInject<ResourceManager>()
    val viewModel = koinViewModel<BudgetsViewModel>()

    val budgetsByType by viewModel.budgetsByType.collectAsStateWithLifecycle()

    BudgetsScreen(
        screenPadding = screenPadding,
        budgetsByType = budgetsByType,
        resourceManager = resourceManager,
        onBudgetClick = { budget ->
            navViewModel.navigateToScreenMovingTowardsLeft(
                navController = navController, screen = MainScreens.BudgetStatistics(budget.id)
            )
        }
    )
}

@Composable
fun BudgetsScreen(
    screenPadding: PaddingValues = PaddingValues(),
    budgetsByType: BudgetsByType,
    resourceManager: ResourceManager,
    onBudgetClick: (Budget) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if (budgetsByType.areEmpty()) {
            MessageContainer(
                message = stringResource(R.string.you_have_no_budgets_yet)
            )
        } else {
            BudgetListsByPeriodComponent(
                budgetsByType = budgetsByType,
                contentPadding = screenPadding + PaddingValues(vertical = 24.dp),
                textDividerFilledWidth = FilledWidthByScreenType(.76f).getByType(CurrWindowType)
            ) { budget ->
                BudgetWithStatsGlassComponent(
                    budget = budget, resourceManager = resourceManager, onClick = onBudgetClick
                )
            }
        }
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun BudgetsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    budgetDataModelsWithAssociations: List<BudgetDataModelWithAssociations>? = null,
    accounts: List<Account> = listOf(
        Account(id = 1, orderNum = 1, isActive = true),
        Account(id = 2, orderNum = 2, isActive = false)
    ),
    transactions: List<Transaction> = listOf(
        RecordWithItems(
            record = Record(
                id = 1,
                date = LocalDateTime(2024, 9, 24, 12, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 1,
                    recordId = 1,
                    totalAmount = 68.43,
                    quantity = null,
                    categoryId = 1,
                    subcategoryId = 13,
                    note = "bread, milk"
                ),
                RecordItem(
                    id = 2,
                    recordId = 1,
                    totalAmount = 178.9,
                    quantity = null,
                    categoryId = 3,
                    subcategoryId = 24,
                    note = "shampoo"
                )
            )
        ),
        Transfer(
            id = 1,
            date = LocalDateTime(2024, 9, 23, 0, 0).toTimestamp(),
            sender = TransferItem(
                accountId = accounts[0].id,
                amount = 3000.0,
                rate = 1.0
            ),
            receiver = TransferItem(
                accountId = accounts[1].id,
                amount = 3000.0,
                rate = 1.0
            ),
            includeInBudgets = true
        ),
        RecordWithItems(
            record = Record(
                id = 4,
                date = LocalDateTime(2024, 9, 18, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 4,
                    recordId = 4,
                    totalAmount = 120.9,
                    quantity = null,
                    categoryId = 6,
                    subcategoryId = 40,
                    note = "Music platform"
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 5,
                date = LocalDateTime(2024, 9, 15, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 5,
                    recordId = 5,
                    totalAmount = 799.9,
                    quantity = null,
                    categoryId = 3,
                    subcategoryId = 21,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 6,
                date = LocalDateTime(2024, 9, 12, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 6,
                    recordId = 6,
                    totalAmount = 3599.9,
                    quantity = null,
                    categoryId = 1,
                    subcategoryId = 13,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 7,
                date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 7,
                    recordId = 7,
                    totalAmount = 8500.0,
                    quantity = null,
                    categoryId = 2,
                    subcategoryId = 15,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 8,
                date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
                type = CategoryType.Income,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 8,
                    recordId = 8,
                    totalAmount = 42600.0,
                    quantity = null,
                    categoryId = 72,
                    subcategoryId = null,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 9,
                date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 9,
                    recordId = 9,
                    totalAmount = 799.9,
                    quantity = null,
                    categoryId = 6,
                    subcategoryId = 38,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 10,
                date = LocalDateTime(2024, 6, 4, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[1].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 10,
                    recordId = 10,
                    totalAmount = 450.41,
                    quantity = null,
                    categoryId = 9,
                    subcategoryId = 50,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 10,
                date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 10,
                    recordId = 10,
                    totalAmount = 690.56,
                    quantity = null,
                    categoryId = 10,
                    subcategoryId = 58,
                    note = null
                )
            )
        ),
    ),
) {
    val budgetsByType = budgetDataModelsWithAssociations
        ?.let { budgets ->
            budgets.mapNotNull {
                it.toDomainModel(
                    groupedCategoriesList = groupedCategoriesByType.expense, accounts = accounts
                )
            }
        }?.groupByType()?.fillUsedAmountsByTransactions(transactions = transactions)
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
                    dateRange = RepeatingPeriod.Daily.toTimestampRange(),
                    currentTimeWithinRangeGraphPercentage = .5f,
                    currency = "USD",
                    linkedAccountIds = listOf(1, 2)
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
                    dateRange = RepeatingPeriod.Weekly.toTimestampRange(),
                    currentTimeWithinRangeGraphPercentage = .5f,
                    currency = "CZK",
                    linkedAccountIds = listOf(3, 4)
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
                    dateRange = RepeatingPeriod.Monthly.toTimestampRange(),
                    currentTimeWithinRangeGraphPercentage = .5f,
                    currency = "USD",
                    linkedAccountIds = listOf(1, 2)
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
                    dateRange = RepeatingPeriod.Monthly.toTimestampRange(),
                    currentTimeWithinRangeGraphPercentage = .5f,
                    currency = "CZK",
                    linkedAccountIds = listOf(3, 4)
                )
            ),
            yearly = listOf(
                Budget(
                    id = 1,
                    priorityNum = 1.0,
                    amountLimit = 4000.0,
                    usedAmount = 2500.0,
                    usedPercentage = 62.5F,
                    category = groupedCategoriesByType.expense[0].category,
                    name = "Food & drinks",
                    repeatingPeriod = RepeatingPeriod.Yearly,
                    dateRange = RepeatingPeriod.Yearly.toTimestampRange(),
                    currentTimeWithinRangeGraphPercentage = .5f,
                    currency = "USD",
                    linkedAccountIds = listOf(1, 2)
                ),
                Budget(
                    id = 3,
                    priorityNum = 3.0,
                    amountLimit = 4000.0,
                    usedAmount = 1000.0,
                    usedPercentage = 25F,
                    category = groupedCategoriesByType.expense[2].category,
                    name = "Shopping",
                    repeatingPeriod = RepeatingPeriod.Yearly,
                    dateRange = RepeatingPeriod.Yearly.toTimestampRange(),
                    currentTimeWithinRangeGraphPercentage = .5f,
                    currency = "CZK",
                    linkedAccountIds = listOf(3, 4)
                )
            )
        )

    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        BudgetsScreen(
            screenPadding = scaffoldPadding,
            budgetsByType = budgetsByType,
            resourceManager = ResourceManagerImpl(LocalContext.current),
            onBudgetClick = {}
        )
    }
}