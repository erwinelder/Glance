package com.ataglance.walletglance.data.color

import androidx.compose.ui.graphics.Color

data class LighterDarkerColors(
    val lighter: Color = Color.Gray,
    val darker: Color = Color.Gray
) {

    fun asListDarkToLight(): List<Color> {
        return listOf(darker, lighter)
    }

}
