package com.ataglance.walletglance.category.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreenPreview
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryColor
import com.ataglance.walletglance.category.domain.model.CategoryIcon
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@Composable
fun CategoryIconComponent(
    category: Category,
    modifier: Modifier = Modifier,
    cornerSize: Int = 30
) {
    Icon(
        painter = painterResource(category.icon.res),
        contentDescription = "category ${category.name} icon",
        tint = GlanciColors.background,
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                spotColor = category.getIconSolidColorByTheme(CurrAppTheme),
                shape = RoundedCornerShape(cornerSize)
            )
            .clip(RoundedCornerShape(cornerSize))
            .background(category.getIconSolidColorByTheme(CurrAppTheme))
            .size(28.dp)
            .padding(5.dp)
    )
}

@Composable
fun CategoryIconComponent(
    categoryIcon: CategoryIcon,
    categoryColor: CategoryColor,
    modifier: Modifier = Modifier,
    cornerSize: Int = 30
) {
    Icon(
        painter = painterResource(categoryIcon.res),
        contentDescription = "category icon",
        tint = GlanciColors.background,
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                spotColor = categoryColor.color.getCategoryIconSolidColorByTheme(CurrAppTheme),
                shape = RoundedCornerShape(cornerSize)
            )
            .clip(RoundedCornerShape(cornerSize))
            .background(categoryColor.color.getCategoryIconSolidColorByTheme(CurrAppTheme))
            .size(28.dp)
            .padding(5.dp)
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun CategoryIconComponentPreview() {
    EditBudgetsScreenPreview(appTheme = AppTheme.LightDefault)
}
