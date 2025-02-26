package com.ataglance.walletglance.account.domain.utils

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.model.CurrencyItem
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.color.ColorWithName
import com.ataglance.walletglance.core.utils.roundToTwoDecimals
import com.ataglance.walletglance.record.domain.model.RecordType
import java.util.Currency


fun Set<Currency>.toSortedCurrencyItemList(): List<CurrencyItem> {
    return this
        .map { CurrencyItem(it) }
        .sortedBy { it.currencyCode }
}


fun getAccountColorsWithNames(theme: AppTheme): List<ColorWithName> {
    return AccountColors.asList().map { it.toColorWithName(theme) }
}

fun AccountColors.toColorWithName(theme: AppTheme): ColorWithName {
    return ColorWithName(this.name.name, this.color.getByTheme(theme).lighter)
}


fun List<Account>.findById(id: Int): Account? {
    return this.find { it.id == id }
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
    return Pair(
        first = first.copy(
            balance = (first.balance + if (recordType == RecordType.Expense) prevAmount else -prevAmount)
                .roundToTwoDecimals()
        ),
        second = second.copy(
            balance = (second.balance + if (recordType == RecordType.Expense) -newAmount else newAmount)
                .roundToTwoDecimals()
        )
    )
}


fun List<Account>.filterByBudgetAccounts(budget: Budget): List<Account> {
    return filter { it.id in budget.linkedAccountsIds }
}
