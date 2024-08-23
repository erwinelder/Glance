package com.ataglance.walletglance.core.domain.color

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class LighterDarkerColors(
    val lighter: Color = Color.Gray,
    val darker: Color = Color.Gray
) {

    fun asListDarkToLight(): List<Color> {
        return listOf(darker, lighter)
    }

}
