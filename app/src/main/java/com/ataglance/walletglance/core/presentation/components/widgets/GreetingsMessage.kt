package com.ataglance.walletglance.core.presentation.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsCompact
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsExpanded

@Composable
fun GreetingsMessage(messageRes: Int) {

    Row(
        horizontalArrangement = if (WindowTypeIsCompact) Arrangement.Start else Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth(if (!WindowTypeIsExpanded) .84f else .42f)
            .padding(top = dimensionResource(R.dimen.nav_widget_gap))
    ) {
        Text(
            text = stringResource(messageRes),
            color = GlanceColors.onSurface,
            fontSize = 29.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp
        )
    }
}