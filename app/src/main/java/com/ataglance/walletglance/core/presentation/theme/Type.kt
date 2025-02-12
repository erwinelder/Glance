package com.ataglance.walletglance.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R

val Manrope = FontFamily(
    Font(R.font.manrope_extralight, weight = FontWeight.ExtraLight),
    Font(R.font.manrope_light, weight = FontWeight.Light),
    Font(R.font.manrope_regular, weight = FontWeight.Normal),
    Font(R.font.manrope_medium, weight = FontWeight.Medium),
    Font(R.font.manrope_semibold, weight = FontWeight.SemiBold),
    Font(R.font.manrope_bold, weight = FontWeight.Bold),
    Font(R.font.manrope_extrabold, weight = FontWeight.ExtraBold)
)

val NotoSans = FontFamily(
    Font(R.font.notosans_extralight, weight = FontWeight.ExtraLight),
    Font(R.font.notosans_light, weight = FontWeight.Light),
    Font(R.font.notosans_regular, weight = FontWeight.Normal),
    Font(R.font.notosans_medium, weight = FontWeight.Medium),
    Font(R.font.notosans_semibold, weight = FontWeight.SemiBold),
    Font(R.font.notosans_bold, weight = FontWeight.Bold),
    Font(R.font.notosans_extrabold, weight = FontWeight.ExtraBold),
    Font(R.font.notosans_black, weight = FontWeight.Black)
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = NotoSans,
        fontSize = 40.sp,
        lineHeight = 46.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.W900,
        textAlign = TextAlign.Center,
    ),
    bodyLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
