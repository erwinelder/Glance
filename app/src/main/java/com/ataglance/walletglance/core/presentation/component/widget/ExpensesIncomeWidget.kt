package com.ataglance.walletglance.core.presentation.component.widget

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.domain.widgets.ExpensesIncomeWidgetUiState
import com.ataglance.walletglance.core.presentation.component.chart.GlanceLineChart
import com.ataglance.walletglance.core.presentation.component.divider.BigDivider
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.viewmodel.ExpensesIncomeWidgetViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

@Composable
fun ExpensesIncomeWidget(
    activeAccount: Account?,
    dateRangeWithEnum: DateRangeWithEnum
) {
    val resourceManager = koinInject<ResourceManager>()
    val viewModel = koinViewModel<ExpensesIncomeWidgetViewModel> {
        parametersOf(activeAccount, dateRangeWithEnum.dateRange)
    }

    LaunchedEffect(activeAccount) {
        activeAccount?.id?.let(viewModel::setActiveAccountId)
    }
    LaunchedEffect(dateRangeWithEnum.dateRange) {
        viewModel.setActiveDateRange(dateRangeWithEnum.dateRange)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExpensesIncomeWidgetContent(
        uiState = uiState,
        dateRangeWithEnum = dateRangeWithEnum,
        accountCurrency = activeAccount?.currency ?: "???",
        resourceManager = resourceManager
    )
}

@Composable
fun ExpensesIncomeWidgetContent(
    uiState: ExpensesIncomeWidgetUiState,
    dateRangeWithEnum: DateRangeWithEnum,
    accountCurrency: String,
    resourceManager: ResourceManager
) {
    val period = dateRangeWithEnum.getEnumStringRepr(resourceManager)
    val expensesPercentage by animateFloatAsState(
        targetValue = uiState.expensesPercentageFloat,
        animationSpec = tween(500)
    )
    val incomePercentage by animateFloatAsState(
        targetValue = uiState.incomePercentageFloat,
        animationSpec = tween(500)
    )

    WidgetComponent(
        filledWidthByScreenType = FilledWidthByScreenType(.84f, .63f, .4f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp)
        ) {
            AnimatedContent(targetState = period) { targetPeriod ->
                Text(
                    text = targetPeriod,
                    color = GlanceColors.onSurface,
                    fontSize = 24.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            AnimatedContent(
                targetState = stringResource(R.string.total) +
                        " ${uiState.getTotalFormatted()} $accountCurrency"
            ) { targetTotalExpensesAndIncome ->
                Text(
                    text = targetTotalExpensesAndIncome,
                    color = GlanceColors.onSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Light
                )
            }
            BigDivider(Modifier.padding(vertical = 16.dp))
            StatisticBlock(
                titleRes = R.string.income_plural,
                percentage = uiState.incomePercentage,
                percentageFloat = incomePercentage,
                totalStringRow = "+ ${uiState.getIncomeTotalFormatted()} $accountCurrency",
                gradientColorsPair = GlanceColors.lineChartGreenGradientPair
            )
            Spacer(modifier = Modifier.height(16.dp))
            StatisticBlock(
                titleRes = R.string.expenses,
                percentage = uiState.expensesPercentage,
                percentageFloat = expensesPercentage,
                totalStringRow = "- ${uiState.getExpensesTotalFormatted()} $accountCurrency",
                gradientColorsPair = GlanceColors.lineChartRedGradientPair
            )
        }
    }
}

@Composable
private fun StatisticBlock(
    @StringRes titleRes: Int,
    percentage: Double,
    percentageFloat: Float,
    totalStringRow: String,
    gradientColorsPair: Pair<Color, Color>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        AnimatedContent(
            targetState = "${stringResource(titleRes)} ${"%.2f".format(Locale.US, percentage)}%"
        ) { targetContent ->
            Text(
                text = targetContent,
                color = GlanceColors.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        AnimatedContent(targetState = totalStringRow) { targetContent ->
            Text(
                text = targetContent,
                color = GlanceColors.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        GlanceLineChart(
            percentage = percentageFloat,
            brushColors = gradientColorsPair.toList(),
            shadowColor = gradientColorsPair.first,
            height = 16.dp
        )
    }
}

@Preview
@Composable
private fun ExpensesIncomeWidgetPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        ExpensesIncomeWidgetContent(
            uiState = ExpensesIncomeWidgetUiState(
                incomeTotal = 1000.0,
                expensesTotal = 500.0,
                expensesPercentage = 33.33,
                incomePercentage = 66.67,
                expensesPercentageFloat = 0.8f,
                incomePercentageFloat = 0.67f
            ),
            dateRangeWithEnum = DateRangeWithEnum(
                enum = DateRangeEnum.ThisMonth,
                dateRange = LongDateRange(0, 0)
            ),
            accountCurrency = "USD",
            resourceManager = ResourceManagerImpl(LocalContext.current)
        )
    }
}