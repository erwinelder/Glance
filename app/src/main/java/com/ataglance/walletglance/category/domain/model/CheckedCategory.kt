package com.ataglance.walletglance.category.domain.model

data class CheckedCategory(
    val category: Category,
    val checked: Boolean = false
) {

    fun inverseCheckedState(): CheckedCategory {
        return this.copy(checked = !checked)
    }

}
