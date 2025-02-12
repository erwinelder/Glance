package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategory
import com.ataglance.walletglance.record.domain.usecase.GetRecordStackUseCase

class GetLastUsedRecordCategoryUseCaseImpl(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getRecordStackUseCase: GetRecordStackUseCase
) : GetLastUsedRecordCategoryUseCase {

    override suspend fun get(type: CategoryType): CategoryWithSubcategory? {
        val categories = getAllCategoriesUseCase.get()

        val accountId = getAccountsUseCase.getActive()?.id
            ?: return categories.getLastCategoryWithSubcategoryByType(type)

        return getRecordStackUseCase
            .getLastByTypeAndAccount(type = type, accountId = accountId, categories = categories)
            ?.stack?.firstOrNull()
            ?.categoryWithSubcategory
            ?: categories.getLastCategoryWithSubcategoryByType(type)
    }

}