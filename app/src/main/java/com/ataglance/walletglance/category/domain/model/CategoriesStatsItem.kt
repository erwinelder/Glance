package com.ataglance.walletglance.category.domain.model

data class CategoriesStatsItem(
    val category: Category,
    var totalAmount: Double = 0.0
) {

    fun getPercentage(allCategoriesTotalAmount: Double): Float {
        return if (allCategoriesTotalAmount != 0.0) {
            ((100 / allCategoriesTotalAmount) * totalAmount).toFloat()
        } else {
            0.0f
        }
    }

}