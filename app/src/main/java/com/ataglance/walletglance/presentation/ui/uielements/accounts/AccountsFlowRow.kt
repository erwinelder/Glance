package com.ataglance.walletglance.presentation.ui.uielements.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.presentation.ui.screens.BudgetStatisticsScreenPreview

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AccountsFlowRow(
    accountList: List<Account>,
    appTheme: AppTheme?,
    maxLines: Int = Int.MAX_VALUE
) {
    val nestedScrollInterop = rememberNestedScrollInteropConnection()

    FlowRow(
        horizontalArrangement = Arrangement.Center,
        maxLines = maxLines,
        overflow = FlowRowOverflow.Clip,
        modifier = Modifier.nestedScroll(nestedScrollInterop)
    ) {
        accountList.forEach { account ->
            SmallAccount(
                account = account,
                appTheme = appTheme,
                fontSize = 16.sp,
                roundedCornerSize = 12.dp,
                outerPadding = PaddingValues(2.dp),
                showBalance = false
            )
        }
    }
}


@Preview
@Composable
private fun GlanceColumnChartPreview() {
    BudgetStatisticsScreenPreview()
}