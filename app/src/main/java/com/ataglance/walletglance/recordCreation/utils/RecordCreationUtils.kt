package com.ataglance.walletglance.recordCreation.utils

import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.AccountsAndActiveOne
import com.ataglance.walletglance.account.utils.getOtherFrom
import com.ataglance.walletglance.category.domain.CategoryWithSubcategory
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.utils.findByRecordNum
import com.ataglance.walletglance.record.utils.getOutAndInTransfersByRecordNum
import com.ataglance.walletglance.recordCreation.domain.mapper.toRecordDraft
import com.ataglance.walletglance.recordCreation.domain.mapper.toTransferDraft
import com.ataglance.walletglance.recordCreation.domain.record.CreatedRecordItem
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraft
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraftGeneral
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraftItem
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferDraft
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferDraftSenderReceiver


fun List<RecordStack>.getRecordDraft(
    isNew: Boolean,
    recordNum: Int,
    accountsAndActiveOne: AccountsAndActiveOne,
    initialCategoryWithSubcategory: CategoryWithSubcategory?
): RecordDraft {
    return this
        .takeUnless { isNew }
        ?.findByRecordNum(recordNum)
        ?.toRecordDraft(accountsAndActiveOne.accountList)
        ?: getClearRecordDraft(
            recordNum = recordNum,
            account = accountsAndActiveOne.activeAccount,
            categoryWithSubcategory = initialCategoryWithSubcategory
        )
}

private fun getClearRecordDraft(
    recordNum: Int,
    account: Account?,
    categoryWithSubcategory: CategoryWithSubcategory?
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
                categoryWithSubcategory = categoryWithSubcategory
            )
        )
    )
}



fun List<RecordStack>.getTransferDraft(
    isNew: Boolean,
    recordNum: Int,
    accountsAndActiveOne: AccountsAndActiveOne
): TransferDraft {
    return this
        .takeUnless { isNew }
        ?.getOutAndInTransfersByRecordNum(recordNum)
        ?.toTransferDraft(accountList = accountsAndActiveOne.accountList)
        ?: getClearTransferDraft(recordNum = recordNum, accountsAndActiveOne = accountsAndActiveOne)
}

private fun getClearTransferDraft(
    recordNum: Int,
    accountsAndActiveOne: AccountsAndActiveOne,
): TransferDraft {
    return TransferDraft(
        isNew = true,
        sender = TransferDraftSenderReceiver(
            account = accountsAndActiveOne.activeAccount,
            recordNum = recordNum
        ),
        receiver = TransferDraftSenderReceiver(
            account = accountsAndActiveOne.activeAccount?.let {
                accountsAndActiveOne.accountList.getOtherFrom(it)
            },
            recordNum = recordNum + 1
        )
    )
}



fun List<RecordDraftItem>.copyWithCategoryAndSubcategory(
    categoryWithSubcategory: CategoryWithSubcategory?
): List<RecordDraftItem> {
    return this.map { it.copy(categoryWithSubcategory = categoryWithSubcategory) }
}

fun List<CreatedRecordItem>.getTotalAmount(): Double {
    return this.fold(0.0) { acc, item ->
        acc + item.totalAmount
    }
}
