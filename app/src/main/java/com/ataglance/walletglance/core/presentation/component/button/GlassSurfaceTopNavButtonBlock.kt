package com.ataglance.walletglance.core.presentation.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun GlassSurfaceTopNavButtonBlock(
    text: String,
    @DrawableRes imageRes: Int? = null,
    filledWidths: FilledWidthByScreenType = FilledWidthByScreenType(.96f, .75f, .75f),
    onClick: () -> Unit,
    companionComponent: @Composable (RowScope.() -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(filledWidths.get(CurrWindowType))
    ) {
        GlassSurfaceNavigationButton(
            text = text,
            imageRes = imageRes.takeIf { companionComponent == null },
            shrink = companionComponent != null,
            onClick = onClick
        )
        if (companionComponent != null) {
            Spacer(modifier = Modifier.width(8.dp))
        }
        companionComponent?.invoke(this)
    }
}

@Composable
fun GlassSurfaceTopNavButtonBlock(
    text: String,
    iconComponent: @Composable (() -> Unit)? = null,
    filledWidths: FilledWidthByScreenType = FilledWidthByScreenType(.96f, .75f, .75f),
    onClick: () -> Unit,
    companionComponent: @Composable (RowScope.() -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(filledWidths.get(CurrWindowType))
    ) {
        GlassSurfaceNavigationButton(
            text = text,
            iconComponent = iconComponent,
            shrink = companionComponent != null,
            onClick = onClick
        )
        if (companionComponent != null) {
            Spacer(modifier = Modifier.width(8.dp))
        }
        companionComponent?.invoke(this)
    }
}


@Composable
private fun RowScope.GlassSurfaceNavigationButton(
    text: String,
    @DrawableRes imageRes: Int? = null,
    shrink: Boolean = false,
    onClick: () -> Unit
) {
    GlassSurfaceNavigationButton(
        text = text,
        iconComponent = if (imageRes != null && !shrink) { {
            Image(
                painter = painterResource(imageRes),
                contentDescription = "$text icon",
                modifier = Modifier.size(32.dp)
            )
        } } else null,
        shrink = shrink,
        onClick = onClick
    )
}

@Composable
private fun RowScope.GlassSurfaceNavigationButton(
    text: String,
    iconComponent: @Composable (() -> Unit)? = null,
    shrink: Boolean = false,
    onClick: () -> Unit
) {
    GlassSurface(
        filledWidths = null,
        cornerSize = if (shrink || iconComponent == null) 22.dp else 24.dp,
        modifier = Modifier
            .bounceClickEffect(.98f, onClick = onClick)
            .weight(1f, fill = !shrink)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.short_arrow_left_icon),
                contentDescription = "left arrow icon",
                tint = GlanciColors.onSurface,
                modifier = Modifier.height(20.dp)
            )
            if (iconComponent != null && !shrink) {
                iconComponent()
            }
            Text(
                text = if (shrink) stringResource(R.string.back) else text,
                color = GlanciColors.onSurface,
                fontSize = 20.sp,
                fontFamily = Manrope,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f, fill = !shrink)
            )
        }
    }
}