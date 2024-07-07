package com.ataglance.walletglance.data.categories

import com.ataglance.walletglance.data.categories.color.CategoryColorWithName
import com.ataglance.walletglance.ui.utils.deleteItemAndMoveOrderNum
import com.ataglance.walletglance.ui.utils.findById
import com.ataglance.walletglance.ui.utils.toCheckedCategoryList

data class CategoryWithSubcategories(
    val category: Category,
    val subcategoryList: List<Category> = emptyList()
) {

    fun appendNewSubcategory(subcategory: Category): CategoryWithSubcategories {
        val list = subcategoryList.toMutableList()
        list.add(
            subcategory.copy(
                orderNum = (list.maxOfOrNull { it.orderNum } ?: 0) + 1
            )
        )
        return this.copy(subcategoryList = list)
    }

    fun replaceSubcategory(subcategory: Category): CategoryWithSubcategories {
        return this.copy(
            subcategoryList = subcategoryList.map {
                it.takeIf { it.id != subcategory.id } ?: subcategory
            }
        )
    }

    fun deleteSubcategoryById(id: Int): CategoryWithSubcategories {
        return this.copy(
            subcategoryList = subcategoryList.deleteItemAndMoveOrderNum(
                { it.id == id }, { it.copy(orderNum = it.orderNum - 1) }
            )
        )
    }

    fun changeSubcategoriesColorTo(colorWithName: CategoryColorWithName): List<Category> {
        return if (subcategoryList.firstOrNull()?.colorWithName?.name == colorWithName.name) {
            subcategoryList
        } else {
            subcategoryList.map { it.copy(colorWithName = colorWithName) }
        }
    }

    fun getWithSubcategoryWithId(id: Int): CategoryWithSubcategory {
        return CategoryWithSubcategory(category, subcategoryList.findById(id))
    }

    fun getWithLastSubcategory(): CategoryWithSubcategory {
        return CategoryWithSubcategory(category, subcategoryList.lastOrNull())
    }

    fun asSingleList(): List<Category> {
        return listOf(category) + subcategoryList
    }

    fun fixSubcategoriesOrderNumbers(): CategoryWithSubcategories {
        return this.copy(
            subcategoryList = subcategoryList.mapIndexed { index, category ->
                category.copy(orderNum = index + 1)
            }
        )
    }

    fun toEditingCategoryWithSubcategories(
        checkedCategoryList: List<Category>
    ): EditingCategoryWithSubcategories {
        val subcategoryList = subcategoryList.toCheckedCategoryList(checkedCategoryList)
        val checkedAndUnchecked = subcategoryList.partition { it.checked }

        return EditingCategoryWithSubcategories(
            category = category,
            checked = if (checkedAndUnchecked.first.isEmpty()) {
                false
            } else if (checkedAndUnchecked.second.isEmpty()) {
                true
            } else {
                null
            },
            subcategoryList = subcategoryList
        )
    }

}
