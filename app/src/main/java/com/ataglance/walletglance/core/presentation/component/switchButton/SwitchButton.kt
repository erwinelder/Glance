package com.ataglance.walletglance.core.presentation.component.switchButton

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.modifier.innerShadow

@Composable
fun SwitchButton(
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
    width: Dp = 54.dp,
    height: Dp = 30.dp,
) {
    val offset by animateDpAsState(
        targetValue = if (checked) width - height else 0.dp
    )
    val background by animateColorAsState(
        targetValue = if (checked) GlanciColors.primary else GlanciColors.surface
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onChecked(!checked) }
            .size(width, height)
            .clip(RoundedCornerShape(height))
            .background(background)
            .border(1.dp, GlanciColors.outline.copy(.4f), CircleShape)
            .innerShadow(
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(x = offset.roundToPx(), y = 0) }
                .size(height - 8.dp)
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(50),
                    ambientColor = Color.Black
                )
                .clip(RoundedCornerShape(50))
                .background(GlanciColors.onPrimary)
        )
    }
}