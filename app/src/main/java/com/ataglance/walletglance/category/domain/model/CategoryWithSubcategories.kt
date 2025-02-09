package com.ataglance.walletglance.category.domain.model

import com.ataglance.walletglance.category.domain.utils.findById
import com.ataglance.walletglance.core.utils.deleteItemAndMoveOrderNum

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

    fun changeSubcategoriesColorTo(categoryColor: CategoryColor): List<Category> {
        return if (subcategoryList.firstOrNull()?.color?.name == categoryColor.name) {
            subcategoryList
        } else {
            subcategoryList.map { it.copy(color = categoryColor) }
        }
    }

    fun getWithSubcategoryWithIdOrWithoutSubcategory(id: Int?): CategoryWithSubcategory {
        val subcategory = id?.let { subcategoryList.findById(it) }
        return CategoryWithSubcategory(category, subcategory)
    }

    fun getWithSubcategoryWithId(id: Int): CategoryWithSubcategory {
        return CategoryWithSubcategory(category, subcategoryList.findById(id))
    }

    fun getWithFirstSubcategory(): CategoryWithSubcategory {
        return CategoryWithSubcategory(category, subcategoryList.firstOrNull())
    }

    fun getWithLastSubcategory(): CategoryWithSubcategory {
        return CategoryWithSubcategory(category, subcategoryList.lastOrNull())
    }

    fun getWithoutSubcategory(): CategoryWithSubcategory {
        return CategoryWithSubcategory(category)
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

}
