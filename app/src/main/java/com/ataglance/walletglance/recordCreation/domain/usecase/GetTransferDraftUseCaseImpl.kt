package com.ataglance.walletglance.recordCreation.domain.usecase

import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.utils.getOtherFrom
import com.ataglance.walletglance.record.domain.usecase.GetLastRecordNumUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordStackUseCase
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferPairRecordStacks
import com.ataglance.walletglance.recordCreation.mapper.toTransferDraft
import com.ataglance.walletglance.recordCreation.presentation.model.transfer.TransferDraft
import com.ataglance.walletglance.recordCreation.presentation.model.transfer.TransferDraftUnits

class GetTransferDraftUseCaseImpl(
    private val getRecordStackUseCase: GetRecordStackUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getLastRecordNumUseCase: GetLastRecordNumUseCase
) : GetTransferDraftUseCase {

    override suspend fun get(recordNum: Int?): TransferDraft {
        val accounts = getAccountsUseCase.getAll()

        return recordNum
            ?.let { getTransfersPair(recordNum) }
            ?.toTransferDraft(accounts = accounts)
            ?: getClearTransferDraft(
                recordNum = recordNum ?: getLastRecordNumUseCase.getNext(),
                accountsAndActiveOne = AccountsAndActiveOne(
                    accounts = accounts,
                    activeAccount = accounts.firstOrNull { it.isActive }
                )
            )
    }

    private suspend fun getTransfersPair(recordNum: Int): TransferPairRecordStacks? {
        val first = getRecordStackUseCase.get(recordNum) ?: return null
        val second = getRecordStackUseCase.get(
            recordNum = recordNum + if (first.isOutTransfer()) 1 else -1
        ) ?: return null
        return TransferPairRecordStacks.fromRecordStacks(first to second)
    }

    private fun getClearTransferDraft(
        recordNum: Int,
        accountsAndActiveOne: AccountsAndActiveOne,
    ): TransferDraft {
        return TransferDraft(
            isNew = true,
            sender = TransferDraftUnits(
                account = accountsAndActiveOne.activeAccount,
                recordNum = recordNum
            ),
            receiver = TransferDraftUnits(
                account = accountsAndActiveOne.activeAccount?.let {
                    accountsAndActiveOne.accounts.getOtherFrom(it)
                },
                recordNum = recordNum + 1
            )
        )
    }

}