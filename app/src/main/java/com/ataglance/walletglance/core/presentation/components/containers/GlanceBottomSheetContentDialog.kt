package com.ataglance.walletglance.core.presentation.components.containers

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.Manrope
import com.ataglance.walletglance.core.presentation.NotoSans
import com.ataglance.walletglance.core.presentation.components.other.IconWithBackground

@Composable
fun GlanceBottomSheetContentDialog(
    title: String,
    titleColor: Color = GlanceColors.onSurface,
    message: String,
    @DrawableRes iconRes: Int,
    iconDescription: String,
    iconGradientColor: List<Color> = GlanceColors.primaryGradient,
    bottomBlock: @Composable (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        IconWithBackground(
            iconRes = iconRes,
            backgroundGradient = iconGradientColor,
            iconDescription = iconDescription
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(.9f)
        ) {
            Text(
                text = title,
                fontSize = 28.sp,
                color = titleColor,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                fontFamily = NotoSans,
                lineHeight = 32.sp
            )
            Text(
                text = message,
                fontSize = 20.sp,
                color = GlanceColors.onSurface,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontFamily = Manrope,
                lineHeight = 32.sp
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        bottomBlock?.invoke()
    }

}