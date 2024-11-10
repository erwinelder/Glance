package com.ataglance.walletglance.core.presentation

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.charts.GlanceSingleValuePieChart
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.presentation.components.containers.PreviewContainer

val md_theme_light_default_primary = Color(165, 93, 135, 255)
val md_theme_light_default_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_default_primaryContainer = Color(182, 103, 149, 255)
val md_theme_light_default_onPrimaryContainer = Color(0xFF3C002A)
val md_theme_light_default_secondary = Color(0xFF715764)
val md_theme_light_default_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_default_secondaryContainer = Color(0xFFFCD9E9)
val md_theme_light_default_onSecondaryContainer = Color(0xFF291520)
val md_theme_light_default_tertiary = Color(0xFF80543C)
val md_theme_light_default_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_default_tertiaryContainer = Color(0xFFFFDBCA)
val md_theme_light_default_onTertiaryContainer = Color(0xFF311302)
val md_theme_light_default_success = Color(59, 162, 51)
val md_theme_light_default_error = Color(0xFFCE3535)
val md_theme_light_default_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_default_onError = Color(0xFFFFFFFF)
val md_theme_light_default_onErrorContainer = Color(0xFF410002)
val md_theme_light_default_background = Color(0xFFF0F0F0)
val md_theme_light_default_onBackground = Color(0xFF1F1A1C)
val md_theme_light_default_surface = Color(0xFFF7F7F7)
val md_theme_light_default_onSurface = Color(0xFF1F1A1C)
val md_theme_light_default_surfaceVariant = Color(255, 255, 255, 128)
val md_theme_light_default_inverseSurface = Color(233, 233, 233, 128)
val md_theme_light_default_onSurfaceVariant = Color(255, 255, 255, 128)
val md_theme_light_default_outline = Color(0xFFB4ABAF)
val md_theme_light_default_inverseOnSurface = Color(0xFFEDEDED)
val md_theme_light_default_inversePrimary = Color(0xFFFFAEDA)
val md_theme_light_default_surfaceTint = Color(0xFF934173)
val md_theme_light_default_outlineVariant = Color(255, 255, 255, 79)
val md_theme_light_default_scrim = Color(0xFF000000)
val md_theme_light_default_primaryGradientLightToDark = Pair(
    Color(182, 103, 149),
    Color(109, 63, 90)
)
val md_theme_light_default_glassSurfaceGradient = listOf(
    Color(242, 242, 242, 128),
    Color(252, 252, 252, 128)
)
val md_theme_light_default_glassSurfaceBorder = Color(255, 255, 255, 69)
val md_theme_light_default_glassSurfaceLightAndDarkShadow = Pair(
    Color(255, 255, 255, 120),
    Color(201, 201, 201, 120)
)
val md_theme_light_default_onGlassSurfaceGradient = listOf(
    Color(245, 245, 245, 120),
    Color(255, 255, 255, 120)
)
val md_theme_light_default_onGlassSurfaceBorder = Color(255, 255, 255, 90)
val md_theme_light_default_glassGradientLightToDark = Pair(
    Color(255, 255, 255, 90),
    Color(233, 233, 233, 90)
)
val md_theme_light_default_surfaceGradient = listOf(
    Color(232, 232, 232, 255),
    Color(239, 239, 239, 255)
)
val md_theme_light_default_disabledGradientLightToDark = Pair(
    Color(180, 171, 175),
    Color(107, 102, 104)
)
val md_theme_light_default_errorGradientLightToDark = Pair(
    Color(206, 53, 53),
    Color(119, 36, 36)
)

val md_theme_light_default_pale_green = Pair(
    Color(173, 207, 153),
    Color(151, 192, 127)
)
val md_theme_light_default_pale_red = Pair(
    Color(203, 139, 137),
    Color(201, 104, 98)
)

val md_theme_light_default_green = Pair(
    Color(173, 207, 153),
    Color(151, 192, 127)
)
val md_theme_light_default_yellow = Pair(
    Color(211, 185, 131, 255),
    Color(209, 179, 104, 255)
)
val md_theme_light_default_red = Pair(
    Color(204, 127, 124, 255),
    Color(202, 102, 96, 255)
)

