package com.ataglance.walletglance.category.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.GlanceTheme

@Composable
fun CategoryControlPanel(
    onUpButtonClick: () -> Unit,
    upButtonEnabled: Boolean,
    onDownButtonClick: () -> Unit,
    downButtonEnabled: Boolean,
    onEditButton: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilledIconButton(
            onClick = onUpButtonClick,
            enabled = upButtonEnabled,
            shape = RectangleShape,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = GlanceTheme.onSurface,
                disabledContentColor = GlanceTheme.outline.copy(.5f)
            ),
            modifier = Modifier.size(25.dp, 15.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.short_arrow_up_icon),
                contentDescription = "arrow up",
            )
        }
        FilledIconButton(
            onClick = onDownButtonClick,
            enabled = downButtonEnabled,
            shape = RectangleShape,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = GlanceTheme.onSurface,
                disabledContentColor = GlanceTheme.outline.copy(.5f)
            ),
            modifier = Modifier.size(25.dp, 15.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.short_arrow_down_icon),
                contentDescription = "arrow down",
            )
        }
        FilledIconButton(
            onClick = onEditButton,
            shape = RectangleShape,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = GlanceTheme.onSurface
            ),
            modifier = Modifier
                .size(21.dp, 18.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.edit_icon),
                contentDescription = "edit"
            )
        }
    }
}