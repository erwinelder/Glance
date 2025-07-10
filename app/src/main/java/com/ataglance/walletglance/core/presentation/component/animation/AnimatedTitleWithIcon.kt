package com.ataglance.walletglance.core.presentation.component.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.text.Title
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewColumnContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedTitleWithIcon(
    text: String,
    iconPathsRes: IconPathsRes,
    isLoading: Boolean
) {
    val gap by animateDpAsState(
        targetValue = if (isLoading) 0.dp else 12.dp
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        RotatingGradientIcon(
            iconPathsRes = iconPathsRes,
            isLoading = isLoading,
        )
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = {
                (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                        scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
                    .togetherWith(fadeOut(animationSpec = tween(90)))
            }
        ) { isLoading ->
            if (!isLoading) {
                Title(text = text)
            }
        }
    }
}

@Composable
fun RotatingGradientIcon(
    iconPathsRes: IconPathsRes,
    isLoading: Boolean
) {
    val colors = GlanciColors.animatedIconGradient

    val iconSize by animateDpAsState(
        targetValue = if (isLoading) 80.dp else 40.dp,
        animationSpec = tween(durationMillis = 220, delayMillis = 90)
    )
    val angleDeg by getAnimatedAngleAsState(isLoading = isLoading)

    val modifier = Modifier
        .padding(vertical = 6.dp)
        .size(iconSize)

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

//                Math.toRadians(angleDeg.toDouble())
                val rad = angleDeg.toDouble() * PI / 180.0
                val cosA = cos(rad).toFloat()
                val sinA = sin(rad).toFloat()

                val start = Offset(x = cx + cosA * radius, y = cy + sinA * radius)
                val end = Offset(x = cx - cosA * radius, y = cy - sinA * radius)

                val brush = Brush.linearGradient(colors = colors, start = start, end = end)

                drawPath(path = path, brush = brush)
            }
        }
    }
}

@Composable
private fun getAnimatedAngleAsState(isLoading: Boolean): State<Float> {
    val anim = remember { Animatable(initialValue = 135f) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            // continuous rotation
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
        } else {
            // animate to the next circle with degree of 135
            val thisCircle = (anim.value.minus(135f) / 360f).toInt()
            val targetValue = (thisCircle + 1) * 360f + 135f
            val duration = (2000 / (360f / (targetValue - anim.value)).toDouble()).toInt()
            anim.animateTo(
                targetValue = targetValue,
                animationSpec = tween(durationMillis = duration, easing = EaseInOut)
            )
        }
    }

    return remember { derivedStateOf { anim.value } }
}

fun Rect.encompass(other: Rect): Rect {
    return Rect(
        left = minOf(left, other.left),
        top = minOf(top, other.top),
        right = maxOf(right, other.right),
        bottom = maxOf(bottom, other.bottom)
    )
}



@Preview(device = PIXEL_7_PRO)
@Composable
private fun AnimatedTitleWithIconPreview() {
    var isLoading by remember { mutableStateOf(false) }

    PreviewColumnContainer(appTheme = AppTheme.LightDefault) {
        AnimatedTitleWithIcon(
            text = "Animated Title",
            iconPathsRes = IconPathsRes.User,
            isLoading = isLoading
        )
        SmallPrimaryButton(text = "Animate") {
            isLoading = !isLoading
        }
    }
}