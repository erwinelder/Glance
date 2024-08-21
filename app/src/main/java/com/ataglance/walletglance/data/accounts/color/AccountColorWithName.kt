package com.ataglance.walletglance.data.accounts.color

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.color.ColorByTheme
import com.ataglance.walletglance.data.color.LighterDarkerColors
import com.ataglance.walletglance.data.color.LighterDarkerColorsByTheme

@Stable
data class AccountColorWithName(
    val name: AccountColorName,
    val color: LighterDarkerColorsByTheme,
    val colorOn: ColorByTheme
) {

    fun getNameValue(): String {
        return name.name
    }

    fun getColorAndColorOnByTheme(theme: AppTheme?): Pair<LighterDarkerColors, Color> {
        return color.getByTheme(theme) to colorOn.getByTheme(theme)
    }

}
