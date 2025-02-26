package com.ataglance.walletglance.account.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.budget.presentation.screen.BudgetStatisticsScreenPreview

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AccountsFlowRow(
    accountList: List<Account>,
    maxLines: Int = Int.MAX_VALUE,
    fontSize: TextUnit = 18.sp
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
                fontSize = fontSize,
                roundedCornerSize = 12.dp,
                outerPadding = PaddingValues(2.dp),
                showBalance = false
            )
        }
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun GlanceColumnChartPreview() {
    BudgetStatisticsScreenPreview()
}