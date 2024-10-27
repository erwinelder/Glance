package com.ataglance.walletglance.category.domain.model

data class EditingCategoryWithSubcategories(
    val category: Category,
    val checked: Boolean?,
    val subcategoryList: List<CheckedCategory>,
    val expanded: Boolean = false
) {

    fun inverseCheckedState(): EditingCategoryWithSubcategories {
        val newChecked = !(checked ?: false)

        return this.copy(
            checked = newChecked,
            subcategoryList = subcategoryList.map { it.copy(checked = newChecked) }
        )
    }

}
