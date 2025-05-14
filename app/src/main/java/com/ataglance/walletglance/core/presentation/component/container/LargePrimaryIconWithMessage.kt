package com.ataglance.walletglance.core.presentation.component.container

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.other.LargePrimaryIcon
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.GlanceTypography
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.presentation.theme.NotoSans

@Composable
fun LargePrimaryIconWithMessage(
    title: String,
    message: String? = null,
    @DrawableRes iconRes: Int,
    iconDescription: String,
    gradientColor: List<Color> = GlanceColors.primaryGradient,
    filledWidth: FilledWidthByScreenType? = FilledWidthByScreenType(compact = .86f)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(filledWidth?.getByType(CurrWindowType) ?: 1f)
    ) {
        LargePrimaryIcon(
            iconRes = iconRes,
            gradientColor = gradientColor,
            iconDescription = iconDescription
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = GlanceTypography.titleLarge,
            color = GlanceColors.onSurface,
            fontFamily = NotoSans,
            modifier = Modifier
                .fillMaxWidth(FilledWidthByScreenType().getByType(CurrWindowType))
        )
        Spacer(modifier = Modifier.height(24.dp))
        message?.let {
            Text(
                text = message,
                color = GlanceColors.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400,
                fontFamily = Manrope,
                textAlign = TextAlign.Center
            )
        }
    }
}