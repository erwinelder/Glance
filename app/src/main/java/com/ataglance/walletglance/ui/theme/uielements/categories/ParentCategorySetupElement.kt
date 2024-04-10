package com.ataglance.walletglance.ui.theme.uielements.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Category
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.theme.LighterDarkerColors

@Composable
fun ParentCategorySetupElement(
    category: Category,
    iconRes: Int?,
    color: LighterDarkerColors?,
    onNavigateToEditSubcategoryListScreen: (Int) -> Unit,
    onEditButton: (Int) -> Unit,
    onUpButtonClick: () -> Unit,
    upButtonEnabled: Boolean,
    onDownButtonClick: () -> Unit,
    downButtonEnabled: Boolean,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(11.dp),
        modifier = Modifier
            .bounceClickEffect(.98f)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.record_corner_size)))
            .background(
                brush = Brush.linearGradient(
                    colors = GlanceTheme.onGlassSurfaceGradient,
                    start = Offset(50f, 190f),
                    end = Offset(100f, 0f)
                )
            )
            .border(
                1.dp,
                GlanceTheme.outlineVariant,
                RoundedCornerShape(dimensionResource(R.dimen.record_corner_size))
            )
            .padding(start = 15.dp, end = 15.dp, top = 12.dp, bottom = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onNavigateToEditSubcategoryListScreen(category.orderNum) }
        ) {
            if (iconRes != null && color != null) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = "category ${category.name} icon",
                    tint = GlanceTheme.surface,
                    modifier = Modifier
                        .shadow(
                            elevation = 8.dp,
                            spotColor = color.darker,
                            shape = RoundedCornerShape(30)
                        )
                        .clip(RoundedCornerShape(30))
                        .background(color.darker)
                        .size(32.dp)
                        .padding(5.dp)
                )
                Spacer(modifier = Modifier.requiredWidth(8.dp))
            }
            Text(
                text = category.name,
                color = GlanceTheme.onSurface,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 1.dp)
            )
            Spacer(modifier = Modifier.requiredWidth(8.dp))
            Icon(
                painter = painterResource(R.drawable.short_arrow_right_icon),
                contentDescription = "right arrow",
                tint = GlanceTheme.onSurface,
                modifier = Modifier
                    .requiredSize(12.dp, 20.dp)
            )
        }
        CategoryControlPanel(
            onUpButtonClick = onUpButtonClick,
            upButtonEnabled = upButtonEnabled,
            onDownButtonClick = onDownButtonClick,
            downButtonEnabled = downButtonEnabled,
            onEditButton = { onEditButton(category.orderNum) }
        )
    }
}