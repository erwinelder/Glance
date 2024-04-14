package com.ataglance.walletglance.ui.theme.uielements.containers

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
import com.ataglance.walletglance.model.DateRange
import com.ataglance.walletglance.model.DateRangeController
import com.ataglance.walletglance.model.DateRangeEnum
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.Manrope
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.uielements.buttons.SmallPrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.dividers.SmallDivider
import com.ataglance.walletglance.ui.theme.uielements.fields.DateField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.CustomDateRangeWindow(
    visible: Boolean,
    padding: PaddingValues,
    currentDateRangeEnum: DateRangeEnum,
    dateRangePickerState: DateRangePickerState,
    onDismissRequest: () -> Unit,
    onDateRangeEnumClick: (DateRangeEnum) -> Unit,
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
                CustomDateRangeComponent(DateRange.ThisWeek, currentDateRangeEnum, onDateRangeEnumClick)
                CustomDateRangeComponent(DateRange.SevenDays, currentDateRangeEnum, onDateRangeEnumClick)
                CustomDateRangeComponent(DateRange.ThisYear, currentDateRangeEnum, onDateRangeEnumClick)
                CustomDateRangeComponent(DateRange.LastYear, currentDateRangeEnum, onDateRangeEnumClick)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomDateRangeComponent(DateRange.January, currentDateRangeEnum, onDateRangeEnumClick)
                    CustomDateRangeComponent(DateRange.February, currentDateRangeEnum, onDateRangeEnumClick)
                    CustomDateRangeComponent(DateRange.March, currentDateRangeEnum, onDateRangeEnumClick)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomDateRangeComponent(DateRange.April, currentDateRangeEnum, onDateRangeEnumClick)
                    CustomDateRangeComponent(DateRange.May, currentDateRangeEnum, onDateRangeEnumClick)
                    CustomDateRangeComponent(DateRange.June, currentDateRangeEnum, onDateRangeEnumClick)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomDateRangeComponent(DateRange.July, currentDateRangeEnum, onDateRangeEnumClick)
                    CustomDateRangeComponent(DateRange.August, currentDateRangeEnum, onDateRangeEnumClick)
                    CustomDateRangeComponent(DateRange.September, currentDateRangeEnum, onDateRangeEnumClick)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomDateRangeComponent(DateRange.October, currentDateRangeEnum, onDateRangeEnumClick)
                    CustomDateRangeComponent(DateRange.November, currentDateRangeEnum, onDateRangeEnumClick)
                    CustomDateRangeComponent(DateRange.December, currentDateRangeEnum, onDateRangeEnumClick)
                }
                DateField(
                    dateFormatted = DateRangeController().formatDateRangeForCustomDateRangeField(
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
private fun CustomDateRangeComponent(
    dateRange: DateRange,
    currentDateRangeEnum: DateRangeEnum,
    onClick: (DateRangeEnum) -> Unit
) {
    val color by animateColorAsState(
        targetValue = if (dateRange.enum == currentDateRangeEnum) {
            GlanceTheme.primary
        } else {
            GlanceTheme.onSurface
        },
        label = "custom date range enum button color"
    )

    Text(
        text = stringResource(dateRange.nameRes),
        color = color,
        fontSize = 19.sp,
        fontFamily = Manrope,
        modifier = Modifier
            .bounceClickEffect(.97f) {
                onClick(dateRange.enum)
            }
    )
}