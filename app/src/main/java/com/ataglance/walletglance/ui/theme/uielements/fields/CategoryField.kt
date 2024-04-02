package com.ataglance.walletglance.ui.theme.uielements.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.data.Category
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect

@Composable
fun CategoryField(
    category: Category?,
    categoryIconRes: Int?,
    fontSize: TextUnit,
    cornerSize: Dp,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .bounceClickEffect(.97f, onClick = onClick)
            .clip(RoundedCornerShape(cornerSize))
            .background(GlanceTheme.surface)
            .padding(12.dp, 6.dp)
    ) {
        categoryIconRes?.let {
            Icon(
                painter = painterResource(categoryIconRes),
                contentDescription = category?.name + " icon",
                tint = GlanceTheme.onSurface,
                modifier = Modifier
                    .size(28.dp)
            )
        }
        Text(
            text = category?.name ?: "???",
            color = category?.let {
                GlanceTheme.onSurface
            } ?: GlanceTheme.surface,
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}