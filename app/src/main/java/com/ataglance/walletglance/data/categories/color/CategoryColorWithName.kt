package com.ataglance.walletglance.data.categories.color

import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.color.LighterDarkerColors
import com.ataglance.walletglance.data.color.LighterDarkerColorsByTheme

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
