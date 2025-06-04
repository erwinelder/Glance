package com.ataglance.walletglance.core.presentation.component.picker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.date.DateRangeAssets
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.TimeInMillisRange
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.divider.SmallDivider
import com.ataglance.walletglance.core.presentation.component.field.DateField
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.DateRangeAssetsPickerComponent(
    visible: Boolean,
    padding: PaddingValues,
    currentDateRangeEnum: DateRangeEnum,
    dateRangePickerState: DateRangePickerState,
    onDismissRequest: () -> Unit,
    onDateRangeSelect: (DateRangeEnum) -> Unit,
    onCustomDateRangeFieldClick: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally {
            (it * 1.12).toInt()
        } + scaleIn(initialScale = .8f),
        exit = slideOutHorizontally {
            (it * 1.12).toInt()
        } + scaleOut(targetScale = .8f),
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(padding)
    ) {
        Dialog(onDismissRequest = onDismissRequest) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.record_corner_size)))
                    .background(GlanciColors.background)
                    .padding(20.dp, 16.dp)
            ) {
                DateRangeAssetComponent(DateRangeAssets.ThisWeek, currentDateRangeEnum, onDateRangeSelect)
                DateRangeAssetComponent(DateRangeAssets.SevenDays, currentDateRangeEnum, onDateRangeSelect)
                DateRangeAssetComponent(DateRangeAssets.ThisYear, currentDateRangeEnum, onDateRangeSelect)
                DateRangeAssetComponent(DateRangeAssets.LastYear, currentDateRangeEnum, onDateRangeSelect)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DateRangeAssetComponent(DateRangeAssets.January, currentDateRangeEnum, onDateRangeSelect)
                    DateRangeAssetComponent(DateRangeAssets.February, currentDateRangeEnum, onDateRangeSelect)
                    DateRangeAssetComponent(DateRangeAssets.March, currentDateRangeEnum, onDateRangeSelect)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DateRangeAssetComponent(DateRangeAssets.April, currentDateRangeEnum, onDateRangeSelect)
                    DateRangeAssetComponent(DateRangeAssets.May, currentDateRangeEnum, onDateRangeSelect)
                    DateRangeAssetComponent(DateRangeAssets.June, currentDateRangeEnum, onDateRangeSelect)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DateRangeAssetComponent(DateRangeAssets.July, currentDateRangeEnum, onDateRangeSelect)
                    DateRangeAssetComponent(DateRangeAssets.August, currentDateRangeEnum, onDateRangeSelect)
                    DateRangeAssetComponent(DateRangeAssets.September, currentDateRangeEnum, onDateRangeSelect)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DateRangeAssetComponent(DateRangeAssets.October, currentDateRangeEnum, onDateRangeSelect)
                    DateRangeAssetComponent(DateRangeAssets.November, currentDateRangeEnum, onDateRangeSelect)
                    DateRangeAssetComponent(DateRangeAssets.December, currentDateRangeEnum, onDateRangeSelect)
                }
                DateField(
                    formattedDate = TimeInMillisRange.from(
                        dateRangePickerState.selectedStartDateMillis,
                        dateRangePickerState.selectedEndDateMillis
                    )?.formatDateRangeByTimeInMillis() ?: "??? - ???",
                    onClick = onCustomDateRangeFieldClick
                )
                SmallDivider()
                SmallPrimaryButton(
                    onClick = onConfirmButtonClick,
                    text = stringResource(R.string.confirm)
                )
            }
        }
    }
}

@Composable
private fun DateRangeAssetComponent(
    dateRangeAssets: DateRangeAssets,
    currentDateRangeEnum: DateRangeEnum,
    onSelect: (DateRangeEnum) -> Unit
) {
    val color by animateColorAsState(
        targetValue = if (dateRangeAssets.enum == currentDateRangeEnum) {
            GlanciColors.primary
        } else {
            GlanciColors.onSurface
        }
    )

    Text(
        text = stringResource(dateRangeAssets.nameRes),
        color = color,
        fontSize = 19.sp,
        fontFamily = Manrope,
        modifier = Modifier.bounceClickEffect {
            onSelect(dateRangeAssets.enum)
        }
    )
}