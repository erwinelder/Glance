package com.ataglance.walletglance.category.presentation.model

import com.ataglance.walletglance.category.domain.model.Category

data class CheckedCategoryWithSubcategories(
    val category: Category,
    val checked: Boolean?,
    val subcategoryList: List<CheckedCategory>,
    val expanded: Boolean = false
) {

    fun inverseCheckedState(): CheckedCategoryWithSubcategories {
        val newChecked = !(checked ?: false)

        return this.copy(
            checked = newChecked,
            subcategoryList = subcategoryList.map { it.copy(checked = newChecked) }
        )
    }

}