package com.ataglance.walletglance.category.domain.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.category.domain.model.color.CategoryColorWithName
import com.ataglance.walletglance.category.domain.model.color.CategoryColors
import com.ataglance.walletglance.category.domain.model.icons.CategoryIcon
import com.ataglance.walletglance.category.domain.utils.toCategoryColorWithName
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.color.LighterDarkerColors

@Stable
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

    fun getColorByTheme(theme: AppTheme): LighterDarkerColors {
        return colorWithName.getColorByTheme(theme)
    }

    fun getIconSolidColorByTheme(appTheme: AppTheme): Color {
        return colorWithName.color.getCategoryIconSolidColorByTheme(appTheme)
    }

    fun getLineChartColorsByTheme(appTheme: AppTheme): List<Color> {
        return colorWithName.color.getCategoryLineChartColorsByTheme(appTheme)
    }

    fun canBeDeleted(): Boolean {
        return (isExpense() && id != 12 && id != 66) || (isIncome() && id != 77)
    }

    fun allowSaving(): Boolean {
        return name.isNotBlank()
    }

    fun toCheckedCategory(checkedCategoryList: List<Category>): CheckedCategory {
        return CheckedCategory(
            category = this,
            checked = checkedCategoryList.find { it.id == id } != null
        )
    }

}
