package com.ataglance.walletglance.core.presentation.component.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.text.Title
import com.ataglance.walletglance.core.presentation.preview.PreviewColumnContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedTitleWithIcon(
    text: String,
    iconPaths: List<String>,
    isLoading: Boolean
) {
    val gap by animateDpAsState(
        targetValue = if (isLoading) 0.dp else 12.dp
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        RotatingGradientMultiPathIcon(
            pathDataList = iconPaths,
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
fun RotatingGradientMultiPathIcon(
    pathDataList: List<String>,
    isLoading: Boolean,
    colors: List<Color> = listOf(
        GlanciColors.primaryGlassGradientPair.first,
        GlanciColors.glassGradientPair.first
    )
) {
    val angleDeg by getAngle(isLoading = isLoading)
    val iconSize by animateDpAsState(
        targetValue = if (isLoading) 80.dp else 40.dp,
        animationSpec = tween(220, delayMillis = 90)
    )

    val modifier = Modifier.size(iconSize)

    val paths = remember(pathDataList) {
        pathDataList.map { PathParser().parsePathString(it).toPath() }
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

                val rad = Math.toRadians(angleDeg.toDouble())
                val cosA = cos(rad).toFloat()
                val sinA = sin(rad).toFloat()

                val start = Offset(cx + cosA * radius, cy + sinA * radius)
                val end = Offset(cx - cosA * radius, cy - sinA * radius)

                val brush = Brush.linearGradient(colors, start, end)

                drawPath(path = path, brush = brush)
            }
        }
    }
}

@Composable
private fun getAngle(isLoading: Boolean): State<Float> {
    val anim = remember { Animatable(135f) }

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
            val stopValue = (thisCircle + 1) * 360f + 135f
            val duration = (2000 / (360f / (stopValue - anim.value)).toDouble()).toInt()
            anim.animateTo(
                targetValue = stopValue,
                animationSpec = tween(
                    durationMillis = duration,
                    easing = CubicBezierEasing(.59f, .3f, .42f, .7f)
                )
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
//    val iconPaths = listOf(
//        "M38.284,1C43.694,1 48.08,5.386 48.08,10.796V39.204C48.08,44.614 43.694,49 38.284,49H11.796C6.386,49 2,44.614 2,39.204V10.796C2,5.386 6.386,1 11.796,1H38.284ZM14.481,33.351C13.79,33.351 13.231,33.91 13.231,34.601C13.231,35.291 13.79,35.851 14.481,35.851H22.644C23.334,35.85 23.893,35.291 23.894,34.601C23.894,33.91 23.334,33.351 22.644,33.351H14.481ZM14.481,23.75C13.79,23.75 13.231,24.31 13.231,25C13.231,25.69 13.79,26.25 14.481,26.25H35.052C35.742,26.25 36.302,25.69 36.302,25C36.302,24.31 35.742,23.75 35.052,23.75H14.481ZM14.481,14.15C13.79,14.15 13.231,14.71 13.231,15.4C13.231,16.091 13.79,16.65 14.481,16.65H35.052C35.742,16.65 36.301,16.09 36.302,15.4C36.302,14.71 35.742,14.151 35.052,14.15H14.481Z"
//    )
//    val iconPaths = listOf(
//        "M40.117,32.888C41.107,35.858 41.353,42.631 40.602,46.363C40.393,47.405 39.469,48.077 38.412,48.182C29.143,49.1 19.924,49.106 11.563,48.198C10.521,48.085 9.617,47.417 9.408,46.39C8.651,42.664 8.895,35.865 9.887,32.888C11.806,27.132 18.026,20.098 25.002,20.098C31.978,20.098 38.199,27.132 40.117,32.888Z",
//        "M25,20.121C30.315,20.121 34.623,15.812 34.623,10.498C34.623,5.183 30.315,0.875 25,0.875C19.685,0.875 15.377,5.183 15.377,10.498C15.377,15.812 19.685,20.121 25,20.121Z"
//    )
    val iconPaths = listOf(
        "M23.757,2.74C23.757,1.792 22.961,1.032 22.02,1.149C19.265,1.494 16.584,2.314 14.097,3.581C10.72,5.302 7.798,7.797 5.571,10.863C3.343,13.929 1.873,17.479 1.28,21.222C0.687,24.965 0.988,28.796 2.16,32.4C3.331,36.004 5.338,39.28 8.018,41.96C10.698,44.64 13.974,46.648 17.578,47.819C21.183,48.99 25.013,49.291 28.756,48.698C31.411,48.278 33.968,47.416 36.325,46.156C37.168,45.705 37.374,44.613 36.813,43.84L32.493,37.881C31.998,37.198 31.073,37.007 30.299,37.339C29.279,37.776 28.206,38.087 27.103,38.262C25.007,38.594 22.862,38.425 20.844,37.769C18.825,37.113 16.99,35.989 15.49,34.488C13.989,32.988 12.865,31.153 12.209,29.135C11.553,27.116 11.384,24.971 11.716,22.875C12.048,20.779 12.872,18.791 14.119,17.074C15.367,15.357 17.003,13.96 18.894,12.996C19.963,12.451 21.097,12.054 22.265,11.811C23.102,11.638 23.757,10.934 23.757,10.078V2.74Z",
        "M47.238,23.735C48.187,23.735 48.947,22.939 48.829,21.998C48.563,19.868 48.011,17.778 47.186,15.787C45.98,12.873 44.211,10.226 41.981,7.996C39.751,5.766 37.103,3.997 34.19,2.79C32.198,1.965 30.109,1.414 27.979,1.148C27.038,1.03 26.242,1.79 26.242,2.738L26.242,10.076C26.242,10.932 26.896,11.635 27.734,11.809C28.557,11.981 29.365,12.229 30.146,12.552C31.778,13.228 33.26,14.219 34.509,15.467C35.758,16.716 36.748,18.199 37.424,19.83C37.748,20.611 37.996,21.419 38.167,22.243C38.341,23.08 39.045,23.735 39.9,23.735H47.238Z",
        "M39.281,42.051C39.844,42.821 40.944,42.961 41.632,42.301C43.562,40.447 45.175,38.278 46.397,35.88C47.664,33.393 48.485,30.712 48.829,27.957C48.947,27.016 48.187,26.22 47.238,26.22H39.9C39.045,26.22 38.341,26.874 38.167,27.712C37.925,28.879 37.527,30.013 36.982,31.083C36.472,32.085 35.839,33.016 35.102,33.855C34.544,34.49 34.439,35.432 34.939,36.115L39.281,42.051Z"
    )

    var isLoading by remember { mutableStateOf(false) }

    PreviewColumnContainer(appTheme = AppTheme.LightDefault) {
        AnimatedTitleWithIcon(
            text = "Animated Title",
            iconPaths = iconPaths,
            isLoading = isLoading
        )
        SmallPrimaryButton(text = "Animate") {
            isLoading = !isLoading
        }
    }
}