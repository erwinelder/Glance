package com.ataglance.walletglance.domain.categories.color

import com.ataglance.walletglance.domain.app.AppTheme
import com.ataglance.walletglance.domain.color.LighterDarkerColors
import com.ataglance.walletglance.domain.color.LighterDarkerColorsByTheme

data class CategoryColorWithName(
    val name: CategoryColorName,
    val color: LighterDarkerColorsByTheme
) {

    fun getNameValue(): String {
        return name.name
    }

    fun getColorByTheme(theme: AppTheme?): LighterDarkerColors {
        return color.getByTheme(theme)
    }

}
