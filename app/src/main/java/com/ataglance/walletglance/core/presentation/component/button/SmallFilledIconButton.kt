package com.ataglance.walletglance.core.presentation.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@Composable
fun SmallFilledIconButton(
    iconRes: Int,
    iconContendDescription: String,
    gradientColor: List<Color> = GlanciColors.glassGradientOnGlass,
    contentColor: Color = GlanciColors.onSurface,
    size: Dp = 24.dp,
    borderSize: Dp = 1.dp,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    GlassSurfaceOnGlassSurface(
        onClick = onClick,
        clickEnabled = enabled,
        shrinkScale = .96f,
        cornerSize = 18.dp,
        borderSize = borderSize,
        contentPadding = PaddingValues(12.dp),
        gradientColor = gradientColor
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = iconContendDescription,
            tint = contentColor,
            modifier = Modifier.size(size)
        )
    }
}