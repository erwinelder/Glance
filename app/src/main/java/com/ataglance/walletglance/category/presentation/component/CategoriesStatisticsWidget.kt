package com.ataglance.walletglance.category.presentation.component

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
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
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
import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.core.presentation.component.container.MessageContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.component.widget.component.WidgetViewAllButton
import com.ataglance.walletglance.core.presentation.component.widget.container.WidgetContainer
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CategoriesStatisticsWidgetWrapper(
    activeAccount: Account?,
    activeDateRange: TimestampRange,
    onNavigateToCategoriesStatisticsScreen: (Int?, CategoryType?) -> Unit
) {
    val viewModel = koinViewModel<CategoryStatisticsWidgetViewModel> {
        parametersOf(activeAccount, activeDateRange)
    }

    LaunchedEffect(activeAccount) {
        activeAccount?.id?.let(viewModel::setActiveAccountId)
    }
    LaunchedEffect(activeDateRange) {
        viewModel.setActiveDateRange(dateRange = activeDateRange)
    }

    val uiState by viewModel.statisticsUiState.collectAsStateWithLifecycle()

    CategoriesStatisticsWidget(
        uiState = uiState,
        onNavigateToCategoriesStatisticsScreen = onNavigateToCategoriesStatisticsScreen
    )
}

@Composable
fun CategoriesStatisticsWidget(
    uiState: CategoriesStatisticsWidgetUiState,
    onNavigateToCategoriesStatisticsScreen: (Int?, CategoryType?) -> Unit
) {
    WidgetContainer(
        title = stringResource(R.string.categories),
        buttonsBlock = {
            WidgetViewAllButton {
                onNavigateToCategoriesStatisticsScreen(null, uiState.getType())
            }
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
                    CategoryStatisticsOnGlassComponent(state.top1Category, enableClick = true) {
                        state.top1Category?.category?.let { category ->
                            onNavigateToCategoriesStatisticsScreen(category.id, category.type)
                        }
                    }
                    CategoryStatisticsOnGlassComponent(state.top2Category, enableClick = true) {
                        state.top2Category?.category?.let { category ->
                            onNavigateToCategoriesStatisticsScreen(category.id, category.type)
                        }
                    }
                    CategoryStatisticsOnGlassComponent(state.top3Category, enableClick = true) {
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


@Preview(device = PIXEL_7_PRO)
@Composable
fun CategoriesStatisticsWidgetPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewContainer(appTheme = appTheme) {
        CategoriesStatisticsWidget(
            uiState = CategoriesStatisticsWidgetUiState(
                top1Category = CategoryStatistics(
                    category = Category(
                        id = 1,
                        name = "Food & Drinks",
                        icon = CategoryIcon.FoodAndDrinks,
                        color = CategoryColor.Olive
                    ),
                    totalAmount = "1000.00",
                    percentageFloat = .75f,
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
                    percentageFloat = .4f,
                    percentageFormatted = "25%",
                    currency = "USD"
                ),
            ),
            onNavigateToCategoriesStatisticsScreen = { _, _ -> }
        )
    }
}