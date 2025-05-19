package com.ataglance.walletglance.category.presentation.component

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
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect

@Composable
fun CategoryField(
    category: Category?,
    fontSize: TextUnit = 20.sp,
    cornerSize: Dp = 15.dp,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .bounceClickEffect(.97f, onClick = onClick)
            .clip(RoundedCornerShape(cornerSize))
            .background(GlanceColors.surface)
            .padding(top = 2.dp, bottom = 2.dp, start = 2.dp, end = 8.dp)
    ) {
        category?.let {
            Icon(
                painter = painterResource(category.icon.res),
                contentDescription = category.name + " icon",
                tint = GlanceColors.surface,
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape((cornerSize - 2.dp).coerceAtLeast(0.dp)),
                        spotColor = category.getIconSolidColorByTheme(CurrAppTheme)
                    )
                    .clip(RoundedCornerShape((cornerSize - 2.dp).coerceAtLeast(0.dp)))
                    .size(38.dp)
                    .background(category.getIconSolidColorByTheme(CurrAppTheme))
                    .padding(6.dp)
            )
        }
        Text(
            text = category?.name ?: "???",
            color = if (category != null) GlanceColors.onSurface else GlanceColors.surface,
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}