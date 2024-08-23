package com.ataglance.walletglance.core.domain.color

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.core.domain.app.AppTheme

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
