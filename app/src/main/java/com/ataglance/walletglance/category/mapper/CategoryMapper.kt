package com.ataglance.walletglance.category.mapper

import com.ataglance.walletglance.category.data.model.CategoryEntity
import com.ataglance.walletglance.category.domain.color.CategoryColorWithName
import com.ataglance.walletglance.category.domain.icons.CategoryIcon
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategories
import com.ataglance.walletglance.category.domain.utils.asChar
import com.ataglance.walletglance.category.domain.utils.toCategoryList



fun Map<String, Any?>.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        id = this["id"] as Int,
        type = this["type"] as Char,
        orderNum = this["orderNum"] as Int,
        parentCategoryId = this["parentCategoryId"] as Int?,
        name = this["name"] as String,
        iconName = this["iconName"] as String,
        colorName = this["colorName"] as String
    )
}

fun CategoryEntity.toMap(timestamp: Long): HashMap<String, Any?> {
    return hashMapOf(
        "LMT" to timestamp,
        "id" to id,
        "type" to type,
        "orderNum" to orderNum,
        "parentCategoryId" to parentCategoryId,
        "name" to name,
        "iconName" to iconName,
        "colorName" to colorName
    )
}



fun CategoryEntity.toCategory(icon: CategoryIcon, color: CategoryColorWithName): Category? {
    val categoryType = this.getCategoryType() ?: return null

    return Category(
        id = id,
        type = categoryType,
        orderNum = orderNum,
        parentCategoryId = parentCategoryId,
        name = name,
        icon = icon,
        colorWithName = color
    )
}

fun List<CategoryEntity>.toCategoryList(
    iconProvider: (String) -> CategoryIcon,
    colorProvider: (String) -> CategoryColorWithName
): List<Category> {
    return this.mapNotNull {
        it.toCategory(
            icon = iconProvider(it.iconName),
            color = colorProvider(it.colorName)
        )
    }
}



fun List<CategoryEntity>.toCategoriesWithSubcategories(): CategoriesWithSubcategories {
    val categoriesAndSubcategories = this.toCategoryList().partition { it.isParentCategory() }

    val subcategoryMap = mutableMapOf<Int, MutableList<Category>>()
    categoriesAndSubcategories.second.forEach { subcategory ->
        subcategory.parentCategoryId?.let { parentCategoryId ->
            if (subcategoryMap.containsKey(subcategory.parentCategoryId)) {
                subcategoryMap[parentCategoryId]!!.add(subcategory)
            } else {
                subcategoryMap[parentCategoryId] = mutableListOf(subcategory)
            }
        }
    }

    return categoriesAndSubcategories.first
        .map { category ->
            CategoryWithSubcategories(
                category = category,
                subcategoryList = subcategoryMap[category.id]?.sortedBy { it.orderNum }
                    ?: emptyList(),
            )
        }
        .partition { it.category.isExpense() }
        .let { expenseAndIncomeCategoriesWithSubcategories ->
            CategoriesWithSubcategories(
                expense = expenseAndIncomeCategoriesWithSubcategories.first
                    .sortedBy { it.category.orderNum },
                income = expenseAndIncomeCategoriesWithSubcategories.second
                    .sortedBy { it.category.orderNum }
            )
        }

}



fun Category.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        type = type.asChar(),
        orderNum = orderNum,
        parentCategoryId = parentCategoryId,
        name = name,
        iconName = icon.name,
        colorName = colorWithName.getNameValue()
    )
}

fun List<Category>.toCategoryEntityList(): List<CategoryEntity> {
    return this.map { it.toCategoryEntity() }
}
