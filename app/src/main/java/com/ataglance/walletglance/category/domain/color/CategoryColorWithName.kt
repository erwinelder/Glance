package com.ataglance.walletglance.category.domain.color

import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.color.LighterDarkerColors
import com.ataglance.walletglance.core.domain.color.LighterDarkerColorsByTheme

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
