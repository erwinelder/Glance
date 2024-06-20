package com.ataglance.walletglance.ui.utils

import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.domain.entities.Account
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.theme.LighterDarkerColors
import com.ataglance.walletglance.data.accounts.AccountColorName
import com.ataglance.walletglance.data.accounts.AccountColorsByTheme
import com.ataglance.walletglance.data.records.RecordType
import java.util.Locale


fun List<Account>.findById(id: Int): Account? {
    return this.find { it.id == id }
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


fun List<Account>.getIdsThatAreNotInList(list: List<Account>): List<Int> {
    return this
        .filter { list.findById(it.id) == null }
        .map { it.id }
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


fun getAccountAndOnAccountColor(
    storageColor: String,
    appTheme: AppTheme?
): Pair<LighterDarkerColors, Color> {
    return when (appTheme) {
        AppTheme.LightDefault -> getAccountBackgroundAndColorLightDefaultTheme(storageColor)
        AppTheme.DarkDefault -> getAccountBackgroundAndColorDarkDefaultTheme(storageColor)
        else -> null
    } ?: (LighterDarkerColors() to Color.White)
}


private fun getAccountBackgroundAndColorLightDefaultTheme(
    storageColor: String
): Pair<LighterDarkerColors, Color>? {
    return when(storageColor) {
        AccountColorName.Default.name -> LighterDarkerColors(
            AccountColorsByTheme().light.default.lighter, AccountColorsByTheme().light.default.darker
        ) to Color(240, 240, 240)
        AccountColorName.Pink.name -> LighterDarkerColors(
            AccountColorsByTheme().light.pink.lighter, AccountColorsByTheme().light.pink.darker
        ) to Color(240, 240, 240)
        AccountColorName.Blue.name -> LighterDarkerColors(
            AccountColorsByTheme().light.blue.lighter, AccountColorsByTheme().light.blue.darker
        ) to Color(240, 240, 240)
        AccountColorName.Camel.name -> LighterDarkerColors(
            AccountColorsByTheme().light.camel.lighter, AccountColorsByTheme().light.camel.darker
        ) to Color(240, 240, 240)
        AccountColorName.Red.name -> LighterDarkerColors(
            AccountColorsByTheme().light.red.lighter, AccountColorsByTheme().light.red.darker
        ) to Color(240, 240, 240)
        AccountColorName.Green.name -> LighterDarkerColors(
            AccountColorsByTheme().light.green.lighter, AccountColorsByTheme().light.green.darker
        ) to Color(240, 240, 240)
        else -> null
    }
}


private fun getAccountBackgroundAndColorDarkDefaultTheme(
    storageColor: String
): Pair<LighterDarkerColors, Color>? {
    return when(storageColor) {
        AccountColorName.Default.name -> LighterDarkerColors(
            AccountColorsByTheme().dark.default.lighter, AccountColorsByTheme().dark.default.darker
        ) to Color(18, 18, 18)
        AccountColorName.Pink.name -> LighterDarkerColors(
            AccountColorsByTheme().dark.pink.lighter, AccountColorsByTheme().dark.pink.darker
        ) to Color(240, 240, 240)
        AccountColorName.Blue.name -> LighterDarkerColors(
            AccountColorsByTheme().dark.blue.lighter, AccountColorsByTheme().dark.blue.darker
        ) to Color(240, 240, 240)
        AccountColorName.Camel.name -> LighterDarkerColors(
            AccountColorsByTheme().dark.camel.lighter, AccountColorsByTheme().dark.camel.darker
        ) to Color(240, 240, 240)
        AccountColorName.Red.name -> LighterDarkerColors(
            AccountColorsByTheme().dark.red.lighter, AccountColorsByTheme().dark.red.darker
        ) to Color(240, 240, 240)
        AccountColorName.Green.name -> LighterDarkerColors(
            AccountColorsByTheme().dark.green.lighter, AccountColorsByTheme().dark.green.darker
        ) to Color(240, 240, 240)
        else -> null
    }
}


fun List<Account>.checkOrderNumbers(): Boolean {
    this.sortedBy { it.orderNum }.forEachIndexed { index, account ->
        if (account.orderNum != index + 1) {
            return false
        }
    }
    return true
}


fun List<Account>.fixOrderNumbers(): List<Account> {
    return this.sortedBy { it.orderNum }.mapIndexed { index, account ->
        account.copy(orderNum = index + 1)
    }
}
