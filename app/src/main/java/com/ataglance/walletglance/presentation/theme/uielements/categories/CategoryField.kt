package com.ataglance.walletglance.presentation.theme.uielements.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.presentation.theme.GlanceTheme
import com.ataglance.walletglance.presentation.theme.animation.bounceClickEffect

@Composable
fun CategoryField(
    category: Category?,
    fontSize: TextUnit,
    cornerSize: Dp,
    appTheme: AppTheme?,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .bounceClickEffect(.97f, onClick = onClick)
            .clip(RoundedCornerShape(cornerSize))
            .background(GlanceTheme.surface)
            .padding(top = 2.dp, bottom = 2.dp, start = 2.dp, end = 6.dp)
    ) {
        category?.let {
            Icon(
                painter = painterResource(category.icon.res),
                contentDescription = category.name + " icon",
                tint = GlanceTheme.surface,
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(
                            (cornerSize - 2.dp).takeIf { it >= 0.dp } ?: 0.dp
                        ),
                        spotColor = category.colorWithName.color.getByTheme(appTheme).darker
                    )
                    .clip(
                        RoundedCornerShape(
                            (cornerSize - 2.dp).takeIf { it >= 0.dp } ?: 0.dp
                        )
                    )
                    .size(38.dp)
                    .background(category.colorWithName.color.getByTheme(appTheme).darker)
                    .padding(6.dp)
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