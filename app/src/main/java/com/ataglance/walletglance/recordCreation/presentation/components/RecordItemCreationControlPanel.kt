package com.ataglance.walletglance.recordCreation.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun RecordItemCreationControlPanel(
    thisIndex: Int,
    lastIndex: Int,
    spaceSize: Dp,
    onSwapButtonsClick: (Int, Int) -> Unit,
    onDeleteButtonClick: (Int) -> Unit,
    collapseExpandButton: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spaceSize),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .bounceClickEffect(.98f)
                .clip(RoundedCornerShape(17.dp))
                .background(GlanceTheme.surface)
                .padding(horizontal = 4.dp)
        ) {
            SubrecordControlPanelButton(
                iconRes = R.drawable.trash_icon,
                iconContentDescription = "delete",
                enabled = lastIndex != 0,
                onClick = { onDeleteButtonClick(thisIndex) }
            )
            SubrecordControlPanelButton(
                iconRes = R.drawable.short_arrow_up_icon,
                iconContentDescription = "move up",
                enabled = thisIndex > 0,
                onClick = { onSwapButtonsClick(thisIndex, thisIndex - 1) }
            )
            SubrecordControlPanelButton(
                iconRes = R.drawable.short_arrow_down_icon,
                iconContentDescription = "move down",
                enabled = thisIndex < lastIndex,
                onClick = { onSwapButtonsClick(thisIndex, thisIndex + 1) }
            )
        }
        collapseExpandButton()
    }
}

@Composable
private fun SubrecordControlPanelButton(
    iconRes: Int,
    iconContentDescription: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val color by animateColorAsState(
        targetValue = if (enabled) GlanceTheme.onSurface else GlanceTheme.outline,
        label = "make record unit control panel button color"
    )

    IconButton(
        onClick = onClick,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier.bounceClickEffect(.98f)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = iconContentDescription,
            tint = color,
            modifier = Modifier.size(28.dp)
        )
    }
}