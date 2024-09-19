package com.ataglance.walletglance.category.domain

import androidx.annotation.DrawableRes
import com.ataglance.walletglance.core.domain.color.LighterDarkerColorsByTheme

data class CategoryStatisticsElementUiState(
    val categoryId: Int,
    val categoryName: String,
    @DrawableRes val categoryIconRes: Int,
    val categoryColor: LighterDarkerColorsByTheme,
    val totalAmount: String,
    val currency: String,
    val percentageFloat: Float,
    val percentageFormatted: String,
    val subcategoriesStatisticsUiState: List<CategoryStatisticsElementUiState>? = null
)