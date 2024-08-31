package com.ataglance.walletglance.recordCreation.presentation.components

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
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SmallPrimaryButton

@Composable
fun MakeRecordBottomButtonBlock(
    showOnlySaveButton: Boolean,
    @StringRes singlePrimaryButtonStringRes: Int,
    onSaveButton: () -> Unit,
    onRepeatButton: () -> Unit,
    onDeleteButton: () -> Unit,
    savingAndRepeatingAreAllowed: Boolean = true
) {
    if (showOnlySaveButton) {
        PrimaryButton(
            onClick = onSaveButton,
            text = stringResource(singlePrimaryButtonStringRes),
            enabled = savingAndRepeatingAreAllowed
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
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                SmallPrimaryButton(
                    onClick = onSaveButton,
                    text = stringResource(R.string.save),
                    enabled = savingAndRepeatingAreAllowed,
                    fontSize = 18.sp
                )
                SmallPrimaryButton(
                    onClick = onRepeatButton,
                    text = stringResource(R.string.repeat),
                    enabled = savingAndRepeatingAreAllowed,
                    fontSize = 18.sp
                )
                SmallPrimaryButton(
                    onClick = onDeleteButton,
                    text = stringResource(R.string.delete),
                    fontSize = 18.sp
                )
            }
        }
    }
}