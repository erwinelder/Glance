package com.ataglance.walletglance.domain.categories

import com.ataglance.walletglance.domain.app.AppTheme
import com.ataglance.walletglance.domain.categories.color.CategoryColorWithName
import com.ataglance.walletglance.domain.categories.color.CategoryColors
import com.ataglance.walletglance.domain.categories.icons.CategoryIcon
import com.ataglance.walletglance.domain.color.LighterDarkerColors
import com.ataglance.walletglance.domain.utils.toCategoryColorWithName

data class Category(
    val id: Int = 0,
    val type: CategoryType = CategoryType.Expense,
    val orderNum: Int = 0,
    val parentCategoryId: Int? = null,
    val name: String = "",
    val icon: CategoryIcon = CategoryIcon.Other,
    val colorWithName: CategoryColorWithName = CategoryColors.GrayDefault.toCategoryColorWithName()
) {

    fun isExpense() = type == CategoryType.Expense
    private fun isIncome() = type == CategoryType.Income

    fun isParentCategory() = parentCategoryId == null

    fun getColorByTheme(theme: AppTheme?): LighterDarkerColors {
        return colorWithName.getColorByTheme(theme)
    }

    fun canBeDeleted(): Boolean {
        return (isExpense() && id != 12 && id != 66) || (isIncome() && id != 77)
    }

    fun toCheckedCategory(checkedCategoryList: List<Category>): CheckedCategory {
        return CheckedCategory(
            category = this,
            checked = checkedCategoryList.find { it.id == id } != null
        )
    }

}
