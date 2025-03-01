package com.ataglance.walletglance.core.presentation.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.presentation.components.other.IconWithBackground
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer

sealed class GlancePalette(
    val primary: Color,
    val primaryGradient: List<Color>,
    val primaryGradientPair: Pair<Color, Color> = Pair(primaryGradient[0], primaryGradient[1]),
    val onPrimary: Color,

    val glassGradient: List<Color>,
    val glassBorder: Color,
    val glassSurfaceShadow: Pair<Color, Color>,

    val glassGradientOnGlass: List<Color>,
    val glassGradientOnGlassBorder: Color,

    val glassButtonGradient: List<Color>,
    val glassButtonGradientPair: Pair<Color, Color> = Pair(glassButtonGradient[0], glassButtonGradient[1]),

    val background: Color,
    val surface: Color,
    val surfaceGradient: List<Color>,
    val onSurface: Color,

    val outline: Color,
    val outlineSemiTransparent: Color,

    val disabledGradient: List<Color>,
    val disabledGradientPair: Pair<Color, Color> = Pair(disabledGradient[0], disabledGradient[1]),

    val success: Color,
    val successGradient: List<Color>,
    val error: Color,
    val errorGradient: List<Color>,
    val errorGradientPair: Pair<Color, Color> = Pair(errorGradient[0], errorGradient[1]),

    val lineChartGreenGradient: List<Color>,
    val lineChartGreenGradientPair: Pair<Color, Color> = Pair(lineChartGreenGradient[0], lineChartGreenGradient[1]),
    val lineChartRedGradient: List<Color>,
    val lineChartRedGradientPair: Pair<Color, Color> = Pair(lineChartRedGradient[0], lineChartRedGradient[1]),

    val pieChartGreenGradient: List<Color>,
    val pieChartYellowGradient: List<Color>,
    val pieChartRedGradient: List<Color>,

    val accountSemiTransparentBackground: Color,
) {

    data object LightDefault : GlancePalette(
        primary = Color(165, 93, 135),
        primaryGradient = listOf(
            Color(182, 103, 149),
            Color(130, 75, 108)
        ),
        onPrimary = Color(255, 255, 255),

        glassGradient = listOf(
            Color(242, 242, 242, 128),
            Color(252, 252, 252, 128)
        ),
        glassBorder = Color(255, 255, 255, 69),
        glassSurfaceShadow = Pair(
            Color(255, 255, 255, 120),
            Color(201, 201, 201, 120)
        ),

        glassGradientOnGlass = listOf(
            Color(245, 245, 245, 120),
            Color(255, 255, 255, 120)
        ),
        glassGradientOnGlassBorder = Color(255, 255, 255, 90),

        glassButtonGradient = listOf(
            Color(255, 255, 255, 90),
            Color(233, 233, 233, 90)
        ),

        background = Color(240, 240, 240),
        surface = Color(247, 247, 247),
        surfaceGradient = listOf(
            Color(232, 232, 232, 255),
            Color(239, 239, 239, 255)
        ),
        onSurface = Color(31, 26, 28),

        outline = Color(180, 171, 175),
        outlineSemiTransparent = Color(180, 171, 175, 128),

        disabledGradient = listOf(
            Color(180, 171, 175),
            Color(107, 102, 104)
        ),

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
    )

    data object DarkDefault : GlancePalette(
        primary = Color(154, 92, 128),
        primaryGradient = listOf(
            Color(168, 90, 133),
            Color(117, 62, 92, 255)
        ),
        onPrimary = Color(231, 212, 225),

        glassGradient = listOf(
            Color(23, 23, 23, 128),
            Color(31, 31, 31, 128)
        ),
        glassBorder = Color(35, 35, 35, 128),
        glassSurfaceShadow = Pair(
            Color(39, 39, 39, 120),
            Color(19, 19, 19, 120)
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

        background = Color(18, 18, 18),
        surface = Color(33, 33, 33, 255),
        surfaceGradient = listOf(
            Color(22, 22, 22, 255),
            Color(29, 29, 29, 255)
        ),
        onSurface = Color(226, 217, 220),

        outline = Color(122, 115, 119),
        outlineSemiTransparent = Color(122, 115, 119, 128),

        disabledGradient = listOf(
            Color(122, 115, 119),
            Color(51, 48, 50)
        ),

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
    )

}

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun PreviewColors() {
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
                    IconWithBackground(
                        iconRes = R.drawable.error_large_icon,
                        backgroundGradient = GlanceColors.errorGradient,
                        iconDescription = ""
                    )
                    GlassSurfaceOnGlassSurface {
                        Box(
                            modifier = Modifier.size(150.dp, 100.dp)
                        )
                    }
                }
            }
            PrimaryButton(text = "Save and continue")
        }
    }
}