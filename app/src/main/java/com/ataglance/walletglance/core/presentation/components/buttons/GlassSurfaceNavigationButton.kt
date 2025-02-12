package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun GlassSurfaceNavigationButton(
    text: String,
    @DrawableRes imageRes: Int,
    showRightIconInsteadOfLeft: Boolean = true,
    @DrawableRes rightIconRes: Int = R.drawable.short_arrow_right_icon,
    filledWidths: FilledWidthByScreenType = FilledWidthByScreenType(1f, .75f, .75f),
    onClick: () -> Unit
) {
    GlassSurface(
        filledWidths = filledWidths,
        cornerSize = dimensionResource(R.dimen.settings_category_plate_corner_size),
        modifier = Modifier.bounceClickEffect(.98f, onClick = onClick)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            if (!showRightIconInsteadOfLeft) {
                Icon(
                    painter = painterResource(R.drawable.short_arrow_left_icon),
                    contentDescription = "left arrow icon",
                    tint = GlanceColors.onSurface,
                    modifier = Modifier.size(26.dp)
                )
            }
            Image(
                painter = painterResource(imageRes),
                contentDescription = "$text icon",
                modifier = Modifier.size(46.dp)
            )
            Text(
                text = text,
                color = GlanceColors.onSurface,
                fontSize = 21.sp,
                fontFamily = Manrope,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            if (showRightIconInsteadOfLeft) {
                Icon(
                    painter = painterResource(rightIconRes),
                    contentDescription = "right arrow icon",
                    tint = GlanceColors.onSurface,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
