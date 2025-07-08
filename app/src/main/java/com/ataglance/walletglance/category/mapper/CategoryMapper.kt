package com.ataglance.walletglance.category.mapper

import com.ataglance.walletglance.category.data.model.CategoryDataModel
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryColor
import com.ataglance.walletglance.category.domain.model.CategoryIcon
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.utils.findById
import com.ataglance.walletglance.category.presentation.model.CheckedCategory
import com.ataglance.walletglance.category.presentation.model.CheckedGroupedCategories
import com.ataglance.walletglance.category.presentation.model.CheckedGroupedCategoriesByType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories


fun CategoryType.toCategoryCollectionType(): CategoryCollectionType {
    return when (this) {
        CategoryType.Expense -> CategoryCollectionType.Expense
        CategoryType.Income -> CategoryCollectionType.Income
    }
}


fun CategoryDataModel.toDomainModel(): Category? {
    val categoryType = this.getCategoryType() ?: return null

    return Category(
        id = id,
        type = categoryType,
        orderNum = orderNum,
        parentCategoryId = parentCategoryId,
        name = name,
        icon = CategoryIcon.getByName(iconName),
        color = CategoryColor.getByName(colorName)
    )
}

fun Category.toDataModel(): CategoryDataModel {
    return CategoryDataModel(
        id = id,
        type = type.asChar(),
        orderNum = orderNum,
        parentCategoryId = parentCategoryId,
        name = name,
        iconName = icon.name,
        colorName = color.getNameValue()
    )
}


fun Category.toCheckedCategory(checkedCategoryList: List<Category>): CheckedCategory {
    return CheckedCategory(
        category = this,
        checked = checkedCategoryList.findById(id) != null
    )
}

fun List<Category>.toCheckedCategories(checkedCategoryList: List<Category>): List<CheckedCategory> {
    return this.map { it.toCheckedCategory(checkedCategoryList) }
}


fun GroupedCategories.toEditingCategoryWithSubcategories(
    checkedCategoryList: List<Category>
): CheckedGroupedCategories {
    val subcategoryList = subcategories.toCheckedCategories(checkedCategoryList)
    val (checkedSubcategories, uncheckedSubcategories) = subcategoryList.partition { it.checked }

    val checked = if (subcategoryList.isNotEmpty()) {
        if (checkedSubcategories.isEmpty()) {
            false
        } else if (uncheckedSubcategories.isEmpty()) {
            true
        } else {
            null
        }
    } else {
        checkedCategoryList.findById(category.id) != null
    }

    return CheckedGroupedCategories(
        category = category,
        checked = checked,
        subcategoryList = subcategoryList
    )
}

fun List<GroupedCategories>.toEditingCategoryWithSubcategoriesList(
    checkedCategoryList: List<Category>
): List<CheckedGroupedCategories> {
    return this.map { it.toEditingCategoryWithSubcategories(checkedCategoryList) }
}

fun GroupedCategoriesByType.toCheckedCategoriesWithSubcategories(
    collection: CategoryCollectionWithCategories?
): CheckedGroupedCategoriesByType {
    val (expenseCategories, incomeCategories) = collection?.categories.orEmpty()
        .partition { it.isExpense() }

    return CheckedGroupedCategoriesByType(
        expense = if (collection?.type != CategoryCollectionType.Income) {
            expense.toEditingCategoryWithSubcategoriesList(expenseCategories)
        } else {
            emptyList()
        },
        income = if (collection?.type != CategoryCollectionType.Expense) {
            income.toEditingCategoryWithSubcategoriesList(incomeCategories)
        } else {
            emptyList()
        }
    )
}