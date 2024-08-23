package com.ataglance.walletglance.presentation.ui.uielements.categories

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
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.presentation.ui.GlanceTheme

@Composable
fun CategoryIconComponent(
    category: Category,
    appTheme: AppTheme?
) {
    Icon(
        painter = painterResource(category.icon.res),
        contentDescription = "category ${category.name} icon",
        tint = GlanceTheme.surface,
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                spotColor = category.getColorByTheme(appTheme).darker,
                shape = RoundedCornerShape(30)
            )
            .clip(RoundedCornerShape(30))
            .background(category.getColorByTheme(appTheme).darker)
            .size(32.dp)
            .padding(5.dp)
    )
}