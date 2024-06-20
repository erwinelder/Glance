package com.ataglance.walletglance.ui.theme.uielements.records

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
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.ui.theme.uielements.buttons.BarButton

@Composable
fun MakeRecordTypeBar(
    isTransferButtonVisible: Boolean,
    onMakeTransferButtonClick: () -> Unit,
    currentRecordType: RecordType,
    onRecordTypeChange: (RecordType) -> Unit,
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
            if (isTransferButtonVisible) {
                BarButton(
                    onClick = onMakeTransferButtonClick,
                    active = false,
                    text = stringResource(R.string.transfer)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            BarButton(
                onClick = {
                    onRecordTypeChange(RecordType.Expense)
                },
                active = currentRecordType == RecordType.Expense,
                text = stringResource(R.string.expense)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BarButton(
                onClick = {
                    onRecordTypeChange(RecordType.Income)
                },
                active = currentRecordType == RecordType.Income,
                text = stringResource(R.string.income_singular)
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
        }
    }
}