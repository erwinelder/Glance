package com.ataglance.walletglance.core.presentation.component.icon

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.model.RotatingGradientAnimState
import com.ataglance.walletglance.core.presentation.preview.PreviewColumnContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RotatingGradientIcon(
    iconPathsRes: IconPathsRes,
    animState: RotatingGradientAnimState,
    iconGradientColor: Pair<Color, Color> = GlanciColors.iconPrimaryGlassGradientPair
) {
    val firstGradientColor by animateColorAsState(targetValue = iconGradientColor.first)
    val secondGradientColor by animateColorAsState(targetValue = iconGradientColor.second)

    val iconSize by remember(animState) {
        derivedStateOf {
            val size = when (animState) {
                RotatingGradientAnimState.Idle -> 48.dp
                RotatingGradientAnimState.Active -> 80.dp
                RotatingGradientAnimState.Calm -> 104.dp
            }
            when {
                iconPathsRes.ratio < 1.0f -> (size * iconPathsRes.ratio) to size
                iconPathsRes.ratio > 1.0f -> size to (size / iconPathsRes.ratio)
                else -> size to size
            }
        }
    }
    val iconSizeX by animateDpAsState(
        targetValue = iconSize.first,
        animationSpec = tween(durationMillis = 220, delayMillis = 90)
    )
    val iconSizeY by animateDpAsState(
        targetValue = iconSize.second,
        animationSpec = tween(durationMillis = 220, delayMillis = 90)
    )
    val angleDeg by getAnimatedAngleAsState(animateState = animState)

    val modifier = Modifier.size(iconSizeX, iconSizeY)

    val paths = remember(iconPathsRes.paths) {
        iconPathsRes.paths.map { PathParser().parsePathString(pathData = it).toPath() }
    }

    val globalBounds = paths
        .map { it.getBounds() }
        .reduce { acc, rect -> acc.encompass(rect) }

    Canvas(modifier = modifier) {
        val scaleX = size.width / globalBounds.width
        val scaleY = size.height / globalBounds.height
        val scale = minOf(scaleX, scaleY)

        val dx = (size.center.x - globalBounds.center.x) * scale
        val dy = (size.center.y - globalBounds.center.y) * scale

        withTransform({
            translate(left = dx, top = dy)
            scale(scaleX = scale, scaleY = scale)
        }) {
            paths.forEach { path ->
                val bounds = path.getBounds()
                val cx = (bounds.left + bounds.right) / 2f
                val cy = (bounds.top + bounds.bottom) / 2f
                val radius = maxOf(bounds.width, bounds.height) / 2f

                val rad = angleDeg.toDouble() * PI / 180.0
                val cosA = cos(rad).toFloat()
                val sinA = sin(rad).toFloat()

                val start = Offset(x = cx + cosA * radius, y = cy + sinA * radius)
                val end = Offset(x = cx - cosA * radius, y = cy - sinA * radius)

                val brush = Brush.Companion.linearGradient(
                    colors = listOf(firstGradientColor, secondGradientColor),
                    start = start,
                    end = end
                )

                drawPath(path = path, brush = brush)
            }
        }
    }
}

@Composable
private fun getAnimatedAngleAsState(animateState: RotatingGradientAnimState): State<Float> {
    val anim = remember { Animatable(initialValue = 135f) }

    LaunchedEffect(animateState) {
        when (animateState) {
            RotatingGradientAnimState.Idle -> {
                // animate to the next circle with degree of 135
                val thisCircle = (anim.value.minus(135f) / 360f).toInt()
                val targetValue = (thisCircle + 1) * 360f + 135f
                val duration = (2000 / (360f / (targetValue - anim.value)).toDouble()).toInt()
                anim.animateTo(
                    targetValue = targetValue,
                    animationSpec = tween(durationMillis = duration, easing = EaseInOut)
                )
            }
            RotatingGradientAnimState.Active -> {
                // continuous fast rotation
                while (true) {
                    anim.animateTo(
                        targetValue = anim.value + 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 4000,
                                easing = CubicBezierEasing(.59f, .3f, .42f, .7f)
                            ),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                }
            }
            RotatingGradientAnimState.Calm -> {
                // continuous slow rotation
                while (true) {
                    anim.animateTo(
                        targetValue = anim.value + 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 12000,
                                easing = LinearEasing
                            ),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                }
            }
        }
    }

    return remember { derivedStateOf { anim.value } }
}

private fun Rect.encompass(other: Rect): Rect {
    return Rect(
        left = minOf(left, other.left),
        top = minOf(top, other.top),
        right = maxOf(right, other.right),
        bottom = maxOf(bottom, other.bottom)
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun RotatingGradientIconPreview() {
    var animState by remember { mutableStateOf(RotatingGradientAnimState.Idle) }

    PreviewColumnContainer(appTheme = AppTheme.LightDefault) {
        RotatingGradientIcon(
            iconPathsRes = IconPathsRes.Email,
            animState = animState
        )
        SmallPrimaryButton(text = "Animate") {
            animState = when (animState) {
                RotatingGradientAnimState.Idle -> RotatingGradientAnimState.Active
                RotatingGradientAnimState.Active -> RotatingGradientAnimState.Calm
                RotatingGradientAnimState.Calm -> RotatingGradientAnimState.Idle
            }
        }
    }
}