package com.ataglance.walletglance.core.presentation.components.containers

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
import com.ataglance.walletglance.core.domain.date.DateRangeAssets
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.presentation.components.buttons.BarButton

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
                text = stringResource(DateRangeAssets.ThisMonth.nameRes),
                active = currentDateRangeEnum == DateRangeEnum.ThisMonth &&
                        !isCustomDateRangeWindowOpened
            ) {
                onDateRangeChange(DateRangeAssets.ThisMonth.enum)
                if (isCustomDateRangeWindowOpened) {
                    onCustomDateRangeButtonClick()
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            BarButton(
                text = stringResource(DateRangeAssets.LastMonth.nameRes),
                active = currentDateRangeEnum == DateRangeEnum.LastMonth &&
                        !isCustomDateRangeWindowOpened
            ) {
                onDateRangeChange(DateRangeAssets.LastMonth.enum)
                if (isCustomDateRangeWindowOpened) {
                    onCustomDateRangeButtonClick()
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            BarButton(
                text = stringResource(R.string.other),
                active = isCustomDateRangeWindowOpened ||
                        (currentDateRangeEnum != DateRangeEnum.ThisMonth &&
                                currentDateRangeEnum != DateRangeEnum.LastMonth),
                onClick = onCustomDateRangeButtonClick
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
        }
    }
}