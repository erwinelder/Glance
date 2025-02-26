package com.ataglance.walletglance.recordCreation.presentation.model.record

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraftPreferences

data class RecordDraftGeneral(
    val isNew: Boolean = true,
    val recordNum: Int = 0,
    val account: Account? = null,
    val type: CategoryType = CategoryType.Expense,
    val dateTimeState: DateTimeState = DateTimeState.fromCurrentTime(),
    val preferences: RecordDraftPreferences = RecordDraftPreferences()
) {

    fun savingIsAllowed(): Boolean {
        return account != null
    }

}
