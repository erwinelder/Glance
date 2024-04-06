package com.ataglance.walletglance.model

import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.theme.LighterDarkerColors
import java.util.Locale

class AccountController {

    fun accountToEditAccountUiState(account: Account): EditAccountUiState {
        return EditAccountUiState(
            id = account.id,
            orderNum = account.orderNum,
            name = account.name,
            currency = account.currency,
            balance = account.balance.toString(),
            colorName = account.color,
            hide = account.hide,
            hideBalance = account.hideBalance,
            withoutBalance = account.withoutBalance,
            isActive = account.isActive
        )
    }

    fun getAccountById(id: Int, accountList: List<Account>): Account? {
        accountList.forEach { account ->
            if (account.id == id) {
                return account
            }
        }
        return null
    }

    fun getAccountAnotherFrom(account: Account, accountList: List<Account>): Account {
        for (i in accountList.indices) {
            if (accountList[i].id == account.id) {
                return if (accountList.lastIndex > i) {
                    accountList[i + 1]
                } else if (i > 0) {
                    accountList[i - 1]
                } else {
                    account
                }
            }
        }
        return account
    }

    fun addToBalance(
        balance: Double,
        amount: Double
    ): Double {
        return "%.2f".format(Locale.US, balance + amount).toDouble()
    }

    fun subtractFromBalance(
        balance: Double,
        amount: Double
    ): Double {
        return "%.2f".format(Locale.US, balance - amount).toDouble()
    }

    fun addToOrSubtractFromBalance(
        balance: Double,
        amount: Double,
        recordType: RecordType
    ): Double {
        return "%.2f".format(
            Locale.US,
            balance + if (recordType == RecordType.Expense) -amount else amount
        ).toDouble()
    }

    fun reapplyAmountToBalance(
        balance: Double,
        prevAmount: Double,
        newAmount: Double,
        recordType: RecordType
    ): Double {
        return "%.2f".format(
            Locale.US,
            balance +
                    (if (recordType == RecordType.Expense) prevAmount else -prevAmount) +
                    if (recordType == RecordType.Expense) -newAmount else newAmount
        ).toDouble()
    }

    fun returnAmountToOneBalanceAndUpdateAnotherBalance(
        balancePrevAccount: Double,
        balanceNewAccount: Double,
        prevAmount: Double,
        newAmount: Double,
        recordType: RecordType
    ): Pair<Double, Double> {
        return "%.2f".format(
            Locale.US,
            balancePrevAccount + if (recordType == RecordType.Expense) prevAmount else -prevAmount
        ).toDouble() to "%.2f".format(
            Locale.US,
            balanceNewAccount + if (recordType == RecordType.Expense) -newAmount else newAmount
        ).toDouble()
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

}