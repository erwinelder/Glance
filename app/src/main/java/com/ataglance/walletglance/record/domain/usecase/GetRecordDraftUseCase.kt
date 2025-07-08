package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.record.presentation.model.RecordDraftWithItems

interface GetRecordDraftUseCase {

    suspend fun execute(
        id: Long? = null,
        accountId: Int? = null,
        accounts: List<Account>? = null,
        categoryWithSub: CategoryWithSub? = null,
        categories: GroupedCategoriesByType? = null
    ): RecordDraftWithItems

}