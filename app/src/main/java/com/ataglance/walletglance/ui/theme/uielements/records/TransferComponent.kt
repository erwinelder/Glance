package com.ataglance.walletglance.ui.theme.uielements.records

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.model.AccountController
import com.ataglance.walletglance.model.DateRangeController
import com.ataglance.walletglance.model.RecordController
import com.ataglance.walletglance.model.RecordStack
import com.ataglance.walletglance.model.RecordType
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.theme.LighterDarkerColors

@Composable
fun TransferComponent(
    recordStack: RecordStack,
    includeYearToDate: Boolean,
    appTheme: AppTheme?,
    getAccount: (Int) -> Account?,
    onTransferClick: (Int) -> Unit
) {
    val type = RecordController().recordTypeCharToRecordType(recordStack.type)
    val transferFirstPairAccount = getAccount(recordStack.accountId)
    val transferSecondPairAccount = recordStack.stack.firstOrNull()?.note?.let { note ->
        getAccount(note.toInt())
    }
    val accountAndOnAccountColor = transferSecondPairAccount?.let {
        AccountController().getAccountAndOnAccountColor(transferSecondPairAccount.color, appTheme)
    } ?: Pair(LighterDarkerColors(), Color.White)

    RecordContainer({ onTransferClick(recordStack.recordNum) }) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = DateRangeController().convertDateLongToDayMonthYear(
                    recordStack.date, includeYearToDate
                ),
                color = GlanceTheme.outline,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = stringResource(R.string.transfer),
                color = GlanceTheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(
                    if (type == RecordType.Expense) R.string.to_account_meaning
                    else R.string.from_account_meaning
                ),
                color = GlanceTheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = transferSecondPairAccount?.name ?: "---",
                color = accountAndOnAccountColor.second,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .clip(RoundedCornerShape(42))
                    .background(accountAndOnAccountColor.first.lighter)
                    .padding(7.dp, 3.dp)
            )
        }
        Text(
            text = recordStack.getFormattedAmountWithSpaces(transferFirstPairAccount?.currency),
            color = GlanceTheme.onSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light
        )
    }
}