package com.ataglance.walletglance.core.presentation.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.innerVolumeShadow(
    shape: Shape,
    offset: Dp = 2.dp,
    blur: Dp = 2.dp,
    spread: Dp = 1.dp,
    whiteColor: Color = Color.White.copy(.55f),
    blackColor: Color = Color.Black.copy(.2f)
) = innerShadow(
    shape = shape,
    color = whiteColor,
    offsetX = -offset,
    offsetY = offset,
    blur = blur,
    spread = spread
)
.innerShadow(
    shape = shape,
    color = blackColor,
    offsetX = offset,
    offsetY = -offset,
    blur = blur,
    spread = spread
)