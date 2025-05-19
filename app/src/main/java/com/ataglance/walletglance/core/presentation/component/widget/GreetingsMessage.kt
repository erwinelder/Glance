package com.ataglance.walletglance.core.presentation.component.widget

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.Typography
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsCompact
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.core.utils.getCurrentLocalDateTime
import com.ataglance.walletglance.core.utils.getGreetingsWidgetTitleRes

@Composable
fun GreetingsMessage() {
    val currentLocalDateTime = getCurrentLocalDateTime()
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
            .padding(top = 8.dp)
    ) {
        Text(
            text = message,
            color = GlanceColors.onSurface,
            style = Typography.titleMedium
        )
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun GreetingsMessagePreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        GreetingsMessageContent(message = "Good afternoon!")
    }
}