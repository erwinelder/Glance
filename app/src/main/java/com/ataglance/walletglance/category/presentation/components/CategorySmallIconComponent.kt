package com.ataglance.walletglance.category.presentation.components

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
import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.CurrAppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.record.presentation.screen.RecordsScreenPreview

@Composable
fun CategoryIconComponent(category: Category) {
    Icon(
        painter = painterResource(category.icon.res),
        contentDescription = "category ${category.name} icon",
        tint = GlanceTheme.surface,
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                spotColor = category.getIconSolidColorByTheme(CurrAppTheme),
                shape = RoundedCornerShape(30)
            )
            .clip(RoundedCornerShape(30))
            .background(category.getIconSolidColorByTheme(CurrAppTheme))
            .size(32.dp)
            .padding(5.dp)
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun CategoryIconComponentPreview() {
    RecordsScreenPreview(appTheme = AppTheme.LightDefault)
}
