package com.ataglance.walletglance.model

import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.theme.LighterDarkerColors

enum class CategoryColorName {
    Olive, Camel, Pink, Green, Red, LightBlue, Lavender, Blue, Aquamarine, Orange, Yellow,
    GrayDefault // DO NOT CHANGE, CONNECTED TO THE DATABASE CATEGORY TABLE DEFAULT COLOR VALUE
}

data class CategoryColor(
    val olive: LighterDarkerColors,
    val camel: LighterDarkerColors,
    val pink: LighterDarkerColors,
    val green: LighterDarkerColors,
    val red: LighterDarkerColors,
    val lightBlue: LighterDarkerColors,
    val lavender: LighterDarkerColors,
    val blue: LighterDarkerColors,
    val aquamarine: LighterDarkerColors,
    val orange: LighterDarkerColors,
    val yellow: LighterDarkerColors,
    val grayDefault: LighterDarkerColors
)

data class CategoryColorByTheme(
    val light: CategoryColor = CategoryColor(
        olive = LighterDarkerColors(
            Color(185, 207, 145), Color(161, 180, 126)
        ),
        camel = LighterDarkerColors(
            Color(227, 188, 129), Color(202, 167, 115)
        ),
        pink = LighterDarkerColors(
            Color(255, 145, 204), Color(229, 130, 183)
        ),
        green = LighterDarkerColors(
            Color(148, 224, 154), Color(131, 199, 137)
        ),
        red = LighterDarkerColors(
            Color(255, 131, 131), Color(229, 118, 118)
        ),
        lightBlue = LighterDarkerColors(
            Color(133, 186, 248), Color(121, 169, 225)
        ),
        lavender = LighterDarkerColors(
            Color(211, 148, 250), Color(190, 133, 225)
        ),
        blue = LighterDarkerColors(
            Color(141, 145, 242), Color(126, 129, 216)
        ),
        aquamarine = LighterDarkerColors(
            Color(135, 240, 201), Color(120, 213, 179)
        ),
        orange = LighterDarkerColors(
            Color(255, 170, 121), Color(229, 153, 109)
        ),
        yellow = LighterDarkerColors(
            Color(242, 226, 84), Color(217, 202, 75)
        ),
        grayDefault = LighterDarkerColors(
            Color(181, 181, 181), Color(155, 155, 155)
        )
    ),
    val dark: CategoryColor = CategoryColor(
        olive = LighterDarkerColors(
            Color(116, 130, 91), Color(139, 156, 109)
        ),
        camel = LighterDarkerColors(
            Color(150, 125, 86), Color(176, 146, 100)
        ),
        pink = LighterDarkerColors(
            Color(179, 102, 143), Color(204, 116, 163)
        ),
        green = LighterDarkerColors(
            Color(98, 148, 102), Color(114, 173, 119)
        ),
        red = LighterDarkerColors(
            Color(179, 93, 93), Color(204, 104, 104)
        ),
        lightBlue = LighterDarkerColors(
            Color(94, 131, 173), Color(107, 150, 199)
        ),
        lavender = LighterDarkerColors(
            Color(146, 102, 173), Color(168, 117, 199)
        ),
        blue = LighterDarkerColors(
            Color(96, 98, 166), Color(111, 114, 191)
        ),
        aquamarine = LighterDarkerColors(
            Color(91, 163, 137), Color(106, 189, 158)
        ),
        orange = LighterDarkerColors(
            Color(179, 120, 86), Color(204, 136, 96)
        ),
        yellow = LighterDarkerColors(
            Color(166, 155, 58), Color(191, 179, 67)
        ),
        grayDefault = LighterDarkerColors(
            Color(105, 105, 105), Color(130, 130, 130)
        )
    )
)

sealed class CategoryColors(val color: Colors) {
    data class Olive(val appTheme: AppTheme?) : CategoryColors(
        Colors(
            CategoryColorName.Olive.name,
            when(appTheme) {
                AppTheme.DarkDefault -> CategoryColorByTheme().dark.olive
                else -> CategoryColorByTheme().light.olive
            }
        )
    )
    data class Camel(val appTheme: AppTheme?) : CategoryColors(
        Colors(
            CategoryColorName.Camel.name,
            when(appTheme) {
                AppTheme.DarkDefault -> CategoryColorByTheme().dark.camel
                else -> CategoryColorByTheme().light.camel
            }
        )
    )
    data class Pink(val appTheme: AppTheme?) : CategoryColors(
        Colors(
            CategoryColorName.Pink.name,
            when(appTheme) {
                AppTheme.DarkDefault -> CategoryColorByTheme().dark.pink
                else -> CategoryColorByTheme().light.pink
            }
        )
    )
    data class Green(val appTheme: AppTheme?) : CategoryColors(
        Colors(
            CategoryColorName.Green.name,
            when(appTheme) {
                AppTheme.DarkDefault -> CategoryColorByTheme().dark.green
                else -> CategoryColorByTheme().light.green
            }
        )
    )
    data class Red(val appTheme: AppTheme?) : CategoryColors(
        Colors(
            CategoryColorName.Red.name,
            when(appTheme) {
                AppTheme.DarkDefault -> CategoryColorByTheme().dark.red
                else -> CategoryColorByTheme().light.red
            }
        )
    )
    data class LightBlue(val appTheme: AppTheme?) : CategoryColors(
        Colors(
            CategoryColorName.LightBlue.name,
            when(appTheme) {
                AppTheme.DarkDefault -> CategoryColorByTheme().dark.lightBlue
                else -> CategoryColorByTheme().light.lightBlue
            }
        )
    )
    data class Lavender(val appTheme: AppTheme?) : CategoryColors(
        Colors(
            CategoryColorName.Lavender.name,
            when(appTheme) {
                AppTheme.DarkDefault -> CategoryColorByTheme().dark.lavender
                else -> CategoryColorByTheme().light.lavender
            }
        )
    )
    data class Blue(val appTheme: AppTheme?) : CategoryColors(
        Colors(
            CategoryColorName.Blue.name,
            when(appTheme) {
                AppTheme.DarkDefault -> CategoryColorByTheme().dark.blue
                else -> CategoryColorByTheme().light.blue
            }
        )
    )
    data class Aquamarine(val appTheme: AppTheme?) : CategoryColors(
        Colors(
            CategoryColorName.Aquamarine.name,
            when(appTheme) {
                AppTheme.DarkDefault -> CategoryColorByTheme().dark.aquamarine
                else -> CategoryColorByTheme().light.aquamarine
            }
        )
    )
    data class Orange(val appTheme: AppTheme?) : CategoryColors(
        Colors(
            CategoryColorName.Orange.name,
            when(appTheme) {
                AppTheme.DarkDefault -> CategoryColorByTheme().dark.orange
                else -> CategoryColorByTheme().light.orange
            }
        )
    )
    data class Yellow(val appTheme: AppTheme?) : CategoryColors(
        Colors(
            CategoryColorName.Yellow.name,
            when(appTheme) {
                AppTheme.DarkDefault -> CategoryColorByTheme().dark.yellow
                else -> CategoryColorByTheme().light.yellow
            }
        )
    )
    data class GrayDefault(val appTheme: AppTheme?) : CategoryColors(
        Colors(
            CategoryColorName.GrayDefault.name,
            when(appTheme) {
                AppTheme.DarkDefault -> CategoryColorByTheme().dark.grayDefault
                else -> CategoryColorByTheme().light.grayDefault
            }
        )
    )
}