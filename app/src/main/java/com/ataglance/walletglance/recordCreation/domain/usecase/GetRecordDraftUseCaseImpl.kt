package com.ataglance.walletglance.recordCreation.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.record.domain.usecase.GetLastRecordNumUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordStackUseCase
import com.ataglance.walletglance.recordCreation.mapper.toRecordDraft
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraft
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftGeneral
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftItem

class GetRecordDraftUseCaseImpl(
    private val getRecordStackUseCase: GetRecordStackUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getLastRecordNumUseCase: GetLastRecordNumUseCase
) : GetRecordDraftUseCase {

    override suspend fun get(
        recordNum: Int?,
        categoryWithSub: CategoryWithSub?
    ): RecordDraft {
        val accounts = getAccountsUseCase.getAll()

        return recordNum
            ?.let { getRecordStackUseCase.get(it) }
            ?.toRecordDraft(accounts = accounts)
            ?: getClearRecordDraft(
                recordNum = getLastRecordNumUseCase.getNext(),
                account = accounts.firstOrNull { it.isActive },
                categoryWithSub = categoryWithSub
            )
    }

    private fun getClearRecordDraft(
        recordNum: Int,
        account: Account?,
        categoryWithSub: CategoryWithSub?
    ): RecordDraft {
        return RecordDraft(
            general = RecordDraftGeneral(
                isNew = true,
                recordNum = recordNum,
                account = account
            ),
            items = listOf(
                RecordDraftItem(
                    lazyListKey = 0,
                    index = 0,
                    categoryWithSub = categoryWithSub
                )
            )
        )
    }

}