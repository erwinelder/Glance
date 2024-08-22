package com.ataglance.walletglance.presentation.theme.uielements.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.presentation.theme.GlanceTheme

@Composable
fun MakeRecordFieldContainer(labelRes: Int, content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(labelRes),
            color = GlanceTheme.outline,
            fontSize = 16.sp,
            fontWeight = FontWeight.Light
        )
        content()
    }
}