val md_theme_dark_default_primary = Color(154, 92, 128)
val md_theme_dark_default_onPrimary = Color(0xFFE7D4E1)
val md_theme_dark_default_primaryContainer = Color(0xFFB15E8B)
val md_theme_dark_default_onPrimaryContainer = Color(0xFFFFD8EA)
val md_theme_dark_default_secondary = Color(0xFFDFBECD)
val md_theme_dark_default_onSecondary = Color(0xFF402A36)
val md_theme_dark_default_secondaryContainer = Color(0xFF58404C)
val md_theme_dark_default_onSecondaryContainer = Color(0xFFFCD9E9)
val md_theme_dark_default_tertiary = Color(0xFFF3BA9C)
val md_theme_dark_default_onTertiary = Color(0xFF4A2713)
val md_theme_dark_default_tertiaryContainer = Color(0xFF653D27)
val md_theme_dark_default_onTertiaryContainer = Color(0xFFFFDBCA)
val md_theme_dark_default_success = Color(66, 155, 59)
val md_theme_dark_default_error = Color(0xFFB83B3B)
val md_theme_dark_default_errorContainer = Color(0xFF93000A)
val md_theme_dark_default_onError = Color(0xFF690005)
val md_theme_dark_default_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_default_background = Color(0xFF121212)
val md_theme_dark_default_onBackground = Color(0xFFEAE0E3)
val md_theme_dark_default_surface = Color(33, 33, 33, 255)
val md_theme_dark_default_onSurface = Color(0xFFE2D9DC)
val md_theme_dark_default_surfaceVariant = Color(34, 34, 34, 128)
val md_theme_dark_default_inverseSurface = Color(24, 24, 24, 128)
val md_theme_dark_default_onSurfaceVariant = Color(70, 70, 70, 128)
val md_theme_dark_default_outline = Color(0xFF7A7377)
val md_theme_dark_default_inverseOnSurface = Color(0xFF1D1D1D)
val md_theme_dark_default_inversePrimary = Color(0xFF934173)
val md_theme_dark_default_surfaceTint = Color(0xFFFFAEDA)
val md_theme_dark_default_outlineVariant = Color(37, 37, 37, 128)
val md_theme_dark_default_scrim = Color(0xFF000000)
val md_theme_dark_default_primaryGradientLightToDark = Pair(
    Color(168, 90, 133),
    Color(88, 47, 70)
)
val md_theme_dark_default_glassSurfaceGradient = listOf(
    Color(23, 23, 23, 128),
    Color(31, 31, 31, 128)
)
val md_theme_dark_default_glassSurfaceBorder = Color(35, 35, 35, 128)
val md_theme_dark_default_glassSurfaceLightAndDarkShadow = Pair(
    Color(39, 39, 39, 120),
    Color(19, 19, 19, 120)
)
val md_theme_dark_default_onGlassSurfaceGradient = listOf(
    Color(31, 31, 31, 120),
    Color(36, 36, 36, 120)
)
val md_theme_dark_default_onGlassSurfaceBorder = Color(44, 44, 44, 128)
val md_theme_dark_default_glassGradientLightToDark = Pair(
    Color(197, 197, 197, 10),
    Color(63, 63, 63, 10)
)
val md_theme_dark_default_surfaceGradient = listOf(
    Color(22, 22, 22, 255),
    Color(29, 29, 29, 255)
)
val md_theme_dark_default_disabledGradientLightToDark = Pair(
    Color(122, 115, 119),
    Color(51, 48, 50)
)
val md_theme_dark_default_errorGradientLightToDark = Pair(
    Color(177, 55, 55),
    Color(104, 33, 33)
)

val md_theme_dark_default_pale_green = Pair(
    Color(78, 107, 58, 255),
    Color(107, 163, 72, 255)
)
val md_theme_dark_default_pale_red = Pair(
    Color(107, 44, 44, 255),
    Color(158, 56, 51, 255)
)

val md_theme_dark_default_green = Pair(
    Color(108, 156, 78, 255),
    Color(83, 115, 62, 255)
)
val md_theme_dark_default_yellow = Pair(
    Color(181, 158, 109, 255),
    Color(186, 158, 87, 255)
)
val md_theme_dark_default_red = Pair(
    Color(171, 95, 92, 255),
    Color(176, 84, 79, 255)
)

