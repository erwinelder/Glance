package com.ataglance.walletglance.category.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryColor
import com.ataglance.walletglance.category.domain.model.CategoryIcon
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.presentation.model.CategoriesStatisticsWidgetUiState
import com.ataglance.walletglance.category.presentation.model.CategoryStatistics
import com.ataglance.walletglance.category.presentation.viewmodel.CategoryStatisticsWidgetViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.presentation.components.containers.MessageContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.presentation.components.widgets.WidgetWithTitleAndButtonComponent
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CategoriesStatisticsWidget(
    activeAccount: Account?,
    activeDateRange: LongDateRange,
    onNavigateToCategoriesStatisticsScreen: (Int?, CategoryType?) -> Unit
) {
    val viewModel = koinViewModel<CategoryStatisticsWidgetViewModel> {
        parametersOf(activeAccount, activeDateRange)
    }

    LaunchedEffect(activeAccount) {
        activeAccount?.id?.let(viewModel::setActiveAccountId)
    }
    LaunchedEffect(activeDateRange) {
        viewModel.setActiveDateRange(activeDateRange)
    }

    val uiState by viewModel.statisticsUiState.collectAsStateWithLifecycle()

    CategoriesStatisticsWidgetContent(
        uiState = uiState,
        onNavigateToCategoriesStatisticsScreen = onNavigateToCategoriesStatisticsScreen
    )
}

@Composable
fun CategoriesStatisticsWidgetContent(
    uiState: CategoriesStatisticsWidgetUiState,
    onNavigateToCategoriesStatisticsScreen: (Int?, CategoryType?) -> Unit
) {
    WidgetWithTitleAndButtonComponent(
        title = stringResource(R.string.categories),
        onBottomNavigationButtonClick = {
            onNavigateToCategoriesStatisticsScreen(null, uiState.getType())
        }
    ) {
        AnimatedContent(targetState = uiState) { state ->
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CategoryStatisticsItemComponent(state.top1Category, enableClick = true) {
                        state.top1Category?.category?.let { category ->
                            onNavigateToCategoriesStatisticsScreen(category.id, category.type)
                        }
                    }
                    CategoryStatisticsItemComponent(state.top2Category, enableClick = true) {
                        state.top2Category?.category?.let { category ->
                            onNavigateToCategoriesStatisticsScreen(category.id, category.type)
                        }
                    }
                    CategoryStatisticsItemComponent(state.top3Category, enableClick = true) {
                        state.top3Category?.category?.let { category ->
                            onNavigateToCategoriesStatisticsScreen(category.id, category.type)
                        }
                    }
                }
                if (state.isEmpty()) {
                    MessageContainer(
                        message = stringResource(R.string.no_data_for_the_selected_filter)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun CategoriesStatisticsWidgetPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        CategoriesStatisticsWidgetContent(
            uiState = CategoriesStatisticsWidgetUiState(
                top1Category = CategoryStatistics(
                    category = Category(
                        id = 1,
                        name = "Food & Drinks",
                        icon = CategoryIcon.FoodAndDrinks,
                        color = CategoryColor.Olive
                    ),
                    totalAmount = "1000.00",
                    percentageFloat = 50f,
                    percentageFormatted = "50%",
                    currency = "USD"
                ),
                top2Category = CategoryStatistics(
                    category = Category(
                        id = 2,
                        name = "Housing",
                        icon = CategoryIcon.Housing,
                        color = CategoryColor.Camel
                    ),
                    totalAmount = "500.00",
                    percentageFloat = 25f,
                    percentageFormatted = "25%",
                    currency = "USD"
                ),
            ),
            onNavigateToCategoriesStatisticsScreen = { _, _ -> }
        )
    }
}