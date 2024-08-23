package com.ataglance.walletglance.core.presentation.components.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.CategoryStatisticsElementUiState
import com.ataglance.walletglance.category.domain.CategoryStatisticsLists
import com.ataglance.walletglance.category.domain.color.CategoryColors
import com.ataglance.walletglance.category.domain.icons.CategoryIcon
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.WalletGlanceTheme
import com.ataglance.walletglance.category.presentation.components.CategoryStatisticsItemComponent
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface
import com.ataglance.walletglance.core.presentation.components.containers.MessageContainer

@Composable
fun CategoriesStatisticsWidget(
    categoryStatisticsLists: CategoryStatisticsLists,
    appTheme: AppTheme?,
    onNavigateToCategoriesStatisticsScreen: (Int) -> Unit,
) {
    GlassSurface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.categories),
                color = GlanceTheme.onSurface,
                fontSize = 24.sp,
                fontWeight = FontWeight.Light
            )
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
                        CategoryStatisticsItemComponent(firstCategory, appTheme, enableClick = true) {
                            firstCategory?.categoryId?.let(onNavigateToCategoriesStatisticsScreen)
                        }
                        CategoryStatisticsItemComponent(secondCategory, appTheme, enableClick = true) {
                            secondCategory?.categoryId?.let(onNavigateToCategoriesStatisticsScreen)
                        }
                        CategoryStatisticsItemComponent(thirdCategory, appTheme, enableClick = true) {
                            thirdCategory?.categoryId?.let(onNavigateToCategoriesStatisticsScreen)
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
}

@Preview
@Composable
private fun CategoriesStatisticsWidgetPreview() {
    BoxWithConstraints {
        WalletGlanceTheme(boxWithConstraintsScope = this) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.main_background_light),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                CategoriesStatisticsWidget(
                    categoryStatisticsLists = CategoryStatisticsLists(
                        expense = listOf(
                            CategoryStatisticsElementUiState(
                                categoryId = 1,
                                categoryName = "Food & Drinks",
                                categoryIconRes = CategoryIcon.FoodAndDrinks.res,
                                categoryColor = CategoryColors.Olive.color,
                                totalAmount = "1000.00",
                                percentage = 50f,
                                currency = "USD"
                            ),
                            CategoryStatisticsElementUiState(
                                categoryId = 2,
                                categoryName = "Housing",
                                categoryIconRes = CategoryIcon.Housing.res,
                                categoryColor = CategoryColors.Camel.color,
                                totalAmount = "500.00",
                                percentage = 25f,
                                currency = "USD"
                            )
                        ),
                        income = emptyList()
                    ),
                    appTheme = AppTheme.LightDefault,
                    onNavigateToCategoriesStatisticsScreen = {}
                )
            }
        }
    }
}