val md_theme_dark_blue_primary = Color(0xFFB15E8B)
val md_theme_dark_blue_onPrimary = Color(0xFFE7D4E1)
val md_theme_dark_blue_primaryContainer = Color(0xFF762A5B)
val md_theme_dark_blue_onPrimaryContainer = Color(0xFFFFD8EA)
val md_theme_dark_blue_secondary = Color(0xFFDFBECD)
val md_theme_dark_blue_onSecondary = Color(0xFF402A36)
val md_theme_dark_blue_secondaryContainer = Color(0xFF58404C)
val md_theme_dark_blue_onSecondaryContainer = Color(0xFFFCD9E9)
val md_theme_dark_blue_tertiary = Color(0xFFF3BA9C)
val md_theme_dark_blue_onTertiary = Color(0xFF4A2713)
val md_theme_dark_blue_tertiaryContainer = Color(0xFF653D27)
val md_theme_dark_blue_onTertiaryContainer = Color(0xFFFFDBCA)
val md_theme_dark_blue_success = Color(66, 155, 59)
val md_theme_dark_blue_error = Color(0xFFFFB4AB)
val md_theme_dark_blue_errorContainer = Color(0xFF93000A)
val md_theme_dark_blue_onError = Color(0xFF690005)
val md_theme_dark_blue_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_blue_background = Color(0xFF1F1A1C)
val md_theme_dark_blue_onBackground = Color(0xFFEAE0E3)
val md_theme_dark_blue_surface = Color(0xFF1F1A1C)
val md_theme_dark_blue_onSurface = Color(0xFFEAE0E3)
val md_theme_dark_blue_surfaceVariant = Color(0xFF504349)
val md_theme_dark_blue_inverseSurface = Color(0xFFEAE0E3)
val md_theme_dark_blue_onSurfaceVariant = Color(0xFFD3C2C9)
val md_theme_dark_blue_outline = Color(0xFF9C8D93)
val md_theme_dark_blue_inverseOnSurface = Color(0xFF1F1A1C)
val md_theme_dark_blue_inversePrimary = Color(0xFF934173)
val md_theme_dark_blue_surfaceTint = Color(0xFFFFAEDA)
val md_theme_dark_blue_outlineVariant = Color(0xFF504349)
val md_theme_dark_blue_scrim = Color(0xFF000000)
val md_theme_dark_blue_primaryGradientLightToDark = Pair(
    Color(177, 94, 139),
    Color(109, 57, 85)
)
val md_theme_dark_blue_glassSurfaceGradient = listOf(
    Color(24, 24, 24, 128),
    Color(34, 34, 34, 128)
)
val md_theme_dark_blue_glassSurfaceBorder = Color(37, 37, 37, 128)
val md_theme_dark_blue_glassSurfaceLightAndDarkShadow = Pair(
    Color(255, 255, 255, 128),
    Color(0, 0, 0, 128)
)
val md_theme_dark_blue_onGlassSurfaceGradient = listOf(
    Color(39, 39, 39, 120),
    Color(19, 19, 19, 120)
)
val md_theme_dark_blue_onGlassSurfaceBorder = Color(255, 255, 255, 100)
val md_theme_dark_blue_glassGradientLightToDark = Pair(
    Color(255, 255, 255, 120),
    Color(201, 201, 201, 120)
)
val md_theme_dark_blue_surfaceGradient = listOf(
    Color(22, 22, 22, 255),
    Color(29, 29, 29, 255)
)
val md_theme_dark_blue_disabledGradientLightToDark = Pair(
    Color(122, 115, 119),
    Color(122, 115, 119)
)
val md_theme_dark_blue_errorGradientLightToDark = Pair(
    Color(184, 59, 59),
    Color(136, 44, 44)
)

val md_theme_dark_blue_pale_green = Pair(
    Color(78, 107, 58, 255),
    Color(107, 163, 72, 255)
)
val md_theme_dark_blue_pale_red = Pair(
    Color(107, 44, 44, 255),
    Color(158, 56, 51, 255)
)

val md_theme_dark_blue_green = Pair(
    Color(108, 156, 78, 255),
    Color(83, 115, 62, 255)
)
val md_theme_dark_blue_yellow = Pair(
    Color(181, 158, 109, 255),
    Color(186, 158, 87, 255)
)
val md_theme_dark_blue_red = Pair(
    Color(171, 95, 92, 255),
    Color(176, 84, 79, 255)
)


@Preview
@Composable
private fun PreviewColors() {
    PreviewContainer(appTheme = AppTheme.DarkDefault) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
//            BarButton(onClick = {}, text = "Apply", active = true)
//            PrimaryButton(onClick = {}, text = "ApplyApplyApply", enabledGradientColor = md_theme_dark_default_errorGradientLightToDark)
//            PrimaryButton(onClick = {}, text = "ApplyApplyApply")
//            PrimaryButton(onClick = {}, enabled = false, text = "ApplyApplyApply")
//            SmallPrimaryButton(onClick = {}, text = "ApplyApplyApply", enabledGradientColor = md_theme_dark_default_errorGradientLightToDark)
//            SmallPrimaryButton(onClick = {}, text = "ApplyApplyApply")
//            SmallPrimaryButton(onClick = {}, enabled = false, text = "ApplyApplyApply")
            GlassSurface {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                ) {
                    GlassSurfaceOnGlassSurface {
                        Box(
                            modifier = Modifier.size(150.dp, 100.dp)
                        )
                    }
                    GlanceSingleValuePieChart(
                        percentage = 50f * 3.6f,
                        brush = GlanceTheme.greenGradient.toList().reversed(),
                        size = 90.dp
                    )
                    GlanceSingleValuePieChart(
                        percentage = 95f * 3.6f,
                        brush = GlanceTheme.yellowGradient.toList().reversed(),
                        size = 90.dp
                    )
                    GlanceSingleValuePieChart(
                        percentage = 100f * 3.6f,
                        brush = GlanceTheme.redGradient.toList().reversed(),
                        size = 90.dp
                    )
                }
            }
        }
    }
}