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
import com.ataglance.walletglance.category.domain.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.DefaultCategoriesPackage
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.navigation.CategoryCollectionsSettingsScreens
import com.ataglance.walletglance.categoryCollection.presentation.components.CategoryCollectionTypeToggleButton
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceContainerWithFilterBars
import com.ataglance.walletglance.core.utils.getTodayDateLong
import com.ataglance.walletglance.navigation.utils.isScreen
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.data.mapper.toRecordStackList
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordStackItem
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.record.presentation.components.RecordStackComponent
import com.ataglance.walletglance.record.presentation.components.TransferComponent
import com.ataglance.walletglance.record.utils.containsRecordsFromDifferentYears

@Composable
fun RecordsScreen(
    scaffoldAppScreenPadding: PaddingValues,
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
    onNavigateToScreenMovingTowardsLeft: (Any) -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    val includeYearToRecordDate by remember {
        derivedStateOf { filteredRecords.containsRecordsFromDifferentYears() }
    }
    val lazyListState = rememberLazyListState()

    GlassSurfaceContainerWithFilterBars(
        screenPadding = scaffoldAppScreenPadding,
        accountList = accountList,
        onAccountClick = onAccountClick,
        currentDateRangeEnum = currentDateRangeEnum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        onDateRangeChange = onDateRangeChange,
        onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
        collectionList = collectionList,
        selectedCollection = selectedCollection,
        onCollectionSelect = onCollectionSelect,
        animatedContentLabel = "records history widget content",
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
                    if (recordStack.isTransfer()) {
                        TransferComponent(
                            recordStack = recordStack,
                            includeYearToDate = includeYearToRecordDate,
                            secondAccount = recordStack.stack.firstOrNull()?.note?.toInt()?.let {
                                accountList.findById(it)?.toRecordAccount()
                            }
                        ) { recordNum ->
                            onNavigateToScreenMovingTowardsLeft(
                                MainScreens.TransferCreation(isNew = false, recordNum = recordNum)
                            )
                        }
                    } else {
                        RecordStackComponent(
                            recordStack = recordStack,
                            includeYearToDate = includeYearToRecordDate
                        ) { recordNum ->
                            onNavigateToScreenMovingTowardsLeft(
                                MainScreens.RecordCreation(isNew = false, recordNum = recordNum)
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
    isSetupProgressTopBarVisible: Boolean = false,
    isBottomBarVisible: Boolean = true,
    categoriesWithSubcategories: CategoriesWithSubcategories = DefaultCategoriesPackage(
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
    recordStackList: List<RecordStack> = recordList?.toRecordStackList(
        accountList = accountList,
        categoriesWithSubcategories = categoriesWithSubcategories
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
                    categoryWithSubcategory = categoriesWithSubcategories
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
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
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
            collectionType = collectionType,
            filteredRecords = recordStackList,
            collectionList = collectionList,
            selectedCollection = selectedCollection,
            onCollectionSelect = {},
            onToggleCollectionType = {},
            onNavigateToScreenMovingTowardsLeft = {},
            onDimBackgroundChange = {}
        )
    }
}
