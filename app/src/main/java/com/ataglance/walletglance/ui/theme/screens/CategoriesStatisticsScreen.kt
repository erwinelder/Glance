package com.ataglance.walletglance.ui.theme.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.model.CategoryStatisticsViewModel
import com.ataglance.walletglance.model.DateRangeEnum
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.categories.CategoryStatisticsItemComponent
import com.ataglance.walletglance.ui.theme.uielements.categories.CategoryTypeFilterBar
import com.ataglance.walletglance.ui.theme.uielements.categories.EmptyCategoriesStatisticsMessageContainer
import com.ataglance.walletglance.ui.theme.uielements.containers.DateFilterBar
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import com.ataglance.walletglance.ui.theme.uielements.containers.SmallAccountsContainer
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
    LaunchedEffect(parentCategoryId) {
        viewModel.setParentCategoryById(parentCategoryId)
    }

    val categoryType by viewModel.categoryType.collectAsStateWithLifecycle()
    val parentCategory by viewModel.parentCategory.collectAsStateWithLifecycle()
    val categoryList by viewModel.categoryList.collectAsStateWithLifecycle()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.button_bar_to_widget_gap)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = scaffoldAppScreenPadding.calculateTopPadding() +
                        dimensionResource(R.dimen.button_bar_to_widget_gap),
                bottom = scaffoldAppScreenPadding.calculateBottomPadding() +
                        dimensionResource(R.dimen.screen_vertical_padding),
                start = 16.dp,
                end = 16.dp
            )
    ) {
        if (accountList.size > 1) {
            SmallAccountsContainer(
                accountList = accountList,
                appTheme = appTheme,
                onAccountClick = onAccountClick
            )
        }
        DateFilterBar(
            currentDateRangeEnum = currentDateRangeEnum,
            isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
            onDateRangeChange = onDateRangeChange,
            onCustomDateRangeButtonClick = onCustomDateRangeButtonClick
        )
        CategoryTypeFilterBar(
            currentCategoryType = categoryType,
            onClick = viewModel::setCategoryType,
        )
        Spacer(modifier = Modifier)
        GlassSurface(
            modifier = Modifier.fillMaxHeight(),
            filledWidth = 1f
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AnimatedContent(
                    targetState = Pair(categoryList, parentCategory),
                    label = "all categories statistics"
                ) { categoryListAndParCategory ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
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
                            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            if (categoryList.isNotEmpty()) {
                                items(
                                    items = categoryListAndParCategory.first,
                                    key = { it.categoryId }
                                ) { category ->
                                    CategoryStatisticsItemComponent(
                                        showRightArrow =
                                            category.subcategoriesStatisticsUiState != null,
                                        uiState = category
                                    ) {
                                        viewModel.setParentCategory(category)
                                    }
                                }
                            } else {
                                item {
                                    EmptyCategoriesStatisticsMessageContainer()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}