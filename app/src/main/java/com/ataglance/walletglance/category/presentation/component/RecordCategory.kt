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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun RecordCategory(
    category: Category?,
    iconSize: Dp = 27.dp,
    fontSize: TextUnit = 18.sp,
    onClick: ((Category) -> Unit)? = null
) {
    category?.let {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .run {
                    onClick?.let { this.bounceClickEffect { onClick(category) } } ?: this
                }
        ) {
            Icon(
                painter = painterResource(category.icon.res),
                contentDescription = category.name + " icon",
                tint = GlanciColors.surface,
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(30),
                        spotColor = category.getIconSolidColorByTheme(CurrAppTheme)
                    )
                    .clip(RoundedCornerShape(30))
                    .size(iconSize)
                    .background(category.getIconSolidColorByTheme(CurrAppTheme))
                    .padding(5.dp)
            )
            Text(
                text = category.name,
                color = GlanciColors.onSurface,
                fontSize = fontSize,
                fontFamily = Manrope,
                fontWeight = FontWeight.W400,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, false)
            )
        }
    }
}