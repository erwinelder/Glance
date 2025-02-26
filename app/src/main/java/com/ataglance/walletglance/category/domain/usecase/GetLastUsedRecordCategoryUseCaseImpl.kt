package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.record.domain.usecase.GetRecordStackUseCase

class GetLastUsedRecordCategoryUseCaseImpl(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getRecordStackUseCase: GetRecordStackUseCase
) : GetLastUsedRecordCategoryUseCase {

    override suspend fun get(type: CategoryType): CategoryWithSub? {
        val categories = getCategoriesUseCase.getGrouped()

        val accountId = getAccountsUseCase.getActive()?.id
            ?: return categories.getLastCategoryWithSubByType(type)

        return getRecordStackUseCase
            .getLastByTypeAndAccount(type = type, accountId = accountId, categories = categories)
            ?.stack?.firstOrNull()
            ?.categoryWithSub
            ?: categories.getLastCategoryWithSubByType(type)
    }

}