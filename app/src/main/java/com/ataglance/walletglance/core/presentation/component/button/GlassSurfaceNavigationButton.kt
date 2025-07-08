package com.ataglance.walletglance.core.presentation.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.model.IconOrientation
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.modifier.singleTapLightVibration
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun GlassSurfaceNavigationButton(
    text: String,
    @DrawableRes imageRes: Int? = null,
    iconOrientation: IconOrientation = IconOrientation.Right,
    @DrawableRes rightIconRes: Int = R.drawable.short_arrow_right_icon,
    filledWidths: FilledWidthByScreenType? = FilledWidthByScreenType(1f, .75f, .75f),
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    fontSize: TextUnit = 20.sp,
    cornerSize: Dp = 24.dp,
    onClick: () -> Unit
) {
    GlassSurface(
        filledWidths = filledWidths,
        cornerSize = cornerSize,
        modifier = Modifier
            .singleTapLightVibration()
            .bounceClickEffect(.98f, onClick = onClick)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            if (iconOrientation == IconOrientation.Left) {
                Icon(
                    painter = painterResource(R.drawable.short_arrow_left_icon),
                    contentDescription = "left arrow icon",
                    tint = GlanciColors.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
            imageRes?.let {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = "$text icon",
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = text,
                color = GlanciColors.onSurface,
                fontSize = fontSize,
                fontFamily = Manrope,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            if (iconOrientation == IconOrientation.Right) {
                Icon(
                    painter = painterResource(rightIconRes),
                    contentDescription = "right arrow icon",
                    tint = GlanciColors.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
