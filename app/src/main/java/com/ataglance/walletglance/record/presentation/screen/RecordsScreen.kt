package com.ataglance.walletglance.record.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.mapper.toRecordAccount
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.presentation.model.CategoryCollectionsUiState
import com.ataglance.walletglance.categoryCollection.presentation.navigation.CategoryCollectionsSettingsScreens
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainerWithFilters
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.utils.getTodayDateLong
import com.ataglance.walletglance.navigation.domain.utils.isScreen
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.model.RecordStackItem
import com.ataglance.walletglance.record.domain.model.RecordType
import com.ataglance.walletglance.record.domain.utils.containsRecordsFromDifferentYears
import com.ataglance.walletglance.record.mapper.toRecordStacks
import com.ataglance.walletglance.record.presentation.components.RecordStackComponent
import com.ataglance.walletglance.record.presentation.components.TransferComponent

@Composable
fun RecordsScreen(
    scaffoldAppScreenPadding: PaddingValues,
    accountList: List<Account>,
    onAccountClick: (Int) -> Unit,
    currentDateRangeEnum: DateRangeEnum,
    isCustomDateRangeWindowOpened: Boolean,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    onCustomDateRangeButtonClick: () -> Unit,
    collectionsUiState: CategoryCollectionsUiState,
    onToggleCollectionType: () -> Unit,
    onCollectionSelect: (Int) -> Unit,

    recordStacks: List<RecordStack>,
    onNavigateToScreenMovingTowardsLeft: (Any) -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    val includeYearToRecordDate by remember {
        derivedStateOf { recordStacks.containsRecordsFromDifferentYears() }
    }
    val lazyListState = rememberLazyListState()

    GlassSurfaceScreenContainerWithFilters(
        screenPadding = scaffoldAppScreenPadding,
        accountList = accountList,
        onAccountClick = onAccountClick,
        currentDateRangeEnum = currentDateRangeEnum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        onDateRangeChange = onDateRangeChange,
        onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
        collectionsUiState = collectionsUiState,
        onCollectionSelect = onCollectionSelect,
        onToggleCollectionType = onToggleCollectionType,
        animatedContentLabel = "records history widget content",
        animatedContentTargetState = Pair(recordStacks, collectionsUiState.activeType),
        visibleNoDataMessage = recordStacks.isEmpty(),
        noDataMessageRes = when(collectionsUiState.activeType) {
            CategoryCollectionType.Mixed -> R.string.you_have_no_records_in_date_range
            CategoryCollectionType.Expense -> R.string.you_have_no_expenses_in_date_range
            CategoryCollectionType.Income -> R.string.you_have_no_income_in_date_range
        },
        onNavigateToEditCollectionsScreen = {
            onNavigateToScreenMovingTowardsLeft(
                CategoryCollectionsSettingsScreens.EditCategoryCollections
            )
        },
        onDimBackgroundChange = onDimBackgroundChange
    ) { targetRecordStackListAndTypeFilter ->
        Column {
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = targetRecordStackListAndTypeFilter.first,
                    key = { it.recordNum }
                ) { recordStack ->
                    if (recordStack.isNotTransfer()) {
                        RecordStackComponent(
                            recordStack = recordStack,
                            includeYearToDate = includeYearToRecordDate
                        ) { recordNum ->
                            onNavigateToScreenMovingTowardsLeft(
                                MainScreens.RecordCreation(recordNum = recordNum)
                            )
                        }
                    } else {
                        TransferComponent(
                            recordStack = recordStack,
                            includeYearToDate = includeYearToRecordDate,
                            secondAccount = recordStack.stack.firstOrNull()?.note?.toInt()?.let {
                                accountList.findById(it)?.toRecordAccount()
                            }
                        ) { recordNum ->
                            onNavigateToScreenMovingTowardsLeft(
                                MainScreens.TransferCreation(recordNum = recordNum)
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview(
    apiLevel = 34,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun RecordsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isBottomBarVisible: Boolean = true,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    accountList: List<Account> = listOf(
        Account(id = 1, orderNum = 1, isActive = true),
        Account(id = 2, orderNum = 2, isActive = false)
    ),
    currentDateRangeEnum: DateRangeEnum = DateRangeEnum.ThisMonth,
    isCustomDateRangeWindowOpened: Boolean = false,
    collectionType: CategoryCollectionType = CategoryCollectionType.Mixed,
    collectionList: List<CategoryCollectionWithIds> = emptyList(),
    selectedCollection: CategoryCollectionWithIds = CategoryCollectionWithIds(
        name = stringResource(R.string.all_categories)
    ),
    recordList: List<RecordEntity>? = null,
    recordStackList: List<RecordStack> = recordList?.toRecordStacks(
        accounts = accountList, groupedCategoriesByType = groupedCategoriesByType
    ) ?: listOf(
        RecordStack(
            recordNum = 1,
            date = getTodayDateLong(),
            type = RecordType.Expense,
            account = Account().toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSub = groupedCategoriesByType
                        .expense[0].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        )
    )
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isBottomBarVisible = isBottomBarVisible,
        anyScreenInHierarchyIsScreenProvider = { it.isScreen(MainScreens.Records) }
    ) { scaffoldPadding ->
        RecordsScreen(
            scaffoldAppScreenPadding = scaffoldPadding,
            accountList = accountList,
            onAccountClick = {},
            currentDateRangeEnum = currentDateRangeEnum,
            isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
            onDateRangeChange = {},
            onCustomDateRangeButtonClick = {},
            collectionsUiState = CategoryCollectionsUiState(
                collections = collectionList,
                activeCollection = selectedCollection,
                activeType = collectionType
            ),
            recordStacks = recordStackList,
            onCollectionSelect = {},
            onToggleCollectionType = {},
            onNavigateToScreenMovingTowardsLeft = {},
            onDimBackgroundChange = {}
        )
    }
}
