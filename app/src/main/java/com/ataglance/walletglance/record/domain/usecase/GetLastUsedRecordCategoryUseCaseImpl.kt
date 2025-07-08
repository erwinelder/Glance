package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.mapper.toDomainModelWithItems

class GetLastUsedRecordCategoryUseCaseImpl(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val recordRepository: RecordRepository
) : GetLastUsedRecordCategoryUseCase {

    override suspend fun execute(
        type: CategoryType,
        accountId: Int?,
        categories: GroupedCategoriesByType?
    ): CategoryWithSub? {
        val categories = categories ?: getCategoriesUseCase.getGrouped()

        val recordItem = accountId
            ?.let {
                recordRepository
                    .getLastRecordWithItemsByTypeAndAccount(
                        type = type.asChar(), accountId = accountId
                    )
                    ?.toDomainModelWithItems()
            }
            ?.items?.firstOrNull()

        return if (recordItem != null) {
            categories.getCategoryWithSub(
                categoryId = recordItem.categoryId,
                subcategoryId = recordItem.subcategoryId,
                type = type
            )
        } else {
            categories.getLastCategoryWithSubByType(type = type)
        }
    }

}