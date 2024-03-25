package com.ataglance.walletglance.ui.theme.uielements.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Category
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect

@Composable
fun SubcategorySetupElement(
    category: Category,
    iconRes: Int?,
    onEditButton: (Int) -> Unit,
    onUpButtonClick: () -> Unit,
    upButtonEnabled: Boolean,
    onDownButtonClick: () -> Unit,
    downButtonEnabled: Boolean
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
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            iconRes?.let {
                Icon(
                    painter = painterResource(it),
                    contentDescription = category.name + " category icon",
                    tint = GlanceTheme.onSurface,
                    modifier = Modifier.size(24.dp, 24.dp)
                )
            }
            Text(
                text = category.name,
                color = GlanceTheme.onSurface,
                fontSize = 19.sp,
                modifier = Modifier.padding(bottom = 1.dp)
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