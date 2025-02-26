package com.ataglance.walletglance.category.domain.model

import com.ataglance.walletglance.category.domain.utils.findById
import com.ataglance.walletglance.core.utils.deleteItemAndMoveOrderNum

data class GroupedCategories(
    val category: Category,
    val subcategoryList: List<Category> = emptyList()
) {

    fun appendNewSubcategory(subcategory: Category): GroupedCategories {
        val list = subcategoryList.toMutableList()
        list.add(
            subcategory.copy(
                orderNum = (list.maxOfOrNull { it.orderNum } ?: 0) + 1
            )
        )
        return this.copy(subcategoryList = list)
    }

    fun replaceSubcategory(subcategory: Category): GroupedCategories {
        return this.copy(
            subcategoryList = subcategoryList.map {
                it.takeIf { it.id != subcategory.id } ?: subcategory
            }
        )
    }

    fun deleteSubcategoryById(id: Int): GroupedCategories {
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

    fun getWithSubcategoryWithIdOrWithoutSubcategory(id: Int?): CategoryWithSub {
        val subcategory = id?.let { subcategoryList.findById(it) }
        return CategoryWithSub(category, subcategory)
    }

    fun getWithSubcategoryWithId(id: Int): CategoryWithSub {
        return CategoryWithSub(category, subcategoryList.findById(id))
    }

    fun getWithFirstSubcategory(): CategoryWithSub {
        return CategoryWithSub(category, subcategoryList.firstOrNull())
    }

    fun getWithLastSubcategory(): CategoryWithSub {
        return CategoryWithSub(category, subcategoryList.lastOrNull())
    }

    fun getWithoutSubcategory(): CategoryWithSub {
        return CategoryWithSub(category)
    }

    fun asSingleList(): List<Category> {
        return listOf(category) + subcategoryList
    }

    fun fixSubcategoriesOrderNumbers(): GroupedCategories {
        return this.copy(
            subcategoryList = subcategoryList.mapIndexed { index, category ->
                category.copy(orderNum = index + 1)
            }
        )
    }

}
