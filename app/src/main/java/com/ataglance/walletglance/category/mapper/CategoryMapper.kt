package com.ataglance.walletglance.category.mapper

import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryColor
import com.ataglance.walletglance.category.domain.model.CategoryIcon
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategories
import com.ataglance.walletglance.category.domain.utils.asChar
import com.ataglance.walletglance.category.domain.utils.findById
import com.ataglance.walletglance.category.presentation.model.CheckedCategory
import com.ataglance.walletglance.category.presentation.model.EditingCategoriesWithSubcategories
import com.ataglance.walletglance.category.presentation.model.EditingCategoryWithSubcategories
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories


fun CategoryEntity.toDomainModel(): Category? {
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

fun List<CategoryEntity>.toDomainModels(): List<Category> {
    return this.mapNotNull(CategoryEntity::toDomainModel)
}


fun Category.toDataModel(): CategoryEntity {
    return CategoryEntity(
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


fun CategoryWithSubcategories.toEditingCategoryWithSubcategories(
    checkedCategoryList: List<Category>
): EditingCategoryWithSubcategories {
    val subcategoryList = subcategoryList.toCheckedCategories(checkedCategoryList)
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

    return EditingCategoryWithSubcategories(
        category = category,
        checked = checked,
        subcategoryList = subcategoryList
    )
}

fun List<CategoryWithSubcategories>.toEditingCategoryWithSubcategoriesList(
    checkedCategoryList: List<Category>
): List<EditingCategoryWithSubcategories> {
    return this.map { it.toEditingCategoryWithSubcategories(checkedCategoryList) }
}

fun CategoriesWithSubcategories.toEditingCategoriesWithSubcategories(
    collection: CategoryCollectionWithCategories?
): EditingCategoriesWithSubcategories {
    val (expenseCategories, incomeCategories) = collection?.categoryList.orEmpty()
        .partition { it.isExpense() }

    return EditingCategoriesWithSubcategories(
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