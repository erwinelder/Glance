package com.ataglance.walletglance.data.categories.color

import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.data.color.LighterDarkerColors
import com.ataglance.walletglance.data.color.LighterDarkerColorsByTheme

sealed class CategoryColors(val name: CategoryColorName, val color: LighterDarkerColorsByTheme) {

    data object Olive : CategoryColors(
        CategoryColorName.Olive,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(185, 207, 145), Color(161, 180, 126)
            ),
            darkDefault = LighterDarkerColors(
                Color(116, 130, 91), Color(139, 156, 109)
            )
        )
    )

    data object Camel : CategoryColors(
        CategoryColorName.Camel,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(227, 188, 129), Color(202, 167, 115)
            ),
            darkDefault = LighterDarkerColors(
                Color(150, 125, 86), Color(176, 146, 100)
            )
        )
    )

    data object Pink : CategoryColors(
        CategoryColorName.Pink,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(255, 145, 204), Color(229, 130, 183)
            ),
            darkDefault = LighterDarkerColors(
                Color(179, 102, 143), Color(204, 116, 163)
            )
        )
    )

    data object Green : CategoryColors(
        CategoryColorName.Green,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(148, 224, 154), Color(131, 199, 137)
            ),
            darkDefault = LighterDarkerColors(
                Color(98, 148, 102), Color(114, 173, 119)
            )
        )
    )

    data object Red : CategoryColors(
        CategoryColorName.Red,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(255, 131, 131), Color(229, 118, 118)
            ),
            darkDefault = LighterDarkerColors(
                Color(179, 93, 93), Color(204, 104, 104)
            )
        )
    )

    data object LightBlue : CategoryColors(
        CategoryColorName.LightBlue,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(133, 186, 248), Color(121, 169, 225)
            ),
            darkDefault = LighterDarkerColors(
                Color(94, 131, 173), Color(107, 150, 199)
            )
        )
    )

    data object Lavender : CategoryColors(
        CategoryColorName.Lavender,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(211, 148, 250), Color(190, 133, 225)
            ),
            darkDefault = LighterDarkerColors(
                Color(146, 102, 173), Color(168, 117, 199)
            )
        )
    )

    data object Blue : CategoryColors(
        CategoryColorName.Blue,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(141, 145, 242), Color(126, 129, 216)
            ),
            darkDefault = LighterDarkerColors(
                Color(96, 98, 166), Color(111, 114, 191)
            )
        )
    )

    data object Aquamarine : CategoryColors(
        CategoryColorName.Aquamarine,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(135, 240, 201), Color(120, 213, 179)
            ),
            darkDefault = LighterDarkerColors(
                Color(91, 163, 137), Color(106, 189, 158)
            )
        )
    )

    data object Orange : CategoryColors(
        CategoryColorName.Orange,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(255, 170, 121), Color(229, 153, 109)
            ),
            darkDefault = LighterDarkerColors(
                Color(179, 120, 86), Color(204, 136, 96)
            )
        )
    )

    data object Yellow : CategoryColors(
        CategoryColorName.Yellow,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(242, 226, 84), Color(217, 202, 75)
            ),
            darkDefault = LighterDarkerColors(
                Color(166, 155, 58), Color(191, 179, 67)
            )
        )
    )

    data object GrayDefault : CategoryColors(
        CategoryColorName.GrayDefault,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(181, 181, 181), Color(155, 155, 155)
            ),
            darkDefault = LighterDarkerColors(
                Color(105, 105, 105), Color(130, 130, 130)
            )
        )
    )

}