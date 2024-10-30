package com.ataglance.walletglance.account.domain.utils

import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.color.AccountColorWithName
import com.ataglance.walletglance.account.domain.color.AccountColors
import com.ataglance.walletglance.account.domain.color.AccountPossibleColors
import com.ataglance.walletglance.account.mapper.toAccountList
import com.ataglance.walletglance.account.presentation.viewmodel.CurrencyItem
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.color.ColorWithName
import com.ataglance.walletglance.record.domain.RecordType
import java.util.Currency
import java.util.Locale


fun Set<Currency>.toSortedCurrencyItemList(): List<CurrencyItem> {
    return this
        .map { CurrencyItem(it) }
        .sortedBy { it.currencyCode }
}


fun AccountColors.toAccountColorWithName(): AccountColorWithName {
    return AccountColorWithName(this.name, this.color, this.colorOn)
}


fun AccountColors.toColorWithName(theme: AppTheme): ColorWithName {
    return ColorWithName(this.name.name, this.color.getByTheme(theme).lighter)
}


fun List<AccountEntity>.toAccountList(): List<Account> {
    val possibleColors = AccountPossibleColors()
    return this.toAccountList(accountColorProvider = possibleColors::getByName)
}


fun List<Account>.findById(id: Int): Account? {
    return this.find { it.id == id }
}


fun List<Account>.findByOrderNum(orderNum: Int): Account? {
    return this.find { it.orderNum == orderNum }
}


fun List<Account>.makeSureThereIsOnlyOneActiveAccount(): List<Account> {
    return this.takeIf { list ->
        list.filter { it.isActive }.size == 1
    }
    ?: this.mapIndexed { index, account ->
        account.copy(isActive = index == 0)
    }
}


fun List<Account>.makeSureActiveAccountIsVisibleOne(): List<Account> {
    return this
        .takeIf { list -> list.none { it.isActive && it.hide } }
        ?: this.makeFirstVisibleAccountActive()
        ?: this
            .takeIf { it.isNotEmpty() }
            ?.toMutableList()
            ?.apply { this[0] = this[0].copy(isActive = true, hide = false) }
        ?: this
}

fun List<Account>.makeFirstVisibleAccountActive(): List<Account>? {
    return this
        .find { !it.hide }
        ?.let { visibleAccount ->
            this.map { account ->
                account.takeIf { it.id != visibleAccount.id }
                    ?: visibleAccount.copy(isActive = true)
            }
        }
}


fun List<Account>.getOtherFrom(account: Account): Account {
    for (i in this.indices) {
        if (this[i].id == account.id) {
            return if (this.lastIndex > i) {
                this[i + 1]
            } else if (i > 0) {
                this[i - 1]
            } else {
                account
            }
        }
    }
    return account
}


fun List<Account>.mergeWith(list: List<Account>): List<Account> {
    val mergedList = this.toMutableList()

    list.forEach { accountFromSecondaryList ->
        accountFromSecondaryList
            .takeIf { mergedList.findById(it.id) == null }
            ?.let { mergedList.add(it) }
    }

    return mergedList
}


fun List<Account>.fixOrderNums(): List<Account> {
    return this.mapIndexed { index, account ->
        account.copy(orderNum = index + 1)
    }
}


fun Pair<Account, Account>.returnAmountToFirstBalanceAndUpdateSecondBalance(
    prevAmount: Double,
    newAmount: Double,
    recordType: RecordType
): Pair<Account, Account> {
    return this.first.copy(
        balance = "%.2f".format(
            Locale.US,
            this.first.balance +
                    if (recordType == RecordType.Expense) prevAmount else -prevAmount
        ).toDouble()
    ) to this.second.copy(
        balance = "%.2f".format(
            Locale.US,
            this.second.balance +
                    if (recordType == RecordType.Expense) -newAmount else newAmount
        ).toDouble()
    )
}
