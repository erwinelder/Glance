package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.Manrope
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun GlassSurfaceNavigationButton(
    @StringRes textRes: Int,
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
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            if (!showRightIconInsteadOfLeft) {
                Icon(
                    painter = painterResource(R.drawable.short_arrow_left_icon),
                    contentDescription = "left arrow icon",
                    tint = GlanceTheme.onSurface,
                    modifier = Modifier.size(26.dp)
                )
            }
            Image(
                painter = painterResource(imageRes),
                contentDescription = stringResource(textRes) + " icon",
                modifier = Modifier.size(46.dp)
            )
            Text(
                text = stringResource(textRes),
                color = GlanceTheme.onSurface,
                fontSize = 21.sp,
                textAlign = TextAlign.Center,
                fontFamily = Manrope,
                overflow = TextOverflow.Ellipsis
            )
            if (showRightIconInsteadOfLeft) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(rightIconRes),
                    contentDescription = "right arrow icon",
                    tint = GlanceTheme.onSurface,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
