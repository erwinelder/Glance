package com.ataglance.walletglance.data.categories

import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.color.CategoryColorWithName
import com.ataglance.walletglance.data.categories.icons.CategoryIcon
import com.ataglance.walletglance.data.color.LighterDarkerColors
import com.ataglance.walletglance.domain.entities.CategoryEntity
import com.ataglance.walletglance.ui.utils.asChar

data class Category(
    val id: Int,
    val type: CategoryType,
    val rank: CategoryRank,
    val orderNum: Int,
    val parentCategoryId: Int?,
    val name: String,
    val icon: CategoryIcon,
    val colorWithName: CategoryColorWithName
) {

    fun isExpense() = type == CategoryType.Expense
    fun isIncome() = type == CategoryType.Income

    fun getColorByTheme(theme: AppTheme?): LighterDarkerColors {
        return colorWithName.getColorByTheme(theme)
    }

    fun toCategoryEntity(): CategoryEntity {
        return CategoryEntity(
            id = id,
            type = type.asChar(),
            rank = rank.asChar(),
            orderNum = orderNum,
            parentCategoryId = parentCategoryId,
            name = name,
            iconName = icon.name,
            colorName = colorWithName.getNameValue()
        )
    }

}
