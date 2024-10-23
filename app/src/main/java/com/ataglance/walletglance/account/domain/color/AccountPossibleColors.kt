package com.ataglance.walletglance.account.domain.color

import com.ataglance.walletglance.account.domain.utils.toColorWithName
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.color.ColorWithName

data class AccountPossibleColors(
    val default: AccountColors = AccountColors.Default,
    val pink: AccountColors = AccountColors.Pink,
    val blue: AccountColors = AccountColors.Blue,
    val camel: AccountColors = AccountColors.Camel,
    val red: AccountColors = AccountColors.Red,
    val green: AccountColors = AccountColors.Green
) {

    private fun asList(): List<AccountColors> {
        return listOf(
            default,
            pink,
            blue,
            camel,
            red,
            green
        )
    }

    fun getByName(name: String): AccountColorWithName {
        return when (name) {
            AccountColorName.Default.name -> AccountColorWithName(default.name, default.color, default.colorOn)
            AccountColorName.Pink.name -> AccountColorWithName(pink.name, pink.color, pink.colorOn)
            AccountColorName.Blue.name -> AccountColorWithName(blue.name, blue.color, blue.colorOn)
            AccountColorName.Camel.name -> AccountColorWithName(camel.name, camel.color, camel.colorOn)
            AccountColorName.Red.name -> AccountColorWithName(red.name, red.color, red.colorOn)
            AccountColorName.Green.name -> AccountColorWithName(green.name, green.color, green.colorOn)
            else -> AccountColorWithName(default.name, default.color, default.colorOn)
        }
    }

    fun asColorWithNameList(theme: AppTheme): List<ColorWithName> {
        return asList().map { it.toColorWithName(theme) }
    }

}
