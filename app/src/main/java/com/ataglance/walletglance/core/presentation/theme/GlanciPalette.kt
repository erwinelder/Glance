package com.ataglance.walletglance.core.presentation.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SecondaryButton
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SmallSecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.other.LargePrimaryIcon
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer

sealed class GlanciPalette(
    val primary: Color,
    val primaryGradient: List<Color>,
    val primaryGlassGradient: List<Color>,
    val primaryGlassGradientPair: Pair<Color, Color> = Pair(
        primaryGlassGradient[0], primaryGlassGradient[1]
    ),
    val primaryGlassBorderGradient: List<Color>,
    val primarySemiTransparentGlassBorderGradient: List<Color>,
    val onPrimary: Color,

    val disabledGlassGradient: List<Color>,
    val disabledGlassGradientPair: Pair<Color, Color> = Pair(
        disabledGlassGradient[0], disabledGlassGradient[1]
    ),
    val disabledSemiTransparentGlassBorderGradient: List<Color>,

    val glassGradient: List<Color>,
    val glassGradientPair: Pair<Color, Color> = Pair(glassGradient[0], glassGradient[1]),
    val glassBorderGradient: List<Color>,
    val glassShadow: Pair<Color, Color>,

    val glassGradientOnGlass: List<Color>,
    val glassGradientOnGlassBorder: Color,

    val glassButtonGradient: List<Color>,
    val glassButtonGradientPair: Pair<Color, Color> = Pair(
        glassButtonGradient[0], glassButtonGradient[1]
    ),

    val animatedIconGradient: List<Color>,

    val background: Color,
    val surface: Color,
    val surfaceGradient: List<Color>,
    val onSurface: Color,

    val outline: Color,
    val outlineSemiTransparent: Color,

    val success: Color,
    val successGradient: List<Color>,
    val error: Color,
    val errorGradient: List<Color>,
    val errorGradientPair: Pair<Color, Color> = Pair(errorGradient[0], errorGradient[1]),

    val lineChartGreenGradient: List<Color>,
    val lineChartGreenGradientPair: Pair<Color, Color> = Pair(
        lineChartGreenGradient[0], lineChartGreenGradient[1]
    ),
    val lineChartRedGradient: List<Color>,
    val lineChartRedGradientPair: Pair<Color, Color> = Pair(
        lineChartRedGradient[0], lineChartRedGradient[1]
    ),

    val pieChartGreenGradient: List<Color>,
    val pieChartYellowGradient: List<Color>,
    val pieChartRedGradient: List<Color>,

    val accountSemiTransparentBackground: Color,

    val layeredIconBaseColor: Color,
    val layeredIconPlatformColor: Color
) {

    data object LightDefault : GlanciPalette(
        primary = Color(165, 93, 135),
        primaryGradient = listOf(
            Color(192, 97, 146, 204),
            Color(164, 77, 131, 204)
        ),
        primaryGlassGradient = listOf(
            Color(181, 100, 142, 204),
            Color(153, 83, 126, 204)
        ),
        primaryGlassBorderGradient = listOf(
            Color(255, 255, 255, 77),
            Color(255, 255, 255, 64)
        ),
        primarySemiTransparentGlassBorderGradient = listOf(
            Color(191, 98, 146, 112),
            Color(163, 77, 130, 79)
        ),
        onPrimary = Color(255, 255, 255),

        disabledGlassGradient = listOf(
            Color(161, 161, 161, 204),
            Color(138, 138, 138, 204)
        ),
        disabledSemiTransparentGlassBorderGradient = listOf(
            Color(163, 163, 163, 204),
            Color(179, 179, 179, 204)
        ),

        glassGradient = listOf(
            Color(252, 252, 252, 128),
            Color(240, 240, 240, 128)
        ),
        glassBorderGradient = listOf(
            Color(255, 255, 255, 140),
            Color(240, 240, 240, 140)
        ),
        glassShadow = Pair(
//            Color(255, 255, 255, 138),
//            Color(0, 0, 0, 43)
            Color(0, 0, 0, 0),
            Color(0, 0, 0, 0)
        ),

        glassGradientOnGlass = listOf(
            Color(247, 247, 247, 120),
            Color(255, 255, 255, 120)
        ),
        glassGradientOnGlassBorder = Color(255, 255, 255, 90),

        glassButtonGradient = listOf(
            Color(255, 255, 255, 90),
            Color(233, 233, 233, 90)
        ),

        animatedIconGradient = listOf(
            Color(181, 100, 142, 204),
            Color(219, 99, 161, 38)
        ),

        background = Color(240, 240, 240),
        surface = Color(247, 247, 247),
        surfaceGradient = listOf(
            Color(232, 232, 232, 255),
            Color(239, 239, 239, 255)
        ),
        onSurface = Color(31, 26, 28),

        outline = Color(140, 133, 137),
        outlineSemiTransparent = Color(180, 171, 175, 128),

        success = Color(92, 180, 85),
        successGradient = listOf(
            Color(92, 180, 85),
            Color(75, 148, 70)
        ),
        error = Color(211, 92, 92),
        errorGradient = listOf(
            Color(211, 92, 92),
            Color(171, 67, 67)
        ),

        lineChartGreenGradient = listOf(
            Color(173, 207, 153),
            Color(151, 192, 127)
        ),
        lineChartRedGradient = listOf(
            Color(203, 139, 137),
            Color(201, 104, 98)
        ),

        pieChartGreenGradient = listOf(
            Color(173, 207, 153),
            Color(151, 192, 127)
        ),
        pieChartYellowGradient = listOf(
            Color(211, 185, 131, 255),
            Color(209, 179, 104, 255)
        ),
        pieChartRedGradient = listOf(
            Color(204, 127, 124, 255),
            Color(202, 102, 96, 255)
        ),

        accountSemiTransparentBackground = Color(31, 26, 28, 51),

        layeredIconBaseColor = Color(0, 0, 0),
        layeredIconPlatformColor = Color(230, 230, 230),
    )

    data object DarkDefault : GlanciPalette(
        primary = Color(154, 92, 128),
        primaryGradient = listOf(
            Color(199, 113, 160, 204),
            Color(183, 108, 139, 204)
        ),
        primaryGlassGradient = listOf(
            Color(194, 114, 155, 204),
            Color(166, 91, 137, 204)
        ),
        primaryGlassBorderGradient = listOf(
            Color(255, 255, 255, 31),
            Color(255, 255, 255, 15)
        ),
        primarySemiTransparentGlassBorderGradient = listOf(
            Color(212, 114, 168, 51),
            Color(168, 89, 126, 38)
        ),
        onPrimary = Color(231, 212, 225),

        disabledGlassGradient = listOf(
            Color(89, 89, 89, 204),
            Color(77, 77, 77, 204)
        ),
        disabledSemiTransparentGlassBorderGradient = listOf(
            Color(56, 56, 56, 204),
            Color(46, 46, 46, 204)
        ),

        glassGradient = listOf(
            Color(31, 31, 31, 128),
            Color(23, 23, 23, 128)
        ),
        glassBorderGradient = listOf(
            Color(217, 217, 217, 20),
            Color(102, 102, 102, 20)
        ),
        glassShadow = Pair(
//            Color(255, 255, 255, 41),
//            Color(0, 0, 0, 46)
            Color(0, 0, 0, 0),
            Color(0, 0, 0, 0)
        ),

        glassGradientOnGlass = listOf(
            Color(31, 31, 31, 120),
            Color(36, 36, 36, 120)
        ),
        glassGradientOnGlassBorder = Color(44, 44, 44, 128),

        glassButtonGradient = listOf(
            Color(197, 197, 197, 10),
            Color(63, 63, 63, 10)
        ),

        animatedIconGradient = listOf(
            Color(194, 114, 155, 204),
            Color(232, 114, 175, 77)
        ),

        background = Color(18, 18, 18),
        surface = Color(26, 26, 26, 255),
        surfaceGradient = listOf(
            Color(22, 22, 22, 255),
            Color(29, 29, 29, 255)
        ),
        onSurface = Color(217, 208, 210, 255),

        outline = Color(122, 115, 119),
        outlineSemiTransparent = Color(122, 115, 119, 128),

        success = Color(82, 161, 76),
        successGradient = listOf(
            Color(82, 161, 76),
            Color(72, 141, 66)
        ),
        error = Color(169, 66, 66),
        errorGradient = listOf(
            Color(169, 66, 66),
            Color(150, 52, 52)
        ),

        lineChartGreenGradient = listOf(
            Color(78, 107, 58, 255),
            Color(107, 163, 72, 255)
        ),
        lineChartRedGradient = listOf(
            Color(107, 44, 44, 255),
            Color(158, 56, 51, 255)
        ),

        pieChartGreenGradient = listOf(
            Color(108, 156, 78, 255),
            Color(83, 115, 62, 255)
        ),
        pieChartYellowGradient = listOf(
            Color(181, 158, 109, 255),
            Color(186, 158, 87, 255)
        ),
        pieChartRedGradient = listOf(
            Color(171, 95, 92, 255),
            Color(176, 84, 79, 255)
        ),

        accountSemiTransparentBackground = Color(226, 217, 220, 51),

        layeredIconBaseColor = Color(255, 255, 255),
        layeredIconPlatformColor = Color(12, 12, 12),
    )

}



