package com.ataglance.walletglance.data.categories

import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.color.CategoryColorWithName
import com.ataglance.walletglance.data.categories.color.CategoryColors
import com.ataglance.walletglance.data.categories.icons.CategoryIcon
import com.ataglance.walletglance.data.color.LighterDarkerColors
import com.ataglance.walletglance.domain.entities.CategoryEntity
import com.ataglance.walletglance.ui.utils.asChar
import com.ataglance.walletglance.ui.utils.toCategoryColorWithName

data class Category(
    val id: Int = 0,
    val type: CategoryType = CategoryType.Expense,
    val rank: CategoryRank = CategoryRank.Parent,
    val orderNum: Int = 0,
    val parentCategoryId: Int? = null,
    val name: String = "",
    val icon: CategoryIcon = CategoryIcon.Other,
    val colorWithName: CategoryColorWithName = CategoryColors.GrayDefault.toCategoryColorWithName()
) {

    fun isExpense() = type == CategoryType.Expense
    private fun isIncome() = type == CategoryType.Income

    fun isParentCategory() = rank == CategoryRank.Parent

    fun getColorByTheme(theme: AppTheme?): LighterDarkerColors {
        return colorWithName.getColorByTheme(theme)
    }

    fun canBeDeleted(): Boolean {
        return (isExpense() && id != 12 && id != 66) || (isIncome() && id != 77)
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

    fun toCheckedCategory(checkedCategoryList: List<Category>): CheckedCategory {
        return CheckedCategory(
            category = this,
            checked = checkedCategoryList.find { it.id == id } != null
        )
    }

}
