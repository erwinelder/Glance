package com.ataglance.walletglance.presentation.ui.uielements.containers

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
import com.ataglance.walletglance.domain.date.DateRangeAssets
import com.ataglance.walletglance.domain.date.DateRangeEnum
import com.ataglance.walletglance.domain.utils.formatDateRangeForCustomDateRangeField
import com.ataglance.walletglance.presentation.ui.GlanceTheme
import com.ataglance.walletglance.presentation.ui.Manrope
import com.ataglance.walletglance.presentation.ui.animation.bounceClickEffect
import com.ataglance.walletglance.presentation.ui.uielements.buttons.SmallPrimaryButton
import com.ataglance.walletglance.presentation.ui.uielements.dividers.SmallDivider
import com.ataglance.walletglance.presentation.ui.uielements.fields.DateField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.GlanceDateRangeAssetsPicker(
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
                    .background(GlanceTheme.background)
                    /*.innerShadow(
                        shape = RoundedCornerShape(dimensionResource(R.dimen.record_corner_size)),
                        color = GlanceTheme.onSurface.copy(.25f),
                        offsetX = 10.dp,
                        offsetY = -(10).dp,
                        blur = 20.dp
                    )
                    .innerShadow(
                        shape = RoundedCornerShape(dimensionResource(R.dimen.record_corner_size)),
                        color = GlanceTheme.onPrimary,
                        offsetX = -(10).dp,
                        offsetY = 10.dp,
                        blur = 20.dp
                    )*/
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
                    dateFormatted = formatDateRangeForCustomDateRangeField(
                        dateRangePickerState.selectedStartDateMillis,
                        dateRangePickerState.selectedEndDateMillis
                    ),
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
            GlanceTheme.primary
        } else {
            GlanceTheme.onSurface
        },
        label = "custom date range enum button color"
    )

    Text(
        text = stringResource(dateRangeAssets.nameRes),
        color = color,
        fontSize = 19.sp,
        fontFamily = Manrope,
        modifier = Modifier
            .bounceClickEffect {
                onSelect(dateRangeAssets.enum)
            }
    )
}