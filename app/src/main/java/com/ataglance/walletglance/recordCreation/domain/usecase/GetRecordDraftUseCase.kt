package com.ataglance.walletglance.recordCreation.domain.usecase

import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraft

interface GetRecordDraftUseCase {
    suspend fun get(
        recordNum: Int?,
        categoryWithSub: CategoryWithSub?
    ): RecordDraft
}