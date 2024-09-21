package com.ataglance.walletglance.core.domain.color

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.core.domain.app.AppTheme

@Stable
data class LighterDarkerColorsByTheme(
    val lightDefault: LighterDarkerColors = LighterDarkerColors(),
    val darkDefault: LighterDarkerColors = LighterDarkerColors()
) {

    fun getByTheme(theme: AppTheme): LighterDarkerColors {
        return when (theme) {
            AppTheme.LightDefault -> lightDefault
            AppTheme.DarkDefault -> darkDefault
        }
    }

    fun getCategoryIconSolidColorByTheme(appTheme: AppTheme): Color {
        val lighterDarkerColors = getByTheme(appTheme)
        return when (appTheme) {
            AppTheme.LightDefault -> lighterDarkerColors.darker
            AppTheme.DarkDefault -> lighterDarkerColors.lighter
        }
    }

    fun getCategoryLineChartColorsByTheme(appTheme: AppTheme): List<Color> {
        val lighterDarkerColors = getByTheme(appTheme)
        return when (appTheme) {
            AppTheme.LightDefault -> lighterDarkerColors.asListDarkToLight()
            AppTheme.DarkDefault -> lighterDarkerColors.asListLightToDark()
        }
    }

}
