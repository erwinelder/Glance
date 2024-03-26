package com.ataglance.walletglance.ui.theme.widgets

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.model.DateRange
import com.ataglance.walletglance.model.DateRangeController
import com.ataglance.walletglance.model.DateRangeEnum
import com.ataglance.walletglance.model.DateRangeState
import com.ataglance.walletglance.model.ExpensesIncomeWidgetUiState
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import com.ataglance.walletglance.ui.theme.uielements.dividers.BigDivider

@Composable
fun ExpensesIncomeWidget(
    uiState: ExpensesIncomeWidgetUiState,
    appTheme: AppTheme?,
    dateRangeState: DateRangeState,
    accountCurrency: String
) {
    val period = when(dateRangeState.enum) {
        DateRangeEnum.ThisMonth -> stringResource(DateRange.ThisMonth.nameRes)
        DateRangeEnum.LastMonth -> stringResource(DateRange.LastMonth.nameRes)
        DateRangeEnum.ThisWeek -> stringResource(DateRange.ThisWeek.nameRes)
        DateRangeEnum.SevenDays -> stringResource(DateRange.SevenDays.nameRes)
        DateRangeEnum.ThisYear -> stringResource(DateRange.ThisYear.nameRes)
        DateRangeEnum.LastYear -> stringResource(DateRange.LastYear.nameRes)
        DateRangeEnum.January, DateRangeEnum.February, DateRangeEnum.March,
        DateRangeEnum.April, DateRangeEnum.May, DateRangeEnum.June,
        DateRangeEnum.July, DateRangeEnum.August, DateRangeEnum.September,
        DateRangeEnum.October, DateRangeEnum.November, DateRangeEnum.December ->
            dateRangeState.getFormattedMonth(LocalContext.current)
        DateRangeEnum.Custom -> DateRangeController().getFormattedDateFromAndToByFormatDayMonthYear(
            dateRangeState.fromPast, dateRangeState.toFuture, LocalContext.current
        )
    }
    val greenColorGradient = Brush.linearGradient(
        when (appTheme) {
            AppTheme.LightDefault -> listOf(
                Color(173, 206, 153),
                Color(130, 189, 95)
            )
            AppTheme.DarkDefault -> listOf(
                Color(75, 107, 54),
                Color(106, 165, 70)
            )
            else -> listOf(Color.Green, Color.Green)
        }
    )
    val redColorGradient = Brush.linearGradient(
        when (appTheme) {
            AppTheme.LightDefault -> listOf(
                Color(203, 139, 137),
                Color(185, 77, 71)
            )
            AppTheme.DarkDefault -> listOf(
                Color(107, 41, 41),
                Color(160, 53, 47)
            )
            else -> listOf(Color.Red, Color.Red)
        }
    )
    val expensesPercentage by animateFloatAsState(
        targetValue = uiState.expensesPercentageFloat,
        animationSpec = tween(500),
        label = "expenses visualizer width"
    )
    val incomePercentage by animateFloatAsState(
        targetValue = uiState.incomePercentageFloat,
        animationSpec = tween(500),
        label = "income visualizer width"
    )

    GlassSurface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp)
        ) {
            AnimatedContent(
                targetState = period,
                label = "period"
            ) { targetPeriod ->
                Text(
                    text = targetPeriod,
                    color = GlanceTheme.onSurface,
                    fontSize = 25.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            AnimatedContent(
                targetState = stringResource(R.string.total) +
                        " ${uiState.getTotalFormatted()} $accountCurrency",
                label = "total expenses and income for period"
            ) { targetTotalExpensesAndIncome ->
                Text(
                    text = targetTotalExpensesAndIncome,
                    color = GlanceTheme.onSurface,
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
                backgroundBrush = greenColorGradient
            )
            Spacer(modifier = Modifier.height(16.dp))
            StatisticBlock(
                titleRes = R.string.expenses,
                percentage = uiState.expensesPercentage,
                percentageFloat = expensesPercentage,
                totalStringRow = "- ${uiState.getExpensesTotalFormatted()} $accountCurrency",
                backgroundBrush = redColorGradient
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
    backgroundBrush: Brush
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AnimatedContent(
            targetState = "${stringResource(titleRes)} ${"%.2f".format(percentage)}%",
            label = "expenses or income percent for a period"
        ) { targetContent ->
            Text(
                text = targetContent,
                color = GlanceTheme.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        AnimatedContent(
            targetState = totalStringRow,
            label = "total expenses or income for a period"
        ) { targetContent ->
            Text(
                text = targetContent,
                color = GlanceTheme.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(GlanceTheme.glassGradientLightToDark.first)
                .fillMaxWidth()
                .height(20.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(backgroundBrush)
                    .fillMaxWidth(percentageFloat)
                    .height(20.dp)
            )
        }
    }
}