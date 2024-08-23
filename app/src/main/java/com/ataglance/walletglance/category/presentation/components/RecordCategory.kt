package com.ataglance.walletglance.category.presentation.components

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
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun RecordCategory(
    category: Category?,
    appTheme: AppTheme?,
    iconSize: Dp = 27.dp,
    fontSize: TextUnit = 18.sp,
    onClick: ((Category) -> Unit)? = null
) {
    category?.let {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = onClick?.let {
                Modifier.bounceClickEffect { onClick(category) }
            } ?: Modifier
        ) {
            Icon(
                painter = painterResource(category.icon.res),
                contentDescription = category.name + " icon",
                tint = GlanceTheme.surface,
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(30),
                        spotColor = category.colorWithName.color.getByTheme(appTheme).darker
                    )
                    .clip(RoundedCornerShape(30))
                    .size(iconSize)
                    .background(category.colorWithName.color.getByTheme(appTheme).darker)
                    .padding(5.dp)
            )
            Text(
                text = category.name,
                color = GlanceTheme.onSurface,
                fontSize = fontSize,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, false)
            )
        }
    }
}