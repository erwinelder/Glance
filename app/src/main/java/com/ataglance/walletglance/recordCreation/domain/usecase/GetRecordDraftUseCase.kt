package com.ataglance.walletglance.recordCreation.domain.usecase

import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategory
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraft

interface GetRecordDraftUseCase {
    suspend fun get(
        recordNum: Int?,
        categoryWithSubcategory: CategoryWithSubcategory?
    ): RecordDraft
}