package com.ataglance.walletglance.data.color

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.data.app.AppTheme

@Stable
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
