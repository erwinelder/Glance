package com.ataglance.walletglance.errorHandling.presentation.components.containers

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.LocalWindowType
import com.ataglance.walletglance.core.presentation.Typography
import com.ataglance.walletglance.core.presentation.components.other.IconWithBackground

@Composable
fun SuccessMessageWithIcon(
    message: String,
    @DrawableRes iconRes: Int = R.drawable.success_large_icon,
    iconDescription: String = "Success",
    iconBackgroundGradient: List<Color> = GlanceColors.primaryGradient
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        IconWithBackground(
            iconRes = iconRes,
            backgroundGradient = iconBackgroundGradient,
            iconDescription = iconDescription
        )
        Text(
            text = message,
            style = Typography.titleLarge,
            color = GlanceColors.onSurface,
            modifier = Modifier
                .fillMaxWidth(FilledWidthByScreenType().getByType(LocalWindowType.current))
        )
    }
}