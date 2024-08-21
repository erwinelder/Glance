package com.ataglance.walletglance.data.color

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.data.app.AppTheme

@Stable
data class ColorByTheme(
    val lightDefault: Color,
    val darkDefault: Color
) {

    fun getByTheme(theme: AppTheme?): Color {
        return when (theme) {
            AppTheme.DarkDefault -> darkDefault
            else -> lightDefault
        }
    }

}
