package com.ataglance.walletglance.transaction.mapper

import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.mapper.toRecordAccount
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.utils.formatTimestampAsDayMonthNameYear
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.transaction.domain.model.RecordItem
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import com.ataglance.walletglance.transaction.domain.model.Transaction
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transaction.domain.utils.containTransactionsFromDifferentYears
import com.ataglance.walletglance.transaction.domain.utils.distinctByCategories
import com.ataglance.walletglance.transaction.domain.utils.foldNotesByCategory
import com.ataglance.walletglance.transaction.presentation.model.RecordItemUiState
import com.ataglance.walletglance.transaction.presentation.model.RecordUiState
import com.ataglance.walletglance.transaction.presentation.model.TransactionUiState
import com.ataglance.walletglance.transaction.presentation.model.TransferUiState


fun RecordItem.toUiState(
    categoryType: CategoryType,
    groupedCategoriesByType: GroupedCategoriesByType
): RecordItemUiState {
    val categoryWithSub = groupedCategoriesByType
        .getGroupedCategories(categoryId = categoryId, type = categoryType)
        ?.getWithSubcategory(id = subcategoryId)

    return RecordItemUiState(categoryWithSub = categoryWithSub, note = note)
}

fun List<RecordItem>.toUiStates(
    categoryType: CategoryType,
    groupedCategoriesByType: GroupedCategoriesByType
): List<RecordItemUiState> {
    return distinctByCategories(3).map { item ->
        item
            .copy(note = foldNotesByCategory(item.categoryId, item.subcategoryId))
            .toUiState(
                categoryType = categoryType, groupedCategoriesByType = groupedCategoriesByType
            )
    }
}

fun RecordWithItems.toUiState(
    accounts: List<Account>,
    groupedCategoriesByType: GroupedCategoriesByType,
    includeYearInDate: Boolean,
    resourceManager: ResourceManager
): RecordUiState? {
    val date = date.formatTimestampAsDayMonthNameYear(
        resourceManager = resourceManager, includeYear = includeYearInDate
    )

    val account = accounts.find { it.id == accountId }?.toRecordAccount() ?: return null

    val sign = if (isOfType(CategoryType.Expense)) '-' else '+'
    val totalAmount = "%c %s %s".format(sign, totalAmount.formatWithSpaces(), account.currency)

    return RecordUiState(
        id = recordId,
        date = date,
        type = type,
        account = account,
        totalAmount = totalAmount,
        items = items.toUiStates(
            categoryType = type, groupedCategoriesByType = groupedCategoriesByType
        )
    )
}


fun Transfer.toUiState(
    accountId: Int,
    accounts: List<Account>,
    includeYearInDate: Boolean,
    resourceManager: ResourceManager
): TransferUiState? {
    val date = date.formatTimestampAsDayMonthNameYear(
        resourceManager = resourceManager, includeYear = includeYearInDate
    )

    val type = when {
        senderAccountId == accountId -> CategoryType.Expense
        receiverAccountId == accountId -> CategoryType.Income
        else -> return null
    }
    val account = accounts.find { it.id == accountId }?.toRecordAccount() ?: return null
    val secondAccountId = if (type == CategoryType.Expense) receiverAccountId else senderAccountId
    val secondAccountCompanionText = when (type) {
        CategoryType.Expense -> resourceManager.getString(R.string.to_account_meaning)
        CategoryType.Income -> resourceManager.getString(R.string.from_account_meaning)
    }
    val secondAccount = accounts.find { it.id == secondAccountId }?.toRecordAccount() ?: return null

    val transferAmount = if (type == CategoryType.Expense) senderAmount else receiverAmount
    val sign = if (type == CategoryType.Expense) '-' else '+'
    val amount = "%c %s %s".format(sign, transferAmount.formatWithSpaces(), account.currency)

    return TransferUiState(
        id = id,
        date = date,
        type = type,
        account = account,
        secondAccountCompanionText = secondAccountCompanionText,
        secondAccount = secondAccount,
        amount = amount
    )
}


fun Transaction.toUiState(
    accountId: Int,
    accounts: List<Account>,
    groupedCategoriesByType: GroupedCategoriesByType,
    includeYearInDate: Boolean,
    resourceManager: ResourceManager
): TransactionUiState? {
    return when (this) {
        is RecordWithItems -> this.toUiState(
            accounts = accounts,
            groupedCategoriesByType = groupedCategoriesByType,
            includeYearInDate = includeYearInDate,
            resourceManager = resourceManager
        )
        is Transfer -> this.toUiState(
            accountId = accountId,
            accounts = accounts,
            includeYearInDate = includeYearInDate,
            resourceManager = resourceManager
        )
    }
}

fun List<Transaction>.toUiStates(
    accountId: Int,
    accounts: List<Account>,
    groupedCategoriesByType: GroupedCategoriesByType,
    resourceManager: ResourceManager
): List<TransactionUiState> {
    val includeYearInDate = containTransactionsFromDifferentYears()

    return mapNotNull { transaction ->
        transaction.toUiState(
            accountId = accountId,
            accounts = accounts,
            groupedCategoriesByType = groupedCategoriesByType,
            includeYearInDate = includeYearInDate,
            resourceManager = resourceManager
        )
    }
}
