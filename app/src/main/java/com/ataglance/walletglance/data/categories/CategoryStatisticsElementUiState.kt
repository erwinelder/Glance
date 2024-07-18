package com.ataglance.walletglance.data.categories

import androidx.annotation.DrawableRes
import com.ataglance.walletglance.data.color.LighterDarkerColorsByTheme
import java.util.Locale

data class CategoryStatisticsElementUiState(
    val categoryId: Int,
    val categoryName: String,
    @DrawableRes val categoryIconRes: Int,
    val categoryColor: LighterDarkerColorsByTheme,
    val totalAmount: String,
    val currency: String,
    val percentage: Float,
    val subcategoriesStatisticsUiState: List<CategoryStatisticsElementUiState>? = null
) {

    fun getTotalAmountWithCurrency(): String {
        return "$totalAmount $currency"
    }

    fun getFormattedPercentage(): String {
        return "%.2f".format(Locale.US, percentage) + "%"
    }

}