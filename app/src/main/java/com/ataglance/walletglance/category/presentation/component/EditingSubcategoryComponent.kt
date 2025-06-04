package com.ataglance.walletglance.category.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun EditingSubcategoryComponent(
    category: Category,
    onEditButton: (Int) -> Unit,
    onUpButtonClick: () -> Unit,
    upButtonEnabled: Boolean,
    onDownButtonClick: () -> Unit,
    downButtonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(11.dp),
        modifier = modifier
            .bounceClickEffect(.98f)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.record_corner_size)))
            .background(
                brush = Brush.linearGradient(
                    colors = GlanciColors.glassGradientOnGlass,
                    start = Offset(50f, 190f),
                    end = Offset(100f, 0f)
                )
            )
            .border(
                width = 1.dp,
                color = GlanciColors.glassGradientOnGlassBorder,
                shape = RoundedCornerShape(dimensionResource(R.dimen.record_corner_size))
            )
            .padding(start = 15.dp, end = 15.dp, top = 12.dp, bottom = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIconComponent(category)
            Text(
                text = category.name,
                color = GlanciColors.onSurface,
                fontSize = 19.sp,
                fontFamily = Manrope,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
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