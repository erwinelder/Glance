package com.ataglance.walletglance.recordCreation.presentation.model.record

data class RecordDraft(
    val general: RecordDraftGeneral,
    val items: List<RecordDraftItem>
)
