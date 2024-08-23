package com.ataglance.walletglance.category.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.presentation.components.screenContainers.DataPresentationScreenContainer
import com.ataglance.walletglance.category.presentation.components.CategoryStatisticsItemComponent
import com.ataglance.walletglance.category.presentation.components.CategoryTypeToggleButton
import com.ataglance.walletglance.core.presentation.components.dividers.BigDivider
import com.ataglance.walletglance.category.presentation.viewmodel.CategoryStatisticsViewModel

@Composable
fun CategoriesStatisticsScreen(
    scaffoldAppScreenPadding: PaddingValues,
    appTheme: AppTheme?,
    accountList: List<Account>,
    onAccountClick: (Int) -> Unit,
    currentDateRangeEnum: DateRangeEnum,
    isCustomDateRangeWindowOpened: Boolean,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    onCustomDateRangeButtonClick: () -> Unit,
    viewModel: CategoryStatisticsViewModel,
    onNavigateToEditCollectionsScreen: () -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    val categoryType by viewModel.categoryType.collectAsStateWithLifecycle()
    val parentCategory by viewModel.parentCategoryStatistics.collectAsStateWithLifecycle()
    val categoryStatisticsList by viewModel.categoryStatisticsList.collectAsStateWithLifecycle()
    val collectionList by viewModel.currentCollectionList.collectAsStateWithLifecycle()
    val selectedCollection by viewModel.selectedCollection.collectAsStateWithLifecycle()

    DataPresentationScreenContainer(
        scaffoldAppScreenPadding = scaffoldAppScreenPadding,
        appTheme = appTheme,
        accountList = accountList,
        onAccountClick = onAccountClick,
        currentDateRangeEnum = currentDateRangeEnum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        onDateRangeChange = onDateRangeChange,
        onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
        collectionList = collectionList,
        selectedCollection = selectedCollection,
        onCollectionSelect = {
            viewModel.selectCollection(it)
        },
        typeToggleButton = {
            CategoryTypeToggleButton(
                currentType = categoryType,
                onClick = {
                    viewModel.setCategoryType(it)
                }
            )
        },
        animationContentLabel = "all categories statistics",
        animatedContentTargetState = Pair(categoryStatisticsList, parentCategory),
        visibleNoDataMessage = categoryStatisticsList.isEmpty(),
        noDataMessageRes = R.string.no_data_for_the_selected_filter,
        onNavigateToEditCollectionsScreen = onNavigateToEditCollectionsScreen,
        onDimBackgroundChange = onDimBackgroundChange
    ) { categoryListAndParCategory ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            categoryListAndParCategory.second?.let {
                Spacer(modifier = Modifier.height(16.dp))
                CategoryStatisticsItemComponent(it, appTheme, showLeftArrow = true) {
                    viewModel.clearParentCategory()
                }
                Spacer(modifier = Modifier.height(16.dp))
                BigDivider()
            }
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = categoryListAndParCategory.first,
                    key = { it.categoryId }
                ) { category ->
                    CategoryStatisticsItemComponent(category, appTheme) {
                        viewModel.setParentCategory(category)
                    }
                }
            }
        }
    }
}