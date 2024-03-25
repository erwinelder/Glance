package com.ataglance.walletglance.ui.theme.uielements.buttons

import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R

@Composable
fun MakeRecordBottomButtonBlock(
    showSingleButton: Boolean,
    @StringRes singlePrimaryButtonStringRes: Int,
    onSaveButton: () -> Unit,
    onRepeatButton: () -> Unit,
    onDeleteButton: () -> Unit,
    buttonsAreEnabled: Boolean = true
) {
    if (showSingleButton) {
        PrimaryButton(
            onClick = onSaveButton,
            text = stringResource(singlePrimaryButtonStringRes),
            enabled = buttonsAreEnabled
        )
    } else {
        val buttonBlockScrollState = rememberScrollState()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(buttonBlockScrollState)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                SmallPrimaryButton(
                    onClick = onSaveButton,
                    text = stringResource(R.string.save),
                    enabled = buttonsAreEnabled,
                    fontSize = 18.sp
                )
                SmallPrimaryButton(
                    onClick = onRepeatButton,
                    text = stringResource(R.string.repeat),
                    enabled = buttonsAreEnabled,
                    fontSize = 18.sp
                )
                SmallPrimaryButton(
                    onClick = onDeleteButton,
                    text = stringResource(R.string.delete),
                    enabled = buttonsAreEnabled,
                    fontSize = 18.sp
                )
            }
        }
    }
}