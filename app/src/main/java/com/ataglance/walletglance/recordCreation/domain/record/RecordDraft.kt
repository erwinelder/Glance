package com.ataglance.walletglance.recordCreation.domain.record

data class RecordDraft(
    val general: RecordDraftGeneral,
    val items: List<RecordDraftItem>
)
