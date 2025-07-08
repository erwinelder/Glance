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
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
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
import com.ataglance.walletglance.core.domain.widget.ExpensesIncomeWidgetUiState
import com.ataglance.walletglance.core.presentation.component.chart.LineChartComponent
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.divider.BigDivider
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.component.widget.component.WidgetTitleComponent
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.presentation.viewmodel.ExpensesIncomeWidgetViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

@Composable
fun ExpensesIncomeWidgetWrapper(
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
        viewModel.setActiveDateRange(dateRange = dateRangeWithEnum.dateRange)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExpensesIncomeWidget(
        uiState = uiState,
        dateRangeWithEnum = dateRangeWithEnum,
        accountCurrency = activeAccount?.currency ?: "???",
        resourceManager = resourceManager
    )
}

@Composable
fun ExpensesIncomeWidget(
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

    GlassSurface(
        filledWidths = FilledWidthByScreenType(.84f, .63f, .4f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 20.dp)
        ) {
            AnimatedContent(targetState = period) { targetPeriod ->
                WidgetTitleComponent(
                    title = targetPeriod,
                    lineHeight = 32.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedContent(
                targetState = stringResource(R.string.total) +
                        " ${uiState.getTotalFormatted()} $accountCurrency"
            ) { targetTotalExpensesAndIncome ->
                Text(
                    text = targetTotalExpensesAndIncome,
                    color = GlanciColors.onSurface,
                    fontSize = 22.sp,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.W400
                )
            }
            BigDivider(Modifier.padding(vertical = 16.dp))
            StatisticBlock(
                titleRes = R.string.income_plural,
                percentage = uiState.incomePercentage,
                percentageFloat = incomePercentage,
                totalStringRow = "+ ${uiState.getIncomeTotalFormatted()} $accountCurrency",
                gradientColorsPair = GlanciColors.lineChartGreenGradientPair
            )
            Spacer(modifier = Modifier.height(16.dp))
            StatisticBlock(
                titleRes = R.string.expenses,
                percentage = uiState.expensesPercentage,
                percentageFloat = expensesPercentage,
                totalStringRow = "- ${uiState.getExpensesTotalFormatted()} $accountCurrency",
                gradientColorsPair = GlanciColors.lineChartRedGradientPair
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
                color = GlanciColors.onSurface,
                fontSize = 20.sp,
                fontFamily = Manrope,
                fontWeight = FontWeight.W400
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        AnimatedContent(targetState = totalStringRow) { targetContent ->
            Text(
                text = targetContent,
                color = GlanciColors.onSurface,
                fontSize = 20.sp,
                fontFamily = Manrope,
                fontWeight = FontWeight.W400
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LineChartComponent(
            percentage = percentageFloat,
            brushColors = gradientColorsPair.toList(),
            shadowColor = gradientColorsPair.first,
            height = 16.dp
        )
    }
}

@Preview(device = PIXEL_7_PRO)
@Composable
fun ExpensesIncomeWidgetPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewContainer(appTheme = appTheme) {
        ExpensesIncomeWidget(
            uiState = ExpensesIncomeWidgetUiState(
                incomeTotal = 1000.0,
                expensesTotal = 500.0,
                expensesPercentage = 33.33,
                incomePercentage = 66.67,
                expensesPercentageFloat = 0.8f,
                incomePercentageFloat = 0.67f
            ),
            dateRangeWithEnum = DateRangeWithEnum.fromEnum(
                enum = DateRangeEnum.ThisMonth
            ),
            accountCurrency = "USD",
            resourceManager = ResourceManagerImpl(LocalContext.current)
        )
    }
}