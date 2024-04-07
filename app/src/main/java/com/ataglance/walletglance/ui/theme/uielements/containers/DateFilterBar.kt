package com.ataglance.walletglance.ui.theme.uielements.containers

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.model.DateRange
import com.ataglance.walletglance.model.DateRangeEnum
import com.ataglance.walletglance.ui.theme.uielements.buttons.BarButton

@Composable
fun DateFilterBar(
    currentDateRangeEnum: DateRangeEnum,
    isCustomDateRangeWindowOpened: Boolean,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    onCustomDateRangeButtonClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
            BarButton(
                onClick = {
                    onDateRangeChange(DateRange.ThisMonth.enum)
                    if (isCustomDateRangeWindowOpened) {
                        onCustomDateRangeButtonClick()
                    }
                },
                active = currentDateRangeEnum == DateRangeEnum.ThisMonth &&
                        !isCustomDateRangeWindowOpened,
                text = stringResource(DateRange.ThisMonth.nameRes)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BarButton(
                onClick = {
                    onDateRangeChange(DateRange.LastMonth.enum)
                    if (isCustomDateRangeWindowOpened) {
                        onCustomDateRangeButtonClick()
                    }
                },
                active = currentDateRangeEnum == DateRangeEnum.LastMonth &&
                        !isCustomDateRangeWindowOpened,
                text = stringResource(DateRange.LastMonth.nameRes)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BarButton(
                onClick = onCustomDateRangeButtonClick,
                active = isCustomDateRangeWindowOpened ||
                        (currentDateRangeEnum != DateRangeEnum.ThisMonth &&
                                currentDateRangeEnum != DateRangeEnum.LastMonth),
                text = stringResource(R.string.other)
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
        }
    }
}