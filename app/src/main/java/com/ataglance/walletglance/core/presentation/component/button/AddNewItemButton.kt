package com.ataglance.walletglance.core.presentation.component.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect

@Composable
fun AddNewItemButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .width(250.dp)
            .bounceClickEffect(.97f)
            .clip(RoundedCornerShape(22.dp))
            .border(2.dp, GlanceColors.outline, RoundedCornerShape(22.dp))
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.add_icon),
            contentDescription = "add new item",
            tint = GlanceColors.outline,
            modifier = Modifier.size(26.dp)
        )
    }
}