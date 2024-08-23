package com.ataglance.walletglance.account.domain.color

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.color.ColorByTheme
import com.ataglance.walletglance.core.domain.color.LighterDarkerColors
import com.ataglance.walletglance.core.domain.color.LighterDarkerColorsByTheme

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
