package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.components.buttons.BackButton

@Composable
fun BackButtonBlock(onClick: () -> Unit, text: String = stringResource(R.string.back)) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp)
    ) {
        BackButton(onClick = onClick, text = text)
    }
}