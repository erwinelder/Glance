package com.ataglance.walletglance.ui.theme.uielements.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.ui.theme.GlanceTheme

@Composable
fun TwoStateCheckbox(
    checked: Boolean,
    enabled: Boolean = true,
    onClick: (Boolean) -> Unit
) {
    val iconRes = when (checked) {
        true -> R.drawable.checked_icon
        false -> null
    }
    val background by animateColorAsState(
        targetValue = GlanceTheme.primary.copy(
            if (checked && enabled) 1f else if (checked && !enabled) .5f else 0f
        ),
        label = "two state checkbox background color",
        animationSpec = tween(150, 0)
    )
    val borderColor by animateColorAsState(
        targetValue = GlanceTheme.outline.copy(
            if (checked) 0f else if (enabled) 1f else .3f
        ),
        label = "two state checkbox border color",
        animationSpec = tween(150, 0)
    )

    FilledIconButton(
        onClick = {
            if (enabled) onClick(!checked)
        },
        shape = RoundedCornerShape(15),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = background
        ),
        modifier = Modifier
            .border(1.5.dp, borderColor, RoundedCornerShape(15))
            .size(28.dp)
    ) {
        AnimatedContent(
            targetState = iconRes,
            label = "three state checkbox icon",
            transitionSpec = {
                (fadeIn(tween(150, 70)) +
                        scaleIn(tween(150, 70), .92f))
                    .togetherWith(fadeOut(tween(70)))
            },
            contentAlignment = Alignment.Center
        ) { targetIconRes ->
            targetIconRes?.let {
                Icon(
                    painter = painterResource(targetIconRes),
                    contentDescription = "",
                    tint = GlanceTheme.background,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}