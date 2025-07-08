package com.ataglance.walletglance.category.domain.model

import com.ataglance.walletglance.category.domain.utils.findById
import com.ataglance.walletglance.core.utils.deleteItemAndMoveOrderNum

data class GroupedCategories(
    val category: Category,
    val subcategories: List<Category> = emptyList()
) {

    val categoryId: Int
        get() = category.id


    fun appendNewSubcategory(subcategory: Category): GroupedCategories {
        val subcategories = subcategories.toMutableList().apply {
            add(
                subcategory.copy(orderNum = (subcategories.maxOfOrNull { it.orderNum } ?: 0) + 1)
            )
        }
        return copy(subcategories = subcategories)
    }

    fun replaceSubcategory(subcategory: Category): GroupedCategories {
        return copy(
            subcategories = subcategories.map {
                it.takeUnless { it.id == subcategory.id } ?: subcategory
            }
        )
    }

    fun deleteSubcategoryById(id: Int): GroupedCategories {
        return copy(
            subcategories = subcategories.deleteItemAndMoveOrderNum(
                { it.id == id }, { it.copy(orderNum = it.orderNum - 1) }
            )
        )
    }

    fun changeSubcategoriesColorTo(categoryColor: CategoryColor): List<Category> {
        return if (subcategories.firstOrNull()?.color?.name == categoryColor.name) {
            subcategories
        } else {
            subcategories.map { it.copy(color = categoryColor) }
        }
    }

    fun getSubcategoryById(id: Int?): Category? {
        return id?.let { subcategories.findById(it) }
    }

    fun getWithSubcategoryOrNull(id: Int?): CategoryWithSub? {
        val subcategory = getSubcategoryById(id = id) ?: return null

        return CategoryWithSub(
            category = category,
            subcategory = subcategory
        )
    }

    fun getWithSubcategory(id: Int?): CategoryWithSub {
        return CategoryWithSub(
            category = category,
            subcategory = id?.let { subcategories.findById(it) }
        )
    }

    fun getWithFirstSubcategory(): CategoryWithSub {
        return CategoryWithSub(category = category, subcategory = subcategories.firstOrNull())
    }

    fun getWithLastSubcategory(): CategoryWithSub {
        return CategoryWithSub(category = category, subcategory = subcategories.lastOrNull())
    }

    fun asSingleList(): List<Category> {
        return listOf(category) + subcategories
    }

    fun fixSubcategoriesOrderNumbers(): GroupedCategories {
        return copy(
            subcategories = subcategories.mapIndexed { index, category ->
                category.copy(orderNum = index + 1)
            }
        )
    }

}
