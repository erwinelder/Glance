package com.ataglance.walletglance.core.domain.color

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
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

    fun getCategoryIconSolidColorByTheme(appTheme: AppTheme?): Color {
        val lighterDarkerColors = getByTheme(appTheme)
        return when (appTheme) {
            AppTheme.DarkDefault -> lighterDarkerColors.lighter
            else -> lighterDarkerColors.darker
        }
    }

    fun getCategoryLineChartColorsByTheme(appTheme: AppTheme?): List<Color> {
        val lighterDarkerColors = getByTheme(appTheme)
        return when (appTheme) {
            AppTheme.DarkDefault -> lighterDarkerColors.asListLightToDark()
            else -> lighterDarkerColors.asListDarkToLight()
        }
    }

}
