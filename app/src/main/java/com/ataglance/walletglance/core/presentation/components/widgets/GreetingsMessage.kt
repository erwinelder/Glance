package com.ataglance.walletglance.core.presentation.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsCompact
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.core.utils.getGreetingsWidgetTitleRes
import java.time.LocalDateTime

@Composable
fun GreetingsMessage() {
    val currentLocalDateTime = LocalDateTime.now()
    val greetingsTitleRes by remember(currentLocalDateTime.hour) {
        derivedStateOf {
            currentLocalDateTime.hour.getGreetingsWidgetTitleRes()
        }
    }

    GreetingsMessageContent(message = stringResource(greetingsTitleRes))
}

@Composable
fun GreetingsMessageContent(message: String) {
    Row(
        horizontalArrangement = if (WindowTypeIsCompact) Arrangement.Start else Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth(if (!WindowTypeIsExpanded) .84f else .42f)
            .padding(top = dimensionResource(R.dimen.nav_widget_gap))
    ) {
        Text(
            text = message,
            color = GlanceColors.onSurface,
            fontSize = 29.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp
        )
    }
}