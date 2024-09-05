package com.ataglance.walletglance.record.presentation.components

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.RecordAccount
import com.ataglance.walletglance.core.presentation.CurrAppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.utils.convertDateLongToDayMonthYear
import com.ataglance.walletglance.record.domain.RecordStack

@Composable
fun TransferComponent(
    recordStack: RecordStack,
    secondAccount: RecordAccount?,
    includeYearToDate: Boolean,
    onTransferClick: (Int) -> Unit
) {
    GlassSurfaceOnGlassSurface(onClick = { onTransferClick(recordStack.recordNum) }) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = convertDateLongToDayMonthYear(
                    date = recordStack.date,
                    context = LocalContext.current,
                    includeYear = includeYearToDate
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
                    if (recordStack.isOutTransfer()) R.string.to_account_meaning
                    else R.string.from_account_meaning
                ),
                color = GlanceTheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = secondAccount?.name ?: "---",
                color = secondAccount?.color?.colorOn?.getByTheme(CurrAppTheme)
                    ?: Color.Transparent,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .clip(RoundedCornerShape(42))
                    .background(
                        secondAccount?.color?.color?.getByTheme(CurrAppTheme)?.lighter
                            ?: Color.Transparent
                    )
                    .padding(7.dp, 3.dp)
            )
        }
        Text(
            text = recordStack.getFormattedAmountWithSpaces(),
            color = GlanceTheme.onSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light
        )
    }
}