@Preview(device = Devices.PIXEL_7_PRO, group = "Light Default")
@Composable
private fun PreviewColorsLightDefault() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            GlassSurface {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                ) {
                    LargePrimaryIcon(
                        iconRes = R.drawable.error_large_icon,
                        gradientColor = GlanciColors.errorGradient,
                        iconDescription = ""
                    )
                    Text(
                        text = "Outlined text",
                        color = GlanciColors.outline,
                        fontFamily = Manrope
                    )
                }
            }
            PrimaryButton(text = "Save and continue") {}
            PrimaryButton(text = "Save and continue", enabled = false) {}
            SmallPrimaryButton(text = "Apply") { }
            SecondaryButton(text = "Save and continue") { }
            SecondaryButton(text = "Save and continue", enabled = false) { }
            SmallSecondaryButton(text = "Apply") { }
        }
    }
}

@Preview(device = Devices.PIXEL_7_PRO, group = "Dark Default")
@Composable
private fun PreviewColorsDarkDefault() {
    PreviewContainer(appTheme = AppTheme.DarkDefault) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            GlassSurface {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                ) {
                    LargePrimaryIcon(
                        iconRes = R.drawable.error_large_icon,
                        gradientColor = GlanciColors.errorGradient,
                        iconDescription = ""
                    )
                    Text(
                        text = "Outlined text",
                        color = GlanciColors.outline,
                        fontFamily = Manrope
                    )
                }
            }
            PrimaryButton(text = "Save and continue") {}
            PrimaryButton(text = "Save and continue", enabled = false) {}
            SmallPrimaryButton(text = "Apply") { }
            SecondaryButton(text = "Save and continue") { }
            SecondaryButton(text = "Save and continue", enabled = false) { }
            SmallSecondaryButton(text = "Apply") { }
        }
    }
}