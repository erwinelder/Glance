package com.ataglance.walletglance.data.color

import com.ataglance.walletglance.data.app.AppTheme

data class LighterDarkerColorsByTheme(
    val lightDefault: LighterDarkerColors = LighterDarkerColors(),
    val darkDefault: LighterDarkerColors = LighterDarkerColors()
) {

    fun getByTheme(theme: AppTheme?): LighterDarkerColors {
        return when (theme) {
            AppTheme.DarkDefault -> darkDefault
            else -> lightDefault
        }
    }

}
