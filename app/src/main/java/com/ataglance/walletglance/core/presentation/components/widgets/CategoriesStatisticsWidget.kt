package com.ataglance.walletglance.core.presentation.components.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryIcon
import com.ataglance.walletglance.category.domain.model.CategoryColor
import com.ataglance.walletglance.category.presentation.components.CategoryStatisticsItemComponent
import com.ataglance.walletglance.category.presentation.model.CategoryStatisticsElementUiState
import com.ataglance.walletglance.category.presentation.model.CategoryStatisticsLists
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.containers.MessageContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer

@Composable
fun CategoriesStatisticsWidget(
    categoryStatisticsLists: CategoryStatisticsLists,
    onNavigateToCategoriesStatisticsScreen: (Int) -> Unit,
) {
    WidgetWithTitleAndButtonComponent(
        title = stringResource(R.string.categories),
        onBottomNavigationButtonClick = {
            onNavigateToCategoriesStatisticsScreen(0)
        }
    ) {
        AnimatedContent(
            targetState = Triple(
                categoryStatisticsLists.expense.getOrNull(0),
                categoryStatisticsLists.expense.getOrNull(1),
                categoryStatisticsLists.expense.getOrNull(2)
            ),
            label = "top 3 expense categories"
        ) { (firstCategory, secondCategory, thirdCategory) ->
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CategoryStatisticsItemComponent(firstCategory, enableClick = true) {
                        firstCategory?.category?.id?.let(onNavigateToCategoriesStatisticsScreen)
                    }
                    CategoryStatisticsItemComponent(secondCategory, enableClick = true) {
                        secondCategory?.category?.id?.let(onNavigateToCategoriesStatisticsScreen)
                    }
                    CategoryStatisticsItemComponent(thirdCategory, enableClick = true) {
                        thirdCategory?.category?.id?.let(onNavigateToCategoriesStatisticsScreen)
                    }
                }
                if (firstCategory == null) {
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
        CategoriesStatisticsWidget(
            categoryStatisticsLists = CategoryStatisticsLists(
                expense = listOf(
                    CategoryStatisticsElementUiState(
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
                    CategoryStatisticsElementUiState(
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
                    )
                ),
                income = emptyList()
            ),
            onNavigateToCategoriesStatisticsScreen = {}
        )
    }
}
