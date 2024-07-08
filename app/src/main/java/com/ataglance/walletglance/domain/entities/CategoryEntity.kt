package com.ataglance.walletglance.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryRank
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.color.CategoryColorName
import com.ataglance.walletglance.data.categories.color.CategoryColorWithName
import com.ataglance.walletglance.data.categories.icons.CategoryIcon

@Entity(tableName = "Category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: Char,
    val rank: Char,
    val orderNum: Int,
    val parentCategoryId: Int?,
    val name: String,
    val iconName: String = CategoryIcon.Other.name,
    val colorName: String = CategoryColorName.GrayDefault.name
) {

    fun isExpense() = type == '-'

    fun isParentCategory() = rank == 'c'

    fun toCategory(icon: CategoryIcon, color: CategoryColorWithName): Category {
        return Category(
            id = id,
            type = if (isExpense()) CategoryType.Expense else CategoryType.Income,
            rank = if (isParentCategory()) CategoryRank.Parent else CategoryRank.Sub,
            orderNum = orderNum,
            parentCategoryId = parentCategoryId,
            name = name,
            icon = icon,
            colorWithName = color
        )
    }

}
