package com.ataglance.walletglance.core.presentation.component.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.GlanciTypography
import com.ataglance.walletglance.core.presentation.theme.NotoSans
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsCompact
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.core.presentation.utils.getGreetingsWidgetTitleResWithUsername

@Composable
fun GreetingsWidgetWrapper() {
    val titleResWithUsername by getGreetingsWidgetTitleResWithUsername()

    titleResWithUsername.second?.takeIf { it.isNotBlank() }
        ?.let { GreetingsWidget(message = stringResource(titleResWithUsername.first, it)) }
        ?: GreetingsWidget(message = stringResource(titleResWithUsername.first))
}

@Composable
fun GreetingsWidget(message: String) {
    Row(
        horizontalArrangement = if (WindowTypeIsCompact) Arrangement.Start else Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth(if (!WindowTypeIsExpanded) .84f else .42f)
            .padding(top = 8.dp)
    ) {
        Text(
            text = message,
            color = GlanciColors.onSurface,
            style = GlanciTypography.titleMedium,
            textAlign = TextAlign.Start,
            fontFamily = NotoSans
        )
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun GreetingsMessagePreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        GreetingsWidget(message = "Good afternoon, username!")
    }
}