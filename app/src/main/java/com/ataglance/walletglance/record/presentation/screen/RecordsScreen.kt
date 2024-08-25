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
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.utils.findById
import com.ataglance.walletglance.category.domain.DefaultCategoriesPackage
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.presentation.components.CategoryCollectionTypeToggleButton
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.DataPresentationScreenContainer
import com.ataglance.walletglance.core.utils.getTodayDateLong
import com.ataglance.walletglance.core.utils.isScreen
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordStackUnit
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.record.presentation.components.RecordStackComponent
import com.ataglance.walletglance.record.presentation.components.TransferComponent
import com.ataglance.walletglance.record.utils.containsRecordsFromDifferentYears

@Composable
fun RecordsScreen(
    scaffoldAppScreenPadding: PaddingValues,
    appTheme: AppTheme?,
    accountList: List<Account>,
    onAccountClick: (Int) -> Unit,
    currentDateRangeEnum: DateRangeEnum,
    isCustomDateRangeWindowOpened: Boolean,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    onCustomDateRangeButtonClick: () -> Unit,
    collectionType: CategoryCollectionType,
    filteredRecords: List<RecordStack>,
    collectionList: List<CategoryCollectionWithIds>,
    selectedCollection: CategoryCollectionWithIds,
    onCollectionSelect: (CategoryCollectionWithIds) -> Unit,
    onToggleCollectionType: () -> Unit,
    onRecordClick: (Int) -> Unit,
    onTransferClick: (Int) -> Unit,
    onNavigateToEditCollectionsScreen: () -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    val includeYearToRecordDate by remember {
        derivedStateOf { filteredRecords.containsRecordsFromDifferentYears() }
    }
    val lazyListState = rememberLazyListState()
    /*val visibleItemsInfo = remember {
        derivedStateOf { lazyListState.layoutInfo.visibleItemsInfo }
    }*/


    DataPresentationScreenContainer(
        scaffoldAppScreenPadding = scaffoldAppScreenPadding,
        accountList = accountList,
        appTheme = appTheme,
        onAccountClick = onAccountClick,
        currentDateRangeEnum = currentDateRangeEnum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        onDateRangeChange = onDateRangeChange,
        onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
        collectionList = collectionList,
        selectedCollection = selectedCollection,
        onCollectionSelect = onCollectionSelect,
        animationContentLabel = "records history widget content",
        animatedContentTargetState = Pair(filteredRecords, collectionType),
        visibleNoDataMessage = filteredRecords.isEmpty(),
        noDataMessageRes = when(collectionType) {
            CategoryCollectionType.Mixed -> R.string.you_have_no_records_in_date_range
            CategoryCollectionType.Expense -> R.string.you_have_no_expenses_in_date_range
            CategoryCollectionType.Income -> R.string.you_have_no_income_in_date_range
        },
        typeToggleButton = {
            CategoryCollectionTypeToggleButton(
                currentType = collectionType, onClick = onToggleCollectionType
            )
        },
        onNavigateToEditCollectionsScreen = onNavigateToEditCollectionsScreen,
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
                    if (recordStack.isTransfer()) {
                        TransferComponent(
                            recordStack = recordStack,
                            includeYearToDate = includeYearToRecordDate,
                            appTheme = appTheme,
                            secondAccount = recordStack.stack.firstOrNull()?.note?.toInt()?.let {
                                accountList.findById(it)?.toRecordAccount()
                            },
                            onTransferClick = onTransferClick
                        )
                    } else {
                        RecordStackComponent(
                            appTheme = appTheme,
                            recordStack = recordStack,
                            includeYearToDate = includeYearToRecordDate,
                            onRecordClick = onRecordClick
                        )
                    }
                }
                /*itemsIndexed(
                    items = targetRecordStackListAndTypeFilter.first,
                    key = { _, item -> item.recordNum }
                ) { index, recordStack ->
                    val itemScale by animateFloatAsState(
                        targetValue = visibleItemsInfo.value
                            .getOrNull(
                                index - (visibleItemsInfo.value.getOrNull(0)?.index ?: 0)
                            )
                            ?.takeIf { it.size > 0 }
                            ?.let { item ->
                                ((100.0 / item.size) * -(item.offset.takeIf { it <= 0 } ?: 0)) / 100
                            }
                            ?.toFloat()
                            ?: 0.0f,
                        label = "item scale"
                    )

                    if (recordStack.isTransfer()) {
                        TransferComponent(
                            recordStack = recordStack,
                            includeYearToDate = includeYearToRecordDate,
                            appTheme = appTheme,
                            secondAccount = recordStack.stack.firstOrNull()?.note?.toInt()?.let {
                                accountList.findById(it)?.toRecordAccount()
                            },
                            onTransferClick = onTransferClick
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .scale(1f - itemScale)
                        ) {
                            RecordStackComponent(
                                appTheme = appTheme,
                                recordStack = recordStack,
                                includeYearToDate = includeYearToRecordDate,
                                onRecordClick = onRecordClick
                            )
                        }
                    }
                }*/
            }
        }
    }
}



@Preview(
    name = "HomeScreen",
    group = "MainScreens",
    apiLevel = 34,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun RecordsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetup: Boolean = true,
    isSetupProgressTopBarVisible: Boolean = false,
    isBottomBarVisible: Boolean = true,
    accountList: List<Account> = listOf(
        Account(id = 1, orderNum = 1, isActive = true),
        Account(id = 2, orderNum = 2, isActive = false)
    ),
    currentDateRangeEnum: DateRangeEnum = DateRangeEnum.ThisMonth,
    isCustomDateRangeWindowOpened: Boolean = false,
    collectionType: CategoryCollectionType = CategoryCollectionType.Mixed,
    filteredRecords: List<RecordStack> = listOf(
        RecordStack(
            recordNum = 1,
            date = getTodayDateLong(),
            type = RecordType.Expense,
            account = Account().toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackUnit(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSubcategory = DefaultCategoriesPackage(LocalContext.current)
                        .getDefaultCategories().expense[0].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        )
    ),
    collectionList: List<CategoryCollectionWithIds> = emptyList(),
    selectedCollection: CategoryCollectionWithIds = CategoryCollectionWithIds(
        name = stringResource(R.string.all_categories)
    ),
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        anyScreenInHierarchyIsScreenProvider = { it.isScreen(MainScreens.Home) }
    ) { scaffoldPadding ->
        RecordsScreen(
            scaffoldAppScreenPadding = scaffoldPadding,
            appTheme = appTheme,
            accountList = accountList,
            onAccountClick = {},
            currentDateRangeEnum = currentDateRangeEnum,
            isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
            onDateRangeChange = {},
            onCustomDateRangeButtonClick = {},
            collectionType = collectionType,
            filteredRecords = filteredRecords,
            collectionList = collectionList,
            selectedCollection = selectedCollection,
            onCollectionSelect = {},
            onToggleCollectionType = {},
            onRecordClick = {},
            onTransferClick = {},
            onNavigateToEditCollectionsScreen = {},
            onDimBackgroundChange = {}
        )
    }
}
