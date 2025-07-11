package com.ataglance.walletglance.core.domain.app

import androidx.annotation.DrawableRes

data class DrawableResByTheme(
    @DrawableRes val lightDefault: Int,
    @DrawableRes val darkDefault: Int
) {

    fun get(theme: AppTheme?): Int {
        return when (theme) {
            AppTheme.DarkDefault -> darkDefault
            else -> lightDefault
        }
    }

}