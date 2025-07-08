package com.ataglance.walletglance.record.presentation.model

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.core.utils.asList

data class RecordDraftWithItems(
    val record: RecordDraft,
    val items: List<RecordDraftItem>
) {

    companion object {

        fun asNew(account: Account?, categoryWithSub: CategoryWithSub?): RecordDraftWithItems {
            return RecordDraftWithItems(
                record = RecordDraft.asNew(account = account),
                items = RecordDraftItem.asNew(categoryWithSub = categoryWithSub).asList()
            )
        }

    }


    val savingIsAllowed: Boolean
        get() = record.savingIsAllowed && items.all { it.savingIsAllowed }

}
