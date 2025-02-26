package com.ataglance.walletglance.recordCreation.presentation.model.record

data class RecordDraft(
    val general: RecordDraftGeneral,
    val items: List<RecordDraftItem>
) {

    fun savingIsAllowed(): Boolean {
        return general.savingIsAllowed() && items.all { it.savingIsAllowed() }
    }

}
