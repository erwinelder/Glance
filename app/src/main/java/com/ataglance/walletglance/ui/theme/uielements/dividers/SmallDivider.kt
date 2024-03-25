package com.ataglance.walletglance.ui.theme.uielements.dividers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.ui.theme.GlanceTheme

@Composable
fun SmallDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier
            .fillMaxWidth(.3f)
            .clip(RoundedCornerShape(1.dp)),
        thickness = 1.dp,
        color = GlanceTheme.outline
    )
}