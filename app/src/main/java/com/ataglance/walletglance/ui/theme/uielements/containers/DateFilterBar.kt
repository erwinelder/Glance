package com.ataglance.walletglance.ui.theme.uielements.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
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
        BarButton(
            onClick = onCustomDateRangeButtonClick,
            active = isCustomDateRangeWindowOpened ||
                    (currentDateRangeEnum != DateRangeEnum.ThisMonth &&
                            currentDateRangeEnum != DateRangeEnum.LastMonth),
            text = stringResource(R.string.other)
        )
    }
}