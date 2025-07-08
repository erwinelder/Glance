package com.ataglance.walletglance.transaction.domain.model

import com.ataglance.walletglance.category.domain.model.CategoryType

data class Record(
    val id: Long,
    val date: Long,
    val type: CategoryType,
    val accountId: Int,
    val includeInBudgets: Boolean
) {

    val isNew: Boolean
        get() = id == 0L

}
