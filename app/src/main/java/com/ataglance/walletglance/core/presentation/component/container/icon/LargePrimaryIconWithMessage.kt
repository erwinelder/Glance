package com.ataglance.walletglance.core.presentation.component.container.icon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.GlanciTypography
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.presentation.theme.NotoSans

@Composable
fun LargePrimaryIconWithMessage(
    title: String,
    message: String? = null,
    @DrawableRes iconRes: Int,
    iconDescription: String,
    gradientColor: List<Color> = GlanciColors.primaryGlassGradient,
    filledWidth: FilledWidthByScreenType? = FilledWidthByScreenType(compact = .86f)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(filledWidth?.getByType(CurrWindowType) ?: 1f)
    ) {
        LargePrimaryIcon(
            iconRes = iconRes,
            gradientColor = gradientColor,
            iconDescription = iconDescription
        )
        Text(
            text = title,
            style = GlanciTypography.titleLarge,
            color = GlanciColors.onSurface,
            fontFamily = NotoSans,
            modifier = Modifier
                .fillMaxWidth(FilledWidthByScreenType().getByType(CurrWindowType))
        )
        message?.let {
            Text(
                text = message,
                color = GlanciColors.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400,
                fontFamily = Manrope,
                textAlign = TextAlign.Center
            )
        }
    }
}