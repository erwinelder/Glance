package com.ataglance.walletglance.category.data.model

import com.ataglance.walletglance.category.domain.model.CategoryType

data class CategoryDataModel(
    val id: Int,
    val type: Char,
    val orderNum: Int,
    val parentCategoryId: Int?,
    val name: String,
    val iconName: String,
    val colorName: String
) {

    fun getCategoryType(): CategoryType? {
        return when (type) {
            '-' -> CategoryType.Expense
            '+' -> CategoryType.Income
            else -> null
        }
    }


}
