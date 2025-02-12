package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.record.domain.model.RecordStack

interface GetRecordStackUseCase {

    suspend fun get(recordNum: Int): RecordStack?

    suspend fun getLastByTypeAndAccount(
        type: CategoryType,
        accountId: Int,
        categories: CategoriesWithSubcategories
    ): RecordStack?

}