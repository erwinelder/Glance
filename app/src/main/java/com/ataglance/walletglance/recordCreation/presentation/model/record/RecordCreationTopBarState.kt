package com.ataglance.walletglance.recordCreation.presentation.model.record

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.core.domain.date.DateTimeState

data class RecordCreationTopBarState(
    val isNew: Boolean = false,
    val categoryType: CategoryType = CategoryType.Expense,
    val dateTimeState: DateTimeState = DateTimeState.fromCurrentTime(),
    val selectedAccount: Account? = null
) {

    fun savingIsAllowed(): Boolean {
        return selectedAccount != null
    }

}
