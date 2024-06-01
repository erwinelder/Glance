package com.ataglance.walletglance.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.domain.entities.Account
import com.ataglance.walletglance.ui.viewmodels.CategoryStatisticsViewModel
import com.ataglance.walletglance.ui.viewmodels.DateRangeEnum
import com.ataglance.walletglance.ui.theme.screencontainers.ScreenDataContainer
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.categories.CategoryStatisticsItemComponent
import com.ataglance.walletglance.ui.theme.uielements.categories.CategoryTypeFilterBar
import com.ataglance.walletglance.ui.theme.uielements.dividers.BigDivider

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
    parentCategoryId: Int?
) {
    LaunchedEffect(currentDateRangeEnum, accountList) {
        viewModel.clearParentCategory()
    }
    LaunchedEffect(parentCategoryId) {
        viewModel.setParentCategoryById(parentCategoryId)
    }

    val categoryType by viewModel.categoryType.collectAsStateWithLifecycle()
    val parentCategory by viewModel.parentCategory.collectAsStateWithLifecycle()
    val categoryList by viewModel.categoryList.collectAsStateWithLifecycle()

    ScreenDataContainer(
        scaffoldAppScreenPadding = scaffoldAppScreenPadding,
        accountList = accountList,
        appTheme = appTheme,
        onAccountClick = onAccountClick,
        currentDateRangeEnum = currentDateRangeEnum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        onDateRangeChange = onDateRangeChange,
        onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
        animationContentLabel = "all categories statistics",
        animatedContentTargetState = Pair(categoryList, parentCategory),
        visibleNoDataMessage = categoryList.isEmpty(),
        noDataMessageResource = R.string.no_data_for_the_selected_filter,
        typeFilterBar = {
            CategoryTypeFilterBar(categoryType) {
                viewModel.setCategoryType(it)
            }
        }
    ) { categoryListAndParCategory ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            categoryListAndParCategory.second?.let {
                Spacer(modifier = Modifier.height(16.dp))
                CategoryStatisticsItemComponent(it, showLeftArrow = true) {
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
                    CategoryStatisticsItemComponent(category) {
                        viewModel.setParentCategory(category)
                    }
                }
            }
        }
    }
}