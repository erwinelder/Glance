package com.ataglance.walletglance.record.presentation.model

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.core.domain.date.DateTimeState

data class RecordDraft(
    val recordId: Long = 0,
    val type: CategoryType = CategoryType.Expense,
    val dateTimeState: DateTimeState = DateTimeState.fromCurrentTime(),
    val account: Account? = null,
    val preferences: RecordDraftPreferences = RecordDraftPreferences()
) {

    companion object {

        fun asNew(account: Account?): RecordDraft {
            return RecordDraft(account = account)
        }

    }


    val isNew: Boolean
        get() = recordId == 0L

    val savingIsAllowed: Boolean
        get() = account != null

}
