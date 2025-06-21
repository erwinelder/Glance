package com.ataglance.walletglance.category.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun CategoryField(
    category: Category?,
    fontSize: TextUnit = 19.sp,
    cornerSize: Dp = 15.dp,
    onClick: () -> Unit
) {
    GlassSurfaceOnGlassSurface(
        onClick = onClick,
        contentPadding = PaddingValues(top = 2.dp, bottom = 2.dp, start = 2.dp, end = 12.dp),
        cornerSize = cornerSize
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            category?.let {
                Icon(
                    painter = painterResource(category.icon.res),
                    contentDescription = category.name + " icon",
                    tint = GlanciColors.surface,
                    modifier = Modifier
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape((cornerSize - 3.dp).coerceAtLeast(0.dp)),
                            spotColor = category.getIconSolidColorByTheme(CurrAppTheme)
                        )
                        .clip(RoundedCornerShape((cornerSize - 3.dp).coerceAtLeast(0.dp)))
                        .size(36.dp)
                        .background(category.getIconSolidColorByTheme(CurrAppTheme))
                        .padding(6.dp)
                )
            }
            Text(
                text = category?.name ?: "???",
                color = if (category != null) GlanciColors.onSurface else GlanciColors.surface,
                fontSize = fontSize,
                fontFamily = Manrope,
                fontWeight = FontWeight.W400,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}