package com.ataglance.walletglance.category.presentation.model

import com.ataglance.walletglance.category.domain.model.Category

data class CheckedCategory(
    val category: Category,
    val checked: Boolean = false
) {

    fun inverseCheckedState(): CheckedCategory {
        return this.copy(checked = !checked)
    }

}
