package com.ataglance.walletglance.core.presentation.component.bottomSheet

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
import com.ataglance.walletglance.core.presentation.component.other.LargePrimaryIcon
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.GlanciTypography
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.presentation.theme.NotoSans

@Composable
fun BottomSheetContentDialogComponent(
    title: String,
    titleColor: Color = GlanciColors.onSurface,
    message: String?,
    @DrawableRes iconRes: Int,
    iconDescription: String,
    iconGradientColor: List<Color> = GlanciColors.primaryGradient,
    bottomBlock: @Composable (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        LargePrimaryIcon(
            iconRes = iconRes,
            gradientColor = iconGradientColor,
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
                color = titleColor,
                style = GlanciTypography.titleLarge,
                fontSize = 28.sp,
                lineHeight = 32.sp,
                fontFamily = NotoSans
            )
            message?.let {
                Text(
                    text = it,
                    fontSize = 18.sp,
                    color = GlanciColors.onSurface,
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.Center,
                    fontFamily = Manrope,
                    lineHeight = 32.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        bottomBlock?.invoke()
    }

}