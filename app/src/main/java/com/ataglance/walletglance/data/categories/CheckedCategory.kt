package com.ataglance.walletglance.data.categories

data class CheckedCategory(
    val category: Category,
    val checked: Boolean = false
) {

    fun inverseCheckedState(): CheckedCategory {
        return this.copy(checked = !checked)
    }

